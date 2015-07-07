package app.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.database.cl_cliente;
import app.database.cl_hoja_ruta_detalle;
import app.database.cl_pedido;
import app.database.cl_pedido_data_holder;
import app.database.db_distribuidor_assets;
import app.utils.cl_util;

public class cl_cliente_adapter extends BaseAdapter {
	Context context;
	ArrayList<cl_cliente> _lista;
	String paramHrDesc = "";
	int paramHrId = 0;
	int esHojaRutaUnica = 0;
	int paramOpMenu = 0;
	private static LayoutInflater inflater = null;

	public cl_cliente_adapter(Context context, ArrayList<cl_cliente> list,
			String paramHrDesc, int paramHrId, int esHojaRutaUnica,
			int paramOpMenu) {

		this.context = context;
		this.paramHrDesc = paramHrDesc;
		this.paramHrId = paramHrId;
		this.esHojaRutaUnica = esHojaRutaUnica;
		this.paramOpMenu = paramOpMenu;
		_lista = list;
	}

	@Override
	public int getCount() {

		return _lista.size();
	}

	@Override
	public Object getItem(int position) {

		return _lista.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		cl_cliente obj = _lista.get(position);
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.expandable_list_item, null);

		}

		TextView text = (TextView) convertView.findViewById(R.id.text);
		text.setText(obj.getCliNombre());
		TextView textDireccion = (TextView) convertView.findViewById(R.id.textDireccion);
		textDireccion.setText(obj.getDedNomVia());

		//ICONO MAS
		ImageView imvVerMas = (ImageView) convertView.findViewById(R.id.expandable_toggle_button);
		if(obj.getHdrFlgLiq()==true)
			imvVerMas.setImageResource(R.drawable.item_mas_ok);
		
		// DESCARGO
		ImageView imvDescargo = (ImageView) convertView.findViewById(R.id.imvDescargo);
		if(obj.getHdrFlgLiq()==true)
			imvDescargo.setImageResource(R.drawable.item_descargo_ok);

		imvDescargo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cl_cliente obj = _lista.get(position);
				
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				
				cl_hoja_ruta_detalle hrdObj = db.getHojaRutaDetalle(obj.getHrCodigo(),obj.getCliCodigo(),obj.getPedCodigo());
				
				if (hrdObj.getHrdFecLlega()==null){
					cl_util.DisplayMessage(context,"Debe registrar primero la llegada", "L");
				}else{
					
					List<cl_pedido> lista = db.getAllPedidos(obj.getCliCodigo(),obj.getDesCodigo(), obj.getPedIdDireccion());

					if (lista.size() > 0) {
						Bundle bundle = new Bundle();
						bundle.putString("paramHrDesc", paramHrDesc);
						bundle.putInt("paramHrId", paramHrId);
						bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
						bundle.putString("paramcliNombre", obj.getCliNombre());
						bundle.putInt("paramCliId", obj.getCliCodigo());
						bundle.putInt("paramDesCodigo", obj.getDesCodigo());
						bundle.putInt("paramPedIdDireccion",obj.getPedIdDireccion());
						bundle.putInt("paramOpMenu", paramOpMenu);

						Intent detalle = new Intent(context, cl_pedido_lista.class);
						detalle.putExtras(bundle);
						context.startActivity(detalle);
						((Activity) context).finish();
					} else {
						cl_util.DisplayMessage(
								context,
								"No se encontraron pedidos para el cliente "
										+ obj.getCliNombre(), "L");
					}
				}
				
				

			}
		});

		// LIQUIDAR
		ImageView imvLiquidar = (ImageView) convertView.findViewById(R.id.imvLiquidar);
		if(obj.getHdrFlgLiq()==true)
			imvLiquidar.setImageResource(R.drawable.item_liquidar_ok);

		imvLiquidar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				cl_cliente obj = _lista.get(position);
				
				db_distribuidor_assets db = new db_distribuidor_assets(context);
															
				cl_hoja_ruta_detalle hrdObj = db.getHojaRutaDetalle(obj.getHrCodigo(),obj.getCliCodigo(),obj.getPedCodigo());
				
				if (hrdObj.getHrdFecLlega()==null){
					cl_util.DisplayMessage(context,"Debe registrar primero la llegada", "L");
				}else{
					
					ArrayList<cl_pedido> lista = db.getPedidosNoLiquidados(obj.getCliCodigo(), obj.getDesCodigo(),obj.getPedIdDireccion());

					if (lista.size() > 0) {

						cl_pedido_data_holder.getInstance().setData(lista);

						Bundle bundle = new Bundle();
						bundle.putString("paramHrDesc", paramHrDesc);
						bundle.putInt("paramHrId", paramHrId);
						bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
						bundle.putString("paramcliNombre", obj.getCliNombre());
						bundle.putInt("paramCliId", obj.getCliCodigo());
						bundle.putInt("paramDesCodigo", obj.getDesCodigo());
						bundle.putInt("paramPedIdDireccion",obj.getPedIdDireccion());
						bundle.putInt("paramOpMenu", paramOpMenu);
						bundle.putInt("paramLiquidarDirecto", 1);

						Intent detalle = new Intent(context, cl_liquidar.class);
						detalle.putExtras(bundle);
						context.startActivity(detalle);
						((Activity) context).finish();
					} else {
						cl_util.DisplayMessage(context,
								"No existen pedidos disponibles a liquidar", "L");
					}
					
				}
				
				

			}
		});

		// PARTIDA
		final ImageView imvPartida = (ImageView) convertView.findViewById(R.id.imvPartida);
		if(obj.getHrdFecParti()!=null)
			imvPartida.setImageResource(R.drawable.item_partida_ok);

		imvPartida.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				cl_cliente obj = _lista.get(position);
				
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				cl_hoja_ruta_detalle hrdObj = db.getHojaRutaDetalle(obj.getHrCodigo(),obj.getCliCodigo(),obj.getPedCodigo());

				
				
				if (hrdObj.getHrdFecParti()==null) {
						List<cl_pedido> pedidos = db.getAllPedidos(obj.getCliCodigo(),obj.getDesCodigo(), obj.getPedIdDireccion());
						//String result=db.updHojaRutaPartida(obj.getHrCodigo(),obj.getCliCodigo(),obj.getPedCodigo());
						String result=db.updHojaRutaPartida(obj.getHrCodigo(),pedidos);
						if(result!="")
						{
							imvPartida.setImageResource(R.drawable.item_partida_ok);
							cl_util.DisplayMessage(context,"Se ha resgistrado el inicio correctamente a las "+result, "L");
						}else{
							cl_util.DisplayMessage(context,"Ocurrió un problema al intentar registrar la partida", "L");
						}
					
				} else {
					String fecha=hrdObj.getHrdFecParti();
					String hora=hrdObj.getHrdHoraParti();
					cl_util.DisplayMessage(context,"Ya se ha registrado la partida a las "+fecha+" "+hora, "L");
				}

			}
		});

		// LLEGADA
		final ImageView imvLlegada = (ImageView) convertView.findViewById(R.id.imvLlegada);
		if(obj.getHrdFecLlega()!=null)
			imvLlegada.setImageResource(R.drawable.item_llegada_ok);
		
		imvLlegada.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				cl_cliente obj = _lista.get(position);
				
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				cl_hoja_ruta_detalle hrdObj = db.getHojaRutaDetalle(obj.getHrCodigo(),obj.getCliCodigo(),obj.getPedCodigo());

				
				if (hrdObj.getHrdFecParti()==null){
					cl_util.DisplayMessage(context,"Debe registrar primero la partida", "L");
				}else{
					if (hrdObj.getHrdFecLlega()==null) {
						List<cl_pedido> pedidos = db.getAllPedidos(obj.getCliCodigo(),obj.getDesCodigo(), obj.getPedIdDireccion());
						//String result=db.updHojaRutaLlegada(obj.getHrCodigo(),obj.getCliCodigo(),obj.getPedCodigo());
						String result=db.updHojaRutaLlegada(obj.getHrCodigo(),pedidos);
						
						if(result!="")
						{
							imvLlegada.setImageResource(R.drawable.item_llegada_ok);
							cl_util.DisplayMessage(context,"Se ha resgistrado la llegada correctamente a las "+result, "L");
						}else{
							cl_util.DisplayMessage(context,"Ocurrió un problema al intentar registrar la llegada", "L");
						}
					
				} else {
					
					String fecha=hrdObj.getHrdFecLlega();
					String hora=hrdObj.getHrdHoraLlega();
					cl_util.DisplayMessage(context,"Ya se ha registrado la llegada a las "+fecha+" "+hora, "L");
				}
				}
				
			

			}
		});

		return convertView;
	}
}
