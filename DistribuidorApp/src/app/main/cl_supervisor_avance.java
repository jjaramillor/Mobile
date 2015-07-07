package app.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import app.database.cl_avance;
import app.database.db_distribuidor_assets;

public class cl_supervisor_avance extends Activity {
	ProgressDialog prgDialog;
	private Context context;
	List<cl_avance> lista;
	ListView gv;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supervisor_avance_ui);
		context = this;
		prgDialog = new ProgressDialog(this);		
		//*******************Boton Regresar***************************
				ImageView btnHome = (ImageView)findViewById(R.id.imvHome);
				btnHome.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent menu = new Intent( context,cl_supervisor_menu_main.class);
						startActivity(menu);
						finish();
					}
				});				

				ImageView btnAtras = (ImageView) findViewById(R.id.imbAtras);
				btnAtras.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {	
						Intent menu = new Intent(context, cl_supervisor_menu_main.class);						
						startActivity(menu);
						finish();
					}
				});
		
		//------------------------------------------------------------------------		
		gv = (ListView) findViewById(R.id.lstGridAvance);				
		String fecha = new SimpleDateFormat("yyyyMMdd").format(new Date());		
		String[] args = {fecha,"0",""};
		
		prgDialog.setMessage("Obteniendo información, por favor espere...");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new AvanceTask().execute(args);			
	}
	
	private void setupAdapter(){
		
		cl_avance_adapter adapter = new cl_avance_adapter(context, lista);
		gv.setAdapter(adapter);			
	}
		
	private class AvanceTask extends AsyncTask<String, Void, List<cl_avance>> {

		@Override
		protected List<cl_avance> doInBackground(String[] params) {
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			return db.getAvances(params[0],params[1],params[2]);
		}

		@Override
		protected void onPostExecute(List<cl_avance> result) {
			lista = result;	
			setupAdapter();
			prgDialog.hide();
		}
	}
}
