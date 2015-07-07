package app.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import app.database.cl_general_param;
import app.database.cl_hoja_ruta;
import app.database.cl_pedido;
import app.database.cl_pedido_data_holder;
import app.database.db_distribuidor_assets;
import app.services.ResultSync;
import app.services.cl_service_HojaRuta;
import app.services.cl_service_Pedido;
import app.utils.cl_constantes;
import app.utils.cl_util;

public class cl_liquidar extends Activity {
	Context context;
	ProgressDialog prgDialog;
	// *******Parametros de Navegacion*******
	String paramHrDesc = "";
	int paramHrId = 0;
	String paramcliNombre = "";
	int paramCliId = 0;
	int paramDesCodigo = 0;
	int paramPedIdDireccion = 0;
	int esHojaRutaUnica = 0;
	int paramOpMenu = 0;
	ArrayList<cl_pedido> _listaPedidos = new ArrayList<cl_pedido>();
	int paramLiquidarDirecto = 0;
	// **************************************

	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		this.setContentView(R.layout.liquidar_pedido_ui);

		context = this;

		// progress
		prgDialog = new ProgressDialog(this);

		// *******Parametros de Navegacion*******
		getParameters();

		// **************************************
		
		//*******************Boton Regresar***************************
		ImageView btnHome = (ImageView)findViewById(R.id.imvHome);
		btnHome.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent menu = new Intent( context,cl_menu_main.class);
				startActivity(menu);
				finish();
			}
		});
		//****************************************************************

		ImageView btnAtras = (ImageView) findViewById(R.id.imbAtras);
		btnAtras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();

				bundle.putString("paramHrDesc", paramHrDesc);
				bundle.putInt("paramHrId", paramHrId);
				bundle.putString("paramcliNombre", paramcliNombre);
				bundle.putInt("paramCliId", paramCliId);
				bundle.putInt("paramDesCodigo", paramDesCodigo);
				bundle.putInt("paramPedIdDireccion", paramPedIdDireccion);
				bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
				bundle.putInt("paramOpMenu", paramOpMenu);

				Intent menu = new Intent(cl_liquidar.this,
						cl_pedido_lista.class);
				menu.putExtras(bundle);
				startActivity(menu);

				finish();
			}
		});

		// TOTAL
		ImageView btnTotal = (ImageView) findViewById(R.id.imvLiquidarTotal);
		btnTotal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// *******Se crea mensaje a mostrar**************************
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Confirmar")
						.setMessage(
								"Seguro que desea Liquidar Total, los pedidos seleccionados?")
						.setCancelable(false)
						.setIcon(R.drawable.ic_launcher)
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int id) {
										prgDialog.setMessage("Liquidando pedidos...");
										prgDialog.setCancelable(false);
										prgDialog.show();

										new LiquidarTotalTask().execute();

									}
								})
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
									}
								});

				final AlertDialog alert = builder.create();
				alert.show();
				// ************************************************************

			}
		});

		// PARCIAL
		ImageView btnParcial = (ImageView) findViewById(R.id.imvLiquidarParcial);
		btnParcial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();

				bundle.putString("paramHrDesc", paramHrDesc);
				bundle.putInt("paramHrId", paramHrId);
				bundle.putString("paramcliNombre", paramcliNombre);
				bundle.putInt("paramCliId", paramCliId);
				bundle.putInt("paramDesCodigo", paramDesCodigo);
				bundle.putInt("paramPedIdDireccion", paramPedIdDireccion);
				bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
				bundle.putInt("paramOpMenu", paramOpMenu);
				bundle.putString("paramTipoLiquidar",cl_constantes.PED_ENTREGADO_PARCIAL);
				bundle.putInt("paramLiquidarDirecto",paramLiquidarDirecto);
				
				Intent intent = new Intent(cl_liquidar.this,cl_liquidar_estados.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});

		// NO ENTREGADO
		ImageView btnNoEntregado = (ImageView) findViewById(R.id.imvLiquidarNoEntregado);
		btnNoEntregado.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Bundle bundle = new Bundle();

				bundle.putString("paramHrDesc", paramHrDesc);
				bundle.putInt("paramHrId", paramHrId);
				bundle.putString("paramcliNombre", paramcliNombre);
				bundle.putInt("paramCliId", paramCliId);
				bundle.putInt("paramDesCodigo", paramDesCodigo);
				bundle.putInt("paramPedIdDireccion", paramPedIdDireccion);
				bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
				bundle.putInt("paramOpMenu", paramOpMenu);
				bundle.putString("paramTipoLiquidar",cl_constantes.PED_NO_ENTREGADO_TOTAL);
				bundle.putInt("paramLiquidarDirecto",paramLiquidarDirecto);
				
				Intent intent = new Intent(cl_liquidar.this,cl_liquidar_estados.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
	}

	public void getParameters() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			paramHrDesc = bundle.getString("paramHrDesc");
			paramHrId = bundle.getInt("paramHrId");
			paramcliNombre = bundle.getString("paramcliNombre");
			paramCliId = bundle.getInt("paramCliId");
			paramDesCodigo = bundle.getInt("paramDesCodigo");
			paramPedIdDireccion = bundle.getInt("paramPedIdDireccion");
			esHojaRutaUnica = bundle.getInt("esHojaRutaUnica");
			paramOpMenu = bundle.getInt("paramOpMenu");
			paramLiquidarDirecto= bundle.getInt("paramLiquidarDirecto");		
			
			this._listaPedidos = cl_pedido_data_holder.getInstance().getData();
		}

	}

	private class LiquidarTotalTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String[] params) {
			//db_distribuidor_assets db = new db_distribuidor_assets(context);
			//return db.updLiquidarPedido(_listaPedidos, cl_constantes.PED_ENTREGADO_TOTAL);
			
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			boolean result=db.updLiquidarPedido(_listaPedidos,cl_constantes.PED_ENTREGADO_TOTAL);
			
			if(result){
					
				result=db.updLiquidarHojaRutaDetalle(_listaPedidos, cl_constantes.MOT_ENTREGADO_TOTAL, paramHrId,cl_constantes.LIQ_ENTREGADO_TOTAL,cl_general_param.getInstance().getUsrCodMod());
				
				
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {			
			if(result && cl_util.isOnline(context)){
				//Despues de grabar las dos tablas, ya se puede enviar a grabar a la base de datos remota
				_listaPedidos.get(0).setHrCodigo(paramHrId);
				new PedidoSyncOnline().execute(_listaPedidos.get(0)); 				
			}
			
			LiquidarTotalFinaly(result);
			prgDialog.hide();
		}
	}

	public void LiquidarTotalFinaly(Boolean success) {

		if (success) {
			cl_util.DisplayMessage(context, "Se han liquidado satisfactoriamente los pedidos seleccionados","L");

			db_distribuidor_assets db = new db_distribuidor_assets(context);
			Boolean liquidarHr = db.verificarLiquidarHojaRuta(paramHrId);

			if (liquidarHr) {
				prgDialog.setMessage("Liquidando Hoja de Ruta " + paramHrDesc+ "...");
				prgDialog.setCancelable(false);
				prgDialog.show();
				db.updLiquidarHojaRuta(paramHrId);				
				cl_util.DisplayMessage(context,"Se han liquidado la hoja de ruta " + paramHrDesc, "L");
				prgDialog.hide();
				
				if(cl_util.isOnline(context)){
					//Despues de grabar en la db local grabar en la remota	
					cl_hoja_ruta hr = new cl_hoja_ruta();
					hr = db.getHojaRuta(paramHrId);
					_listaPedidos.get(0).setHrCodigo(paramHrId);
					new HojaRutaOnlineSync().execute(hr); 				
				}
			}

			Bundle bundle = new Bundle();

			bundle.putString("paramHrDesc", paramHrDesc);
			bundle.putInt("paramHrId", paramHrId);
			bundle.putString("paramcliNombre", paramcliNombre);
			bundle.putInt("paramCliId", paramCliId);
			bundle.putInt("paramDesCodigo", paramDesCodigo);
			bundle.putInt("paramPedIdDireccion", paramPedIdDireccion);
			bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
			bundle.putInt("paramOpMenu", paramOpMenu);

			if(paramLiquidarDirecto==0){
				Intent menu = new Intent(cl_liquidar.this, cl_pedido_lista.class);
				menu.putExtras(bundle);
				startActivity(menu);
				finish();
			}else{
				Intent menu = new Intent(cl_liquidar.this, cl_cliente_lista.class);
				menu.putExtras(bundle);
				startActivity(menu);
				finish();
			}
			

		} else {
			cl_util.DisplayMessage(context,
					"ocurrió un problema al liquidar los pedido", "L");
		}
	}

	//Sincronizar online
	private class HojaRutaOnlineSync extends AsyncTask<cl_hoja_ruta, Integer, ResultSync> {

		@Override
		protected ResultSync doInBackground(cl_hoja_ruta... hr) {
			
			//Llamar el metodo de sincronizacion
			//el metodo retornara un objeto ResultSync
			ResultSync response = new ResultSync();			
			db_distribuidor_assets db = new db_distribuidor_assets(context);			
			response.setResultHR(db.HojaRutaSync(hr[0]));		
			
			return response;
		}	
		@Override
		protected void onPostExecute(ResultSync result) {
			
			super.onPostExecute(result);
		}		

	}
	
	private class PedidoSyncOnline extends AsyncTask<cl_pedido, Integer, ResultSync> {

		@Override
		protected ResultSync doInBackground(cl_pedido... ped) {
			
			//Llamar el metodo de sincronizacion
			//el metodo retornara un objeto ResultSync
			ResultSync response = new ResultSync();
			
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			response.setResultPedido(db.PedidoSync(ped[0].getHrCodigo(), ped[0].getCliCodigo()));
					
			return response;
		}	
		@Override
		protected void onPostExecute(ResultSync result) {
			
			super.onPostExecute(result);
		}		

	}

}
