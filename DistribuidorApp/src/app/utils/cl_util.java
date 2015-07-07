package app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import app.main.R;

public class cl_util {

	//mostrar alerta
		public static void DisplayMessage(Context context, String sMensaje, String Tiempo){
			Toast tx;
			if (Tiempo.equals("L"))
				tx = Toast.makeText(context, sMensaje, Toast.LENGTH_LONG);
			else
				tx = Toast.makeText(context, sMensaje, Toast.LENGTH_SHORT);
			tx.show();				
		}
		
		//abandonar aplicacion
		public static void ExitApp(Context context ){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Abandonar aplicación")
					.setMessage("Está seguro de salir?")
					.setCancelable(false)
					.setIcon(R.drawable.ic_launcher)
					.setPositiveButton("Si", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int id){
							android.os.Process.killProcess(android.os.Process.myPid());
							//System.exit(0);
							
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					});
				builder.create().show();
		}
			
		public static String ObtenerHost(Context context)
		{
		 String host = context.getString(R.string.myhost);
			return host;
		}
		
		public static boolean isOnline(Context context) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}

			return false;
		}

		public  static boolean convertToBoolean(String value) {
		    boolean returnValue = false;
		    if ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || 
		        "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value))
		        returnValue = true;
		    return returnValue;
		}
		
		public static String getFechaActual() {
		    Date ahora = new Date();
		    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		    return formateador.format(ahora);
		}
}
