package app.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.inqbarna.tablefixheaders.TableFixHeaders;

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
import android.widget.TextView;
import app.adapters.MatrixTableAdapter;
import app.database.cl_avance;
import app.database.cl_estadistica;
import app.database.db_distribuidor_assets;
import app.utils.cl_util;


public class cl_supervisor_avance_detalle extends Activity {
	ProgressDialog prgDialog;
	private Context context;
	List<cl_avance> lista;
	TableFixHeaders tableFixHeaders = null;
	ListView gv;
	String paramTrans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.supervisor_avance_detalle_ui);
		setContentView(R.layout.supervisor_avance_detalle2_ui);
		
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
				Intent menu = new Intent(context, cl_supervisor_avance.class);						
				startActivity(menu);
				finish();
			}
		});
		
		TextView txtTituloHeader = (TextView) findViewById(R.id.txtTituloHeader);
		
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			paramTrans = bundle.getString("paramTrans");
			txtTituloHeader.setText(paramTrans);
		}
		else{
			cl_util.DisplayMessage(context, "Hay un problema en la app, comuniquese con el administrador", "L");
			return;
		}

		//------------------------------------------------------------------------	
		
		//gv = (ListView) findViewById(R.id.lstGridAvance1);
		tableFixHeaders = (TableFixHeaders) findViewById(R.id.gvAvance_detalle);
		String fecha = new SimpleDateFormat("yyyyMMdd").format(new Date());	
		
		String[] args = {fecha,"1",paramTrans};
		
		prgDialog.setMessage("Obteniendo información, por favor espere...");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new Avance1Task().execute(args);			
	}
	
	@SuppressWarnings("null")
	private void setupAdapterFinal(){		
		
		int _r = lista.size();
		
		String[][] array = new String[_r][16] ;
		
		if(lista != null){
			int row = 0;
			for (cl_avance item: lista){
				
				
					if(row == 0){
						array[row][0] = "Cliente";
						array[row][1] = "Documentos";
						array[row][2] = "Avance";
						array[row][3] = "Planeado";
						array[row][4] = "Efectuado";
						array[row][5] = "HR";
						array[row][6] = "Documento";
						array[row][7] = "Pedido";
						
						array[row][8] = "Destinatario";
						array[row][9] = "FlgLiquida";
						array[row][10] = "Partida";
						array[row][11] = "Llegada";
						array[row][12] = "Descargo";
						array[row][13] = "Liquidado";
						array[row][14] = "CodMotivo";
						array[row][15] = "Motivo";
					}
					else{
						array[row][0] = item.getCliente();
						array[row][1] = String.valueOf(item.getDocumentos());
						array[row][2] = String.valueOf(item.getAvance());
						array[row][3] = item.getPlaneado();
						array[row][4] = item.getEfectuado();
						array[row][5] = item.getHR();
						array[row][6] = item.getDocumento();
						array[row][7] = item.getPedido();
						
						array[row][8] = item.getDestinatario();
						array[row][9] = String.valueOf(item.getFlgLiquida());
						array[row][10] = item.getPartida();
						array[row][11] = item.getLlegada();
						array[row][12] = item.getDescargo();
						array[row][13] = item.getLiquidado();
						array[row][14] = item.getCodMotivo();
						array[row][15] = item.getMotivo();
					}
				
				row +=1;
				}
				
			
			MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(context, array);
			
			
			tableFixHeaders.setAdapter(matrixTableAdapter);		
		}
	}	
	
	private void setupAdapter(){
		
		cl_avance_detalle_adapter adapter = new cl_avance_detalle_adapter(context, lista);
		gv.setAdapter(adapter);	
					
	}
		
	private class Avance1Task extends AsyncTask<String, Void, List<cl_avance>> {

		@Override
		protected List<cl_avance> doInBackground(String[] params) {
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			return db.getAvances(params[0],params[1],params[2]);
		}

		@Override
		protected void onPostExecute(List<cl_avance> result) {
			lista = result;	
			//setupAdapter();
			setupAdapterFinal();
			prgDialog.hide();
		}
	}

}
