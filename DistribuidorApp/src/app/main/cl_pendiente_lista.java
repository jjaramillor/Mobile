package app.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;

public class cl_pendiente_lista extends Activity {
	Context context;
	ProgressDialog prgDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pendiente_list_ui);
		
		context = this;
		
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
			
		ImageView btnAtras = (ImageView)findViewById(R.id.imbAtras);
				
		btnAtras.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				//Bundle bundle = new Bundle();					
				Intent menu = new Intent( cl_pendiente_lista.this,cl_menu_main.class);
				startActivity(menu);
				//menu.putExtras(bundle);
				finish();
			}
		});
		
		ListView lvHojaRuta=(ListView)findViewById(R.id.listPendientes);
		cl_pendiente_adapter adapter = getPendienteAdapter();
		lvHojaRuta.setAdapter(adapter);
		
	}

	public cl_pendiente_adapter getPendienteAdapter(){
		//context = this;		
		db_distribuidor_assets db = new db_distribuidor_assets(context);
		ArrayList<cl_hoja_ruta> lista= new ArrayList<cl_hoja_ruta>();
		lista = db.getHojaRutaIniciadaListarSync();	
		cl_pendiente_adapter adapter = new cl_pendiente_adapter(context, lista);
		return adapter;
	}
}
