package app.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_ui);
		
		final TextView lblInfo = (TextView)findViewById(R.id.txt_home);
		lblInfo.setText(Html.fromHtml(getString(R.string.txt_home)));
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				
				Intent myIntent;
				
				if (PreferenciasValidas()){
					//mostrar ficha de login
					//Intent myIntent = new Intent( Splash.this, cl_menu_main.class);
					//Splash.this.startActivity(myIntent);
					//Splash.this.finish();		
					myIntent = new Intent( Splash.this, cl_menu_main.class);
				} else {
					//mostrar ficha de login
					//Intent myIntent = new Intent( Splash.this, cl_login.class);
					//Splash.this.startActivity(myIntent);
					//Splash.this.finish();		
					myIntent = new Intent( Splash.this, cl_login.class);

				}
				Splash.this.startActivity(myIntent);
				Splash.this.finish();		
				
			}
		}, 4000);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	public boolean PreferenciasValidas(){
		SharedPreferences fSettings = getSharedPreferences(app.utils.cl_constantes.FILE_PREFERENCES, MODE_PRIVATE);
		
		String sUserName = fSettings.getString(app.utils.cl_constantes.SHARE_PREF_USERNAME, "");
		//String sUserPass = fSettings.getString("userpass", "");
		
		return !(sUserName.length()==0);
	}
}
