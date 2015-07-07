package app.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;
import app.utils.cl_util;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import app.utils.cl_util;

public class cl_hoja_ruta_lista extends Activity{
	
	Context context;
	int paramOpMenu=0;
	public void onCreate(Bundle savedData) {
		super.onCreate(savedData);
		this.setContentView(R.layout.hoja_ruta_list_ui);
		context = this;
		//**************PARAMETROS*******************
		Bundle bundle = getIntent().getExtras();
		if(bundle !=null){
			paramOpMenu = bundle.getInt("paramOpMenu");
			
		}
			
		//*******************Boton Regresar***************************
		ImageView btnAtras = (ImageView)findViewById(R.id.imbHrAtras);
		btnAtras.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent menu = new Intent( cl_hoja_ruta_lista.this,cl_menu_main.class);
				startActivity(menu);
				finish();
			}
		});
		//****************************************************************
		
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
		
		ListView lvHojaRuta=(ListView)findViewById(R.id.lvHojaRuta);
		cl_hoja_ruta_adapter adapter = getHRAdapter(paramOpMenu);
		lvHojaRuta.setAdapter(adapter);
		
				
	}
	
	public cl_hoja_ruta_adapter getHRAdapter(int paramOpMenu){
		//context = this;		
		db_distribuidor_assets db=new db_distribuidor_assets(cl_hoja_ruta_lista.this);
		ArrayList<cl_hoja_ruta> lista= new ArrayList<cl_hoja_ruta>();
		if(paramOpMenu==cl_constantes.OpMnuIniciarHR){
			lista=db.getHojaRutaListar();
		}else if(paramOpMenu==cl_constantes.OpMnuDestinos){
			lista=db.getHojaRutaIniciadaListar();
		}
		
		cl_hoja_ruta_adapter adapter=new cl_hoja_ruta_adapter(cl_hoja_ruta_lista.this,lista,paramOpMenu);
		return adapter;
	}
	
	

}
