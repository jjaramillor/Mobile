package app.main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import app.database.cl_user_entity;
import app.database.db_distribuidor_assets;
import android.app.AlertDialog;
import android.app.ProgressDialog;
//import app.model.cl_local;
import app.utils.cl_util;

public class cl_login extends Activity {
	Context context;
	ProgressDialog prgDialog;
	int tipoLogin = 0;
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.login_ui);
		
		//estableciendo la conexion actual
		context = this;
		
		//progress
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Iniciando sesión...");
		prgDialog.setCancelable(false);
		
		//Imagenes
		ImageView imgTrans = (ImageView)findViewById(R.id.imgTrans);
		ImageView imgSup = (ImageView)findViewById(R.id.imgSup);
		
		//Seteando tags a las imagenes
		imgTrans.setTag(R.drawable.login_transp_w);
		imgSup.setTag(R.drawable.login_sup_r);
		
		Button btn_login = (Button)findViewById(R.id.imbLoginCia);
		
		//***************EVENTOS****************//
		//BOTON LOGIN 
		btn_login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){							
				//validar los datos del formulario				
				ValidaDatosFormulario();					
			}
		});

		//IMAGENES 
		//imgTrans
		imgTrans.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				ImageView imgTrans = (ImageView)findViewById(R.id.imgTrans);
				ImageView imgSup = (ImageView)findViewById(R.id.imgSup);
				
				Integer src=(Integer)imgTrans.getTag();
				
				if(src.equals(R.drawable.login_transp_r)){	
					imgTrans.setImageResource(R.drawable.login_transp_w);
					imgTrans.setTag(R.drawable.login_transp_w);					
					imgSup.setImageResource(R.drawable.login_sup_r);
					imgSup.setTag(R.drawable.login_sup_r);		
					tipoLogin = 0;
				}	
			}
		});
		
		//imgSup
		imgSup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
					ImageView imgTrans = (ImageView)findViewById(R.id.imgTrans);
					ImageView imgSup = (ImageView)findViewById(R.id.imgSup);					
					
					Integer src=(Integer)imgSup.getTag();					
					if(src.equals(R.drawable.login_sup_r)){
						imgTrans.setImageResource(R.drawable.login_transp_r);
						imgTrans.setTag(R.drawable.login_transp_r);
						
						imgSup.setImageResource(R.drawable.login_sup_w);
						imgSup.setTag(R.drawable.login_sup_w);
						tipoLogin = 1;
					}
				}
			});
		//*************************************//
	}
	
	private void ValidaDatosFormulario(){
		
		EditText txtNombre =(EditText)findViewById(R.id.dfUsuario);
		EditText txtContrasena =(EditText)findViewById(R.id.dfContrasena);
		
		final String sNombre = txtNombre.getText().toString().trim();
		final String sContrasena = txtContrasena.getText().toString().trim();
		
		if (sNombre.length()==0){
			cl_util.DisplayMessage(context, "Ingrese un usuario.\n", "L");
		}
		else if(sContrasena.length()==0){
			cl_util.DisplayMessage(context, "Ingrese la contraseña.\n", "L");
		}
		else{
			if(cl_util.isOnline(context)){
				
				if(tipoLogin == 0)
				{
					final db_distribuidor_assets db=new db_distribuidor_assets(context);
					cl_user_entity user=db.getUserEntity();
					
					if(user!=null){
						if(user.getTraNomCorto()!=sNombre){
							
							//*******Se crea mensaje a mostrar**************************
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setTitle("Confirmar")
								.setMessage("Existe información de otro usuario que no le pertenece. \n Si Ud. desea continuar se eliminará toda la información del usuario anterior?")
								.setCancelable(false)
								.setIcon(R.drawable.ic_launcher)
								.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface dialog, int id){
										
										prgDialog.show();	
										new autenticar().execute(sNombre,sContrasena,"1");
										
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
						}else{
							prgDialog.show();	
							new autenticar().execute(sNombre,sContrasena,"0");
						}
						
					}else{
						prgDialog.show();	
						new autenticar().execute(sNombre,sContrasena,"0");
					}
				}
				else if(tipoLogin == 1){
					prgDialog.show();	
					new autenticarSupervisor().execute(sNombre,sContrasena);
				}
			}
			else{
				
				cl_util.DisplayMessage(context,getString(R.string.msjNoInternet), "L");
			}
		}
		

	}
	
	private void RedirectMenuMain(String mensaje){	
		
		if (mensaje.length()>0){
			cl_util.DisplayMessage(context, mensaje, "L");
		} else {
			//leyendo control desde el formulario
			String sUsuario = ((EditText)findViewById(R.id.dfUsuario)).getText().toString().trim();
			String sContrasena = ((EditText)findViewById(R.id.dfContrasena)).getText().toString().trim();
			
			//registrando en preferencias
			
			SharedPreferences fSettings = getSharedPreferences(app.utils.cl_constantes.FILE_PREFERENCES, MODE_PRIVATE);
			SharedPreferences.Editor editor = fSettings.edit();
			editor.putString("username", sUsuario);
			editor.putString("userpass", sContrasena);
			editor.commit();
			
			
			//Redirect
			Intent myIntent;
			myIntent = new Intent(cl_login.this, cl_menu_main.class);
			startActivity(myIntent);
			//cl_login.this.finish(); se realiza de las dos formas
			finish();
		}
		
	}
	
	private class autenticar extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String[] params) {
	    	
	    	String mensaje="";
	    	String eliminarDatos=params[2];
	    	
			HttpClient Client = new DefaultHttpClient();
			
			String host = cl_util.ObtenerHost(context);
			
			HttpGet autenticar =	new HttpGet(host + "distribuidor/usuario/autenticar?usuario="+ params[0]+"&contrasenia="+params[1]);
			autenticar.setHeader("content-type","application/json");			
			
			try{
				HttpResponse resp = Client.execute(autenticar);
				String respStr =EntityUtils.toString(resp.getEntity());
				
				
				if(!respStr.equals("null")){
					
					Boolean deleteExito=true;
					
					if(eliminarDatos.equals("1")){
						db_distribuidor_assets db= new  db_distribuidor_assets(context);
						deleteExito=db.deleteDestinosHojaRutaSync();
						
						if(deleteExito){
							deleteExito=db.deleteHojaRutaDetalleSync();
							
							if(deleteExito){
								deleteExito=db.deleteHojaRutaSync();
								
								if(deleteExito){
									deleteExito=db.deletePedidosSync();
								}
							}
						}
					}
					
					if(deleteExito){
						JSONObject respJs=new JSONObject(respStr);
						cl_user_entity userEntity=new cl_user_entity();
						
						userEntity.setVehCodigo(respJs.getInt("vehCodigo"));
						userEntity.setTraNombre(respJs.getString("traNombre"));
						userEntity.setTraApePat(respJs.getString("traApePat"));
						userEntity.setTraNumDoc(respJs.getString("traNumDoc"));
						userEntity.setVehPlaca(respJs.getString("vehPlaca"));
						userEntity.setTraCodigo(respJs.getInt("traCodigo"));
						userEntity.setTraNomCorto(respJs.getString("traNomCorto"));
						userEntity.setUsrCodMod(respJs.getString("usrCodMod"));
						
						
						db_distribuidor_assets db = new db_distribuidor_assets(context);	
						db.deleteUserEntity();//temp:esto debe usarse en cerrar session
						if(!db.insertUserEntity(userEntity)){
							mensaje = "Ocurrió un error al intentar guardar la configuracion de usuario.\n";
						}
						
					}else{
						mensaje = "Error al intentar limpiar los datos anteriores.\n";
					}
					
					
				}else{					
					mensaje = "El nombre de usuario o contraseña son incorrectos.\n";
				}	
			}
			catch (Exception e) {
				// TODO: handle exception				
				//Log.e("ServicioRest","Error con el servicio de autenticación!",e);
				mensaje = "Error con el servicio de autenticación.\n";				
			}
	        return mensaje;
	    }

	    @Override
	    protected void onPostExecute(String message) {
	    	//_mensaje=message;
	    	prgDialog.hide();
	    	RedirectMenuMain(message);
	    }
	}
	
	private class autenticarSupervisor extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			String mensaje="";
	    		    	
			HttpClient Client = new DefaultHttpClient();			
			String host = cl_util.ObtenerHost(context);
			
			HttpGet autenticar =	new HttpGet(host + "distribuidor/usuario/AutenticarSup?usuario="+ params[0]+"&contrasenia="+params[1]);
			autenticar.setHeader("content-type","application/json");			
			
			try{
				HttpResponse resp = Client.execute(autenticar);
				String respStr =EntityUtils.toString(resp.getEntity());
								
				if(!respStr.equals("null")){
							
					
				}else{					
					mensaje = "El nombre de usuario o contraseña son incorrectos.\n";
				}	
			}
			catch (Exception e) {				
				mensaje = "Error con el servicio de autenticación.\n";				
			}
	        return mensaje;
		}
		
		 @Override
	    protected void onPostExecute(String message) {
			 prgDialog.hide();
	    	RedirectMenuSupMain(message);
	    }
		
	}
	
	private void RedirectMenuSupMain(String mensaje){	
		
		if (mensaje.length()>0){
			cl_util.DisplayMessage(context, mensaje, "L");
		} else {		
			
			//Redirect
			Intent myIntent;
			myIntent = new Intent(cl_login.this, cl_supervisor_menu_main.class);
			startActivity(myIntent);			
			finish();
		}
		
	}
}


