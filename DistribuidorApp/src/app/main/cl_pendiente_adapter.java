package app.main;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.services.ResultSync;
import app.services.cl_service_HojaRuta;
import app.utils.cl_util;

public class cl_pendiente_adapter extends BaseAdapter {
	
	Context context;
	ArrayList<cl_hoja_ruta> hojaRutaList;	
	private static LayoutInflater inflater = null;
	ProgressDialog prgDialog;
	final RelativeLayout itemHojaRuta = null;
	public cl_pendiente_adapter(Context context, ArrayList<cl_hoja_ruta> list) {

		this.context = context;
		hojaRutaList = list;		
	}

	@Override
	public int getCount() {

		return hojaRutaList.size();
	}

	@Override
	public Object getItem(int position) {

		return hojaRutaList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		cl_hoja_ruta hojaRuta = hojaRutaList.get(position);

		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.pendiente_list_item_ui, null);

		}
		if(hojaRuta != null){
		}
		
		TextView textHrSync = (TextView) convertView.findViewById(R.id.textHrSync);
		textHrSync.setText("HR"+hojaRuta.getHrNumeroHoja());
		
		TextView textNroSync = (TextView) convertView.findViewById(R.id.textNroSync);
		textNroSync.setText(hojaRuta.getSync() +"/"+ hojaRuta.getTotal());

		final ImageView imvSync = (ImageView) convertView.findViewById(R.id.imvSync);
		imvSync.setTag(hojaRuta.getHrCodigo());
		
		imvSync.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				final cl_hoja_ruta hr = hojaRutaList.get(position);		
				
				//validar que exista pedidos dentro de la hoja de ruta para sincronizar
				/*
				if(hr.getSync() == hr.getTotal()){
					cl_util.DisplayMessage(context, "No hay pedidos pendientes para sincronizar", "L");
					//return;
				}
				*/
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				final cl_hoja_ruta hrBD = db.getHojaRuta(hr.getHrCodigo());
								
				//Enviar a sincronizar HR
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Confirmar")
					.setMessage("Desea sincronizar la HR"+hr.getHrNumeroHoja()+"?")
					.setCancelable(false)
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int id){
							if(cl_util.isOnline(context)){
								new HojaRutaTask().execute(hrBD);
							}
							else
							{
								String ms = "No hay conexión a internet.\n-Verifique su conexión, y vuelva a intentar";
								cl_util.DisplayMessage(context, ms, "L");	
							}
													
						}
					})
					.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int id){
							dialog.dismiss();
						}
					});
				
				final AlertDialog alert = builder.create();
				alert.show();
				//************************************************************
			}
		});		
		
		return convertView;
	}
	
	private void ActualizarEntrada () {		
		db_distribuidor_assets db = new db_distribuidor_assets(context);
		ArrayList<cl_hoja_ruta> lista= new ArrayList<cl_hoja_ruta>();
		hojaRutaList = db.getHojaRutaIniciadaListarSync();	
         notifyDataSetChanged();
      }
	
	public void response(ResultSync result ){
		
		if(result.getResultPedido() && !result.getResultHR())	{				
			cl_util.DisplayMessage(context, "Se han sincronizado los pedidos", "L");			
		}
		else if(result.getResultPedido() && result.getResultHR())
		{
			cl_util.DisplayMessage(context, "La hoja de ruta ha sido sincronizada", "L");			
			
		}
		else if(!result.getResultPedido() && !result.getResultHR())
		{
			cl_util.DisplayMessage(context, "La hoja de ruta se encuentra sincronizada ", "L");			
			
		}
		else
			cl_util.DisplayMessage(context, "ocurrió un problema al sincronizar los pedidos", "L");	
	
		ActualizarEntrada();
				
	}
	
	private class HojaRutaTask extends AsyncTask<cl_hoja_ruta, Integer, ResultSync> {
		@Override
		protected ResultSync doInBackground(cl_hoja_ruta... hrs) {			
			//Llamar el metodo de sincronizacion
			//el metodo retornara un objeto ResultSync	boolean
			ResultSync response = new ResultSync();			
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			
			response.setResultPedido(db.PedidoSync(hrs[0].getHrCodigo(), 0 ));//Envio cliCodigo = 0 para sincronizacion manual
			
			if(response.getResultPedido())
			{				
				response.setResultHR(db.HojaRutaSync(hrs[0])); 		
			}
			return response;
		}	
		
		@Override
		  protected void onPreExecute() {
		    super.onPreExecute();
		    cl_util.DisplayMessage(context, "Se inició el proceso de sincronización. ", "L");
		  }
		
		@Override
		protected void onPostExecute(ResultSync result) {
					
			response(result);
			super.onPostExecute(result);
		}		

	}
}
