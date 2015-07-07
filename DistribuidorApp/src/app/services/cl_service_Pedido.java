package app.services;

import android.os.AsyncTask;
import app.database.cl_pedido;
import app.database.db_distribuidor_assets;
import app.main.cl_liquidar;

public class cl_service_Pedido extends AsyncTask<cl_pedido, Integer, ResultSync> {

	@Override
	protected ResultSync doInBackground(cl_pedido... ped) {
		
		//Llamar el metodo de sincronizacion
		//el metodo retornara un objeto ResultSync
		ResultSync response = new ResultSync();
		
		db_distribuidor_assets db = new db_distribuidor_assets(null);
		response.setResultPedido(db.PedidoSync(ped[0].getHrCodigo(), ped[0].getCliCodigo()));
				
		return response;
	}	
	@Override
	protected void onPostExecute(ResultSync result) {
		
		super.onPostExecute(result);
	}		

}
