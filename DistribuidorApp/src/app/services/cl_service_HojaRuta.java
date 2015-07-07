package app.services;

import android.os.AsyncTask;
import app.database.cl_hoja_ruta;
import app.database.cl_pedido;
import app.database.db_distribuidor_assets;
import app.main.cl_pendiente_adapter;

public class cl_service_HojaRuta extends AsyncTask<cl_hoja_ruta, Integer, ResultSync> {

	@Override
	protected ResultSync doInBackground(cl_hoja_ruta... hr) {
		
		//Llamar el metodo de sincronizacion
		//el metodo retornara un objeto ResultSync
		ResultSync response = new ResultSync();
		
		db_distribuidor_assets db = new db_distribuidor_assets(null);
		
		response.setResultHR(db.HojaRutaSync(hr[0]));
		
		
		return response;
	}	
	@Override
	protected void onPostExecute(ResultSync result) {
		
		super.onPostExecute(result);
	}		

}


