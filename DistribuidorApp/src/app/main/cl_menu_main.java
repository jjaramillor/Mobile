package app.main;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import app.database.cl_hoja_ruta;
import app.database.cl_general_param;
import app.database.cl_user_entity;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;
import app.utils.cl_util;

public class cl_menu_main extends Activity {
	Context context;
	ProgressDialog prgDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_main_ui);
		context = this;
		prgDialog = new ProgressDialog(this);
		ImageView imvSincronizar = (ImageView) findViewById(R.id.imvMSincronizar);
		ImageView imvMIniciarHR = (ImageView) findViewById(R.id.imvMIniciarHR);
		ImageView imvMPendientes = (ImageView) findViewById(R.id.imvMPendientes);
		ImageView imvMDestinos = (ImageView) findViewById(R.id.imvMDestinos);
		ImageView imvMConsultas = (ImageView) findViewById(R.id.imvMConsultas);
		ImageView imvMSalir = (ImageView) findViewById(R.id.imvMSalir);
		TextView tvUsuario = (TextView) findViewById(R.id.tvUsuario);
		
		//Obtener Datos de Usuario
		String usrCodMod=cl_general_param.getInstance().getUsrCodMod();
		tvUsuario.setText(cl_general_param.getInstance().getUsrNombreCompleto());
		if(usrCodMod==null ||usrCodMod.equals("")){
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			cl_user_entity user = db.getUserEntity();
			cl_general_param.getInstance().setUsrCodMod(user.getUsrCodMod());
			
			String usrNombreCompleto = user.getTraApePat() + " " + user.getTraNombre();
			cl_general_param.getInstance().setUsrNombreCompleto(usrNombreCompleto);
						
			tvUsuario.setText(usrNombreCompleto);
		}
	
		// SINCRONIZAR
		imvSincronizar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!cl_util.isOnline(context)){
					cl_util.DisplayMessage(cl_menu_main.this, getString(R.string.msjNoInternet), "L");
					return;
				}
				
				
				// *******Se crea mensaje a mostrar**************************
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Confirmar")
						.setMessage("Esta acción reemplazará los datos locales de la Hoja de Ruta, si tiene datos pendientes envíelos y luego continue con esta acción.\n"
										+ "¿Desea continuar?")
						.setCancelable(false)
						.setIcon(R.drawable.ic_launcher)
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										prgDialog.setMessage("Se ha iniciado la sincronización, por favor espere...");
										prgDialog.setCancelable(false);
										prgDialog.show();
										String mensaje = Sincronizar();
										if (mensaje.length() > 0)
											cl_util.DisplayMessage(cl_menu_main.this, mensaje, "L");
									}
								})
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,	int id) {
										dialog.dismiss();
									}
								});

				final AlertDialog alert = builder.create();
				// alert.show();
				// ************************************************************

				// verificar si e existen item a sincronizar
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				int itemsPorSincronizar = db.verificarItemsPorSincronizar();
				if (itemsPorSincronizar > 0) {

					// *******Se crea mensaje a
					// mostrar**************************
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							context);
					builder2.setTitle("Confirmar")
							.setMessage(
									"Ud.aún tiene "
											+ itemsPorSincronizar
											+ " pedidos pendientes por enviar, si continúa usted perderá los cambios.\n"
											+ "¿Desea continuar?")
							.setCancelable(false)
							.setIcon(R.drawable.ic_launcher)
							.setPositiveButton("Aceptar",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											// cl_util.DisplayMessage(cl_menu_main.this,"Los datos fueron sincronizados","L");
											alert.show();
										}
									})
							.setNegativeButton("Cancelar",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
										}
									});

					AlertDialog alert2 = builder2.create();
					alert2.show();
					// ************************************************************

				} else {
					alert.show();
				}
			}
		});

		// INICIAR HR
		imvMIniciarHR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				db_distribuidor_assets db = new db_distribuidor_assets(context);
				ArrayList<cl_hoja_ruta> lista = db.getHojaRutaListar();

				if (lista.size() > 0) {

					Bundle bundle = new Bundle();
					bundle.putInt("paramOpMenu", cl_constantes.OpMnuIniciarHR);

					Intent menu3 = new Intent(cl_menu_main.this, cl_hoja_ruta_lista.class);
					menu3.putExtras(bundle);
					startActivity(menu3);
					finish();

				} else {
					cl_util.DisplayMessage(cl_menu_main.this,
							"Ud. no cuenta con hojas de rutas asignadas", "L");
				}
			}
		});

		//PENDIENTES
		imvMPendientes.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				ArrayList<cl_hoja_ruta> lista = db.getHojaRutaIniciadaListar();
				if (lista.size() > 0) {
					 lista = db.getHojaRutaIniciadaListarSync();
					if(lista.size() > 0){						
						Intent menuPendientes = new Intent(cl_menu_main.this, cl_pendiente_lista.class);			
						startActivity(menuPendientes);
						finish();
					}
					else{
						cl_util.DisplayMessage(cl_menu_main.this,"No existe hojas de ruta liquidadas", "L");
					}
				} else {
					cl_util.DisplayMessage(cl_menu_main.this,
							"Ud. no cuenta con hojas de rutas asignadas", "L");
				}
			}
		});
		
		// DESTINOS
		imvMDestinos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				db_distribuidor_assets db = new db_distribuidor_assets(context);
				ArrayList<cl_hoja_ruta> lista = db.getHojaRutaIniciadaListar();

				if (lista.size() == 1) {
					cl_hoja_ruta hr = lista.get(0);

					Bundle bundle = new Bundle();
					bundle.putString("paramHrDesc", "HR" + hr.getHrNumeroHoja());
					bundle.putInt("paramHrId", hr.getHrCodigo());
					bundle.putInt("esHojaRutaUnica", 1);
					bundle.putInt("paramOpMenu", cl_constantes.OpMnuDestinos);
					Intent menu3 = new Intent(context, cl_cliente_lista.class);
					menu3.putExtras(bundle);
					startActivity(menu3);
					finish();

				} else if (lista.size() > 1) {

					Bundle bundle = new Bundle();
					bundle.putInt("paramOpMenu", cl_constantes.OpMnuDestinos);

					Intent menu3 = new Intent(cl_menu_main.this, cl_hoja_ruta_lista.class);
					menu3.putExtras(bundle);
					startActivity(menu3);
					finish();
				} else {
					cl_util.DisplayMessage(cl_menu_main.this,
							"Ud. no cuenta con hojas de rutas asignadas", "L");
				}

			}
		});

		// CONSULTA
		imvMConsultas.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {					
				db_distribuidor_assets db = new db_distribuidor_assets(context);
				ArrayList<cl_hoja_ruta> lista = db.getHojaRutaListar();
				if (lista.size() > 0) {					
					Intent menu3 = new Intent(cl_menu_main.this, cl_consultas.class);					
					startActivity(menu3);
					finish();
				} else {
					cl_util.DisplayMessage(cl_menu_main.this,
							"Ud. no cuenta con hojas de rutas asignadas", "L");
				}				
			}
		});
				
		// SALIR
		imvMSalir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cl_util.ExitApp(cl_menu_main.this);
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_option, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnuOptCerrarSesion:
			// startActivity(new Intent(this, About.class));
			LogoutApp(cl_menu_main.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// cerrar sesion
	public void LogoutApp(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Cerrar Sesión")
				.setMessage("Está seguro de cerrar sesión y salir?")
				.setCancelable(false)
				.setIcon(R.drawable.ic_launcher)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						// eliminando en preferencias
						DeletePreferenciasLogin();

						android.os.Process.killProcess(android.os.Process
								.myPid());
						// System.exit(0);

					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	public void DeletePreferenciasLogin() {
		SharedPreferences fSettings = getSharedPreferences(
				app.utils.cl_constantes.FILE_PREFERENCES, MODE_PRIVATE);
		String usr = app.utils.cl_constantes.SHARE_PREF_USERNAME;
		String pwd = app.utils.cl_constantes.SHARE_PREF_USERPASS;

		if (fSettings.contains(usr) && fSettings.contains(pwd)) {
			SharedPreferences.Editor editor = fSettings.edit();
			editor.remove(usr);
			editor.remove(pwd);
			editor.commit();
		}
	}

	// sincronizar
	public String Sincronizar() {

		// Eliminar Registro de BD App
		Boolean success = true;
		String mensaje = "";
		db_distribuidor_assets db = new db_distribuidor_assets(context);

		if (!db.deleteHojaRutaSync()) {
			return "No se pudo realizar la sincronización.\n Ocurrió un problema al limpiar las hojas de rutas ";
		}
		if (!db.deleteHojaRutaDetalleSync()) {
			return "No se pudo realizar la sincronización.\n Ocurrió un problema al limpiar el detalle de las hojas de rutas ";
		}
		if (!db.deleteDestinosHojaRutaSync()) {
			return "No se pudo realizar la sincronización.\n Ocurrió un problema al limpiar los Destinos";
		}
		if (!db.deletePedidosSync()) {
			return "No se pudo realizar la sincronización.\n Ocurrió un problema al limpiar los pedidos";
		}

		/*
		 * if(!db.getHojaRutaDeServicioSync()){ return
		 * "No se pudo realizar la sincronización.\n Ocurrió al registrar los datos del servicio"
		 * ; }
		 */
		new SincronizarTask().execute();

		return "";
	}

	public void SincronizarFinaly(Boolean success) {

		if (success) {
			cl_util.DisplayMessage(context, "Se ha sincronizado correctamente",
					"L");

		} else {
			// cl_util.DisplayMessage(context, "No se pudo realizar la sincronización.\n Ocurrió al registrar los datos del servicio", "L");
			cl_util.DisplayMessage(context, "No se pudo realizar la sincronización.\n No existe información", "L");
		}
	}

	private class SincronizarTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String[] params) {
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			return db.getHojaRutaDeServicioSync();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			SincronizarFinaly(result);
			prgDialog.hide();
		}
	}
}
