package app.main;

import java.util.ArrayList;
import java.util.List;

import sft.lib.Expandable.ActionSlideExpandableListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import app.database.cl_cliente;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;

//import sft.lib.Expandable.ActionSlideExpandableListView;


public class cl_cliente_lista extends Activity {
	Context context;
	String paramHrDesc = "";
	int paramHrId = 0;
	String paramcliNombre = "";
	int paramCliId = 0;
	int paramDesCodigo = 0;
	int paramPedIdDireccion = 0;
	int esHojaRutaUnica=0;
	int paramOpMenu = 0;
	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		// set the content view for this activity, check the content view xml file
		// to see how it refers to the ActionSlideExpandableListView view.
		this.setContentView(R.layout.single_expandable_list);
		//context = this;
		
		Bundle bundle = getIntent().getExtras();
		if(bundle !=null){
			paramHrDesc = bundle.getString("paramHrDesc");
			paramHrId = bundle.getInt("paramHrId");			
			paramcliNombre = bundle.getString("paramcliNombre");
			paramCliId = bundle.getInt("paramCliId");
			paramDesCodigo = bundle.getInt("paramDesCodigo");
			paramPedIdDireccion = bundle.getInt("paramPedIdDireccion");			
			esHojaRutaUnica = bundle.getInt("esHojaRutaUnica");
			paramOpMenu = bundle.getInt("paramOpMenu");
			TextView hr = (TextView)findViewById(R.id.txtDescripcion);
			hr.setText(paramHrDesc);
		}
		
		//*******************Boton Regresar***************************
		ImageView btnHome = (ImageView)findViewById(R.id.imvHome);
		btnHome.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent menu = new Intent( cl_cliente_lista.this,cl_menu_main.class);
				startActivity(menu);
				finish();
			}
		});
		//****************************************************************
		
		ImageView btnAtras = (ImageView)findViewById(R.id.imbAtras);
		btnAtras.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(esHojaRutaUnica==1){
					Intent menu = new Intent( cl_cliente_lista.this,cl_menu_main.class);
					startActivity(menu);
					finish();
				}else{
					
					Bundle bundle = getIntent().getExtras();					
					bundle.putInt("paramOpMenu", paramOpMenu);	
					
					Intent menu3 = new Intent(cl_cliente_lista.this,cl_hoja_ruta_lista.class);
					menu3.putExtras(bundle);
					startActivity(menu3);
					finish();
				}
				
			}
		});
		
		
		ActionSlideExpandableListView list = (ActionSlideExpandableListView)this.findViewById(R.id.list);
		cl_cliente_adapter adapter=getCliAdapter(paramHrId);
		list.setAdapter(adapter);
		
	}

	public cl_cliente_adapter getCliAdapter(int hrId){
		//context = this;		
		db_distribuidor_assets db=new db_distribuidor_assets(cl_cliente_lista.this);
		ArrayList<cl_cliente> lista=db.getClientesByHojaRuta(hrId);
		cl_cliente_adapter adapter=new cl_cliente_adapter(cl_cliente_lista.this,lista,paramHrDesc,paramHrId,esHojaRutaUnica,paramOpMenu);
		return adapter;
	}


}
