package app.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.database.cl_cliente;
import app.database.cl_general_param;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;
import app.utils.cl_util;

public class cl_hoja_ruta_adapter extends BaseAdapter {
	Context context;
	ArrayList<cl_hoja_ruta> hojaRutaList;
	int paramOpMenu;
	private static LayoutInflater inflater = null;
	ProgressDialog prgDialog;
	
	public cl_hoja_ruta_adapter(Context context, ArrayList<cl_hoja_ruta> list,int paramOpMenu) {

		this.context = context;
		hojaRutaList = list;
		this.paramOpMenu=paramOpMenu;
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
			convertView = inflater.inflate(R.layout.hoja_ruta_list_item, null);

		}

		final TextView nameHojaRuta = (TextView) convertView.findViewById(R.id.nameHojaRuta);
		nameHojaRuta.setText("HR"+hojaRuta.getHrNumeroHoja());

		ImageView btnHojaRuta = (ImageView) convertView.findViewById(R.id.btnHojaRuta);
		btnHojaRuta.setTag(hojaRuta.getHrCodigo());
		
		final RelativeLayout itemHojaRuta=(RelativeLayout) convertView.findViewById(R.id.itemHojaRuta);
		
		if(hojaRuta != null && hojaRuta.getHrKilometraje() > 0 && hojaRuta.getHrFlgLiq().equals(false)){
			itemHojaRuta.setBackgroundColor(Color.rgb(254, 238, 145));//iniciada 
			nameHojaRuta.setTextColor(Color.BLACK);
		}else if(hojaRuta != null && hojaRuta.getHrKilometraje() > 0 && hojaRuta.getHrFlgLiq().equals(true)){
			itemHojaRuta.setBackgroundColor(Color.rgb(159, 218,158));//liquidada 
			nameHojaRuta.setTextColor(Color.BLACK);
		}else{
			itemHojaRuta.setBackgroundColor(Color.rgb(136, 136,136));//normal
			nameHojaRuta.setTextColor(Color.WHITE);
		}
			
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final cl_hoja_ruta hr = hojaRutaList.get(position);
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				cl_hoja_ruta hrBD = db.getHojaRuta(hr.getHrCodigo());
				
				if(paramOpMenu == cl_constantes.OpMnuIniciarHR){
					if(hrBD.getHrKilometraje()>0){
						cl_util.DisplayMessage(context, "La hoja de ruta ya se encuentra iniciada", "L");
					}else{
						//*******Se crea mensaje a mostrar**************************
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Confirmar")
							.setMessage("Desea iniciar la hoja de ruta HR"+hr.getHrNumeroHoja()+"?")
							.setCancelable(false)
							.setIcon(R.drawable.ic_launcher)
							.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog, int id){
									db_distribuidor_assets db=new db_distribuidor_assets(context);
									int result=db.IniciarHojaRuta(hr.getHrCodigo(),cl_general_param.getInstance().getUsrCodMod());
									if(result>0){
										itemHojaRuta.setBackgroundColor(Color.rgb(254, 238, 145));
										nameHojaRuta.setTextColor(Color.BLACK);
										cl_util.DisplayMessage(context, "Se ha iniciado correctamente la hoja de ruta", "L");
									}else{
										cl_util.DisplayMessage(context, "ocurrió un problema al iniciar la hoja de ruta", "L");
									}
									//prgDialog.hide();
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
					
					
				}else if(paramOpMenu==cl_constantes.OpMnuDestinos){					
					
					//db_distribuidor_assets db=new db_distribuidor_assets(context);
					ArrayList<cl_cliente> lista=db.getClientesByHojaRuta(hr.getHrCodigo());
					if(lista.size()>0){
						Bundle bundle = new Bundle();
						bundle.putString("paramHrDesc", "HR"+hr.getHrNumeroHoja());
						bundle.putInt("paramHrId", hr.getHrCodigo());
						bundle.putInt("esHojaRutaUnica",0);
						bundle.putInt("paramOpMenu",cl_constantes.OpMnuDestinos);
						Intent menu3 = new Intent(context, cl_cliente_lista.class);
						menu3.putExtras(bundle);
						context.startActivity(menu3);
						((Activity)context).finish();
					}else
					{
						cl_util.DisplayMessage(context, "No se encontraron clientes para la hoja de ruta HR"+hr.getHrNumeroHoja(), "L");
					}
				}
			}
		});
				
		return convertView;
	}
}
