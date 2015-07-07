package app.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;
import app.utils.cl_util;

public class cl_supervisor_menu_main extends Activity {

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.supervisor_menu_main_ui);
		context = this;
		
		ImageView imvMSupAvance = (ImageView) findViewById(R.id.imvMSupAvance);
		ImageView imvMSupEstadistica = (ImageView) findViewById(R.id.imvMSupEstadistica);
		
		imvMSupAvance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
					Bundle bundle = new Bundle();
					//bundle.putInt("paramOpMenu", cl_constantes.OpMnuIniciarHR);
					Intent menu3 = new Intent(context, cl_supervisor_avance.class);
					//menu3.putExtras(bundle);
					startActivity(menu3);
					finish();
			}
		});
		
		
		imvMSupEstadistica.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
					Bundle bundle = new Bundle();
					//bundle.putInt("paramOpMenu", cl_constantes.OpMnuIniciarHR);
					Intent menu3 = new Intent(context, cl_supervisor_estadistica.class);
					//menu3.putExtras(bundle);
					startActivity(menu3);
					finish();
			}
		});
		
		
	}
	
	
}
