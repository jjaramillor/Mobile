package app.services;

import android.view.View;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import app.database.cl_hoja_ruta;
import app.main.R;

public class cl_service_main extends ActionBarActivity {

	final private cl_hoja_ruta ruta = new cl_hoja_ruta();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pendiente_list_ui);
		
		new cl_service_HojaRuta().execute(ruta);
	}

	
}
