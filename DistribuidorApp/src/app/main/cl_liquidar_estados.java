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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import app.database.cl_general_param;
import app.database.cl_hoja_ruta;
import app.database.cl_motivos;
import app.database.cl_pedido;
import app.database.cl_pedido_data_holder;
import app.database.db_distribuidor_assets;
import app.services.ResultSync;
import app.utils.cl_constantes;
import app.utils.cl_util;
import android.support.v7.widget.GridLayout;

public class cl_liquidar_estados extends Activity {

	Context context;
	ProgressDialog prgDialog;
	String _hdrCodMotivo="";
	int btnOn = 0;
	Boolean btnPressed = true;
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
    String paramTipoLiquidar="";
    int paramLiquidarDirecto = 0;
	// **************************************
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.liquidar_estados_ui);

		context = this;

		// progress
		prgDialog = new ProgressDialog(this);

		// *******Parametros de Navegacion*******
		getParameters();

		// **************************************
		// LinearLayout llayout = (LinearLayout)findViewById(R.id.llBotones);
		
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

				Intent menu = new Intent(cl_liquidar_estados.this,
						cl_liquidar.class);
				menu.putExtras(bundle);
				startActivity(menu);
				finish();
			}
		});

		CrearBotones();
		
		//Liquidar
		Button btnLiquidar = (Button) findViewById(R.id.btnLiquidarNoEntregado);
		btnLiquidar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(btnOn>0){
					// *******Se crea mensaje a mostrar**************************
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Confirmar")
							.setMessage(
									"Seguro que desea Liquidar , los pedidos seleccionados?")
							.setCancelable(false)
							.setIcon(R.drawable.ic_launcher)
							.setPositiveButton("Aceptar",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int id) {
											prgDialog.setMessage("Liquidando pedidos...");
											prgDialog.setCancelable(false);
											prgDialog.show();
											
											new LiquidarTotalTask().execute(paramTipoLiquidar);
											
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
				}else{
					cl_util.DisplayMessage(context, "Debe seleccionar un motivo para proceder a liquidar ","L");
				}
				

			}
		});
	}

	protected void CrearBotones() {

		db_distribuidor_assets db = new db_distribuidor_assets(context);
		ArrayList<cl_motivos> motivos = db.getMotivosNoEntrega();
		
		//ddlMotivos SPINNER
		Spinner ddlMotivos = (Spinner) findViewById(R.id.ddlMotivos);
		cl_spinner_motivos_adapter spinnerAdapter=new cl_spinner_motivos_adapter(context,motivos);
		ddlMotivos.setAdapter(spinnerAdapter);
		
		ddlMotivos.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
				cl_spinner_motivos_adapter adapter=(cl_spinner_motivos_adapter)parent.getAdapter();
				cl_motivos motivo=adapter.getItem(pos);
				
				
				Button btn = new Button(context);
				btn = ((Button) findViewById(Integer.parseInt(motivo.getCodReg())));
				btn.setBackgroundColor(Color.rgb(254, 238, 145));
				_hdrCodMotivo=motivo.getCodReg();
				
				if(!btnPressed){
					if(btnOn>0){
						Button btnOff = ((Button) findViewById(btnOn));
						btnOff.setBackgroundColor(Color.rgb(136, 136,136));
					}
					
					
					btnOn=btn.getId();
				}else if(btnOn==0){
					btnOn=btn.getId();
				}
				
				btnPressed=false;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//GridLayout
		GridLayout grid = (GridLayout) findViewById(R.id.glMotivos);
		grid.setColumnCount(5);
		

		for (final cl_motivos motivo : motivos) {
			
			/*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);*/
			GridLayout.LayoutParams params = new GridLayout.LayoutParams();
	        params.setMargins(10, 10,10, 10);
	        
			Button btn = new Button(this);
			btn.setId(Integer.parseInt(motivo.getCodReg()));
			btn.setTag(motivo.getCodReg());
			final int id_ = btn.getId();

			btn.setText(""+id_);
			//btn.setHeight(80);
			btn.setWidth(85);
			btn.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
			btn.setBackgroundColor(Color.rgb(136, 136,136));
			grid.addView(btn, params);
			
			Button btn1 = new Button(this);
			btn1 = ((Button) findViewById(id_));
			

			//Click Motivo
			btn1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					
					Button btnPress = ((Button) view);
					cl_util.DisplayMessage(cl_liquidar_estados.this,motivo.getDescripcion(), "");
					btnPressed=true;
					
					if(btnOn==btnPress.getId()){
						btnOn=0;
						btnPress.setBackgroundColor(Color.rgb(136, 136,136));
					}else{
						
						btnPress.setBackgroundColor(Color.rgb(254, 238, 145));
						_hdrCodMotivo=motivo.getCodReg();
						
						if(btnOn>0){
							Button btnOff = ((Button) findViewById(btnOn));
							btnOff.setBackgroundColor(Color.rgb(136, 136,136));
						}
												
						btnOn=btnPress.getId();
						
						//ddlMotivos
						Spinner ddlMotivos = (Spinner) findViewById(R.id.ddlMotivos);
						cl_spinner_motivos_adapter adapter=(cl_spinner_motivos_adapter)ddlMotivos.getAdapter();
						
						for(int i=0;i<adapter.getCount();i++){
							cl_motivos motivoSpinner=adapter.getItem(i);
							if(motivoSpinner.getCodReg()==motivo.getCodReg()){
								ddlMotivos.setSelection(i);
								break;
							}
						}
						
					}
					
				}
			});
		}

				

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
			paramTipoLiquidar=bundle.getString("paramTipoLiquidar");
			paramLiquidarDirecto= bundle.getInt("paramLiquidarDirecto");
			this._listaPedidos = cl_pedido_data_holder.getInstance().getData();
		}

	}

	private class LiquidarTotalTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String[] params) {
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			boolean result=db.updLiquidarPedido(_listaPedidos,params[0]);
			
			if(result){
					
				String hrdCodTipLiq="";
				
				if(paramTipoLiquidar.equals(cl_constantes.PED_ENTREGADO_PARCIAL)){
					hrdCodTipLiq=cl_constantes.LIQ_ENTREGADO_PARCIAL;
				}else if(paramTipoLiquidar.equals(cl_constantes.PED_NO_ENTREGADO_TOTAL)){
					hrdCodTipLiq=cl_constantes.LIQ_NO_ENTREGADO_TOTAL;
				}
				
				result=db.updLiquidarHojaRutaDetalle(_listaPedidos, _hdrCodMotivo, paramHrId,hrdCodTipLiq,cl_general_param.getInstance().getUsrCodMod());
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
			
			db_distribuidor_assets db=new db_distribuidor_assets(context);
			Boolean liquidarHr=db.verificarLiquidarHojaRuta(paramHrId);
			
			if(liquidarHr){
				prgDialog.setMessage("Liquidando Hoja de Ruta "+paramHrDesc+"...");
				prgDialog.setCancelable(false);
				prgDialog.show();
				db.updLiquidarHojaRuta(paramHrId);
				cl_util.DisplayMessage(context, "Se han liquidado la hoja de ruta "+paramHrDesc,"L");
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

			/*Intent menu = new Intent(cl_liquidar_estados.this,cl_pedido_lista.class);
			menu.putExtras(bundle);
			startActivity(menu);
			finish();*/
			
			if(paramLiquidarDirecto==0){
				Intent menu = new Intent(cl_liquidar_estados.this, cl_pedido_lista.class);
				menu.putExtras(bundle);
				startActivity(menu);
				finish();
			}else{
				Intent menu = new Intent(cl_liquidar_estados.this, cl_cliente_lista.class);
				menu.putExtras(bundle);
				startActivity(menu);
				finish();
			}

		} else {
			cl_util.DisplayMessage(context,"ocurrió un problema al liquidar los pedido","L");
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
