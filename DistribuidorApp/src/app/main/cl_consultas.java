package app.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import app.database.cl_consultas_entity;

public class cl_consultas extends Activity {

	Context context;
	
	public void onCreate(Bundle savedData) {
		super.onCreate(savedData);
		this.setContentView(R.layout.consultas_ui);
		context = this;
		
		
		// *******************Boton Regresar***************************
		ImageView btnAtras = (ImageView) findViewById(R.id.imbHrAtras);
		btnAtras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent menu = new Intent(cl_consultas.this, cl_menu_main.class);
				startActivity(menu);
				finish();
			}
		});
		// ****************************************************************
		
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
		ListView lv=(ListView)findViewById(R.id.lvConsultas);
		cl_consultas_adapter adapter = getAdapter();
		lv.setAdapter(adapter);

	}
	public cl_consultas_adapter getAdapter(){
		
		ArrayList<cl_consultas_entity> lista=new ArrayList<cl_consultas_entity>();
		
		cl_consultas_entity c1=new cl_consultas_entity();
		c1.setIdConsulta(1);
		c1.setDescripcion("Cumplimiento del Día");
		lista.add(c1);
		
		cl_consultas_entity c2=new cl_consultas_entity();
		c2.setIdConsulta(2);
		c2.setDescripcion("Estado de Entrega del Día");
		lista.add(c2);
		
		/*
		cl_consultas_entity c3=new cl_consultas_entity();
		c3.setIdConsulta(2);
		c3.setDescripcion("Tablero del Detalle del Día");
		lista.add(c3);
		*/
		
		
		cl_consultas_adapter adapter=new cl_consultas_adapter(cl_consultas.this,lista);
		return adapter;
	}

}
