package app.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.adapters.*;
import app.database.cl_estadistica;
import app.database.db_distribuidor_assets;
import app.utils.cl_util;

public class cl_supervisor_estadistica_detalle extends Activity {
	TableFixHeaders tableFixHeaders = null;
	ProgressDialog prgDialog;
	private Context context;
	List<cl_estadistica> lista;
	String[][] array;
	
	String paramCodMotivo;
	String paramMotivo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supervisor_estadistica_detalle_ui);
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
				Intent menu = new Intent(context, cl_supervisor_estadistica.class);						
				startActivity(menu);
				finish();
			}
		});
		
		TextView txtTituloHeader = (TextView) findViewById(R.id.txtTituloHeader);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			paramCodMotivo = bundle.getString("paramCodMotivo");	
			paramMotivo = bundle.getString("paramMotivo");
			txtTituloHeader.setText(paramMotivo);
		}
		else{
			cl_util.DisplayMessage(context, "Hay un problema en la app, comuniquese con el administrador", "L");
			return;
		}

		//------------------------------------------------------------------------			
		tableFixHeaders = (TableFixHeaders) findViewById(R.id.gvEstadistica_detalle);
				
		String fecha = new SimpleDateFormat("yyyyMMdd").format(new Date());	
		
		String[] args = {fecha,"1",paramCodMotivo};
		
		prgDialog.setMessage("Obteniendo información, por favor espere...");
		prgDialog.setCancelable(false);
		prgDialog.show();
		new EstadisticaTask().execute(args);
		
	}
	
		
	@SuppressWarnings("null")
	private void setupAdapter(){		
		
		int _r = lista.size();
		
		array = new String[_r][14] ;
		
		if(lista != null){
			int row = 0;
			for (cl_estadistica item: lista){
				
				
					if(row == 0){
						array[0][0] = "cliente";
						array[0][1] = "Planeado";
						array[0][2] = "Efectuado";
						array[0][3] = "HR";
						array[0][4] = "Documento";
						//array[0][5] = "cliente";
						array[0][5] = "Pedido";				
						array[0][6] = "Destinatario";
						array[0][7] = "Esta liquidado";
						array[0][8] = "Partida";
						array[0][9] = "Llegada";
						array[0][10] = "Descargo";
						array[0][11] = "Liquidado";
						array[0][12] = "Cod. Motivo";
						array[0][13] = "Motivo";
					}
					else{
						array[row][0] = item.getCliente();
						array[row][1] = item.getPlaneado();
						array[row][2] = item.getEfectuado();
						array[row][3] = item.getHR();
						array[row][4] = item.getDocumento();
						//array[row][5] = item.getCliente();
						array[row][5] = item.getPedido();
						array[row][6] = item.getDestinatario();
						array[row][7] = String.valueOf(item.getFlgLiquida());
						array[row][8] = item.getPartida();
						array[row][9] = item.getLlegada();
						array[row][10] = item.getDescargo();
						array[row][11] = item.getLiquidado();						
						array[row][12] = item.getCodMotivo();
						array[row][13] = item.getMotivo();
					}
				
				row +=1;
				}
				
			
			MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(context, array);
			tableFixHeaders.setAdapter(matrixTableAdapter);
												
			//tableFixHeaders.setAdapter(new MyAdapter(this));
		}
	}
		
	private class EstadisticaTask extends AsyncTask<String, Void, List<cl_estadistica>> {

		@Override
		protected List<cl_estadistica> doInBackground(String[] params) {
			db_distribuidor_assets db = new db_distribuidor_assets(context);
			return db.getEstadisticas(params[0],params[1],params[2]);
		}

		@Override
		protected void onPostExecute(List<cl_estadistica> result) {
			lista = result;	
			setupAdapter();
			prgDialog.hide();
		}
	}
	
	
	//Adaptador
	private class MyAdapter extends BaseTableAdapter {

		private final int width;
		private final int height;

		public MyAdapter(Context context) {			

			Resources resources = context.getResources();

			width = resources.getDimensionPixelSize(R.dimen.table_width);
			height = resources.getDimensionPixelSize(R.dimen.table_height);
		}

		@Override
		public int getRowCount() {
			return array.length - 1;
		}

		@Override
		public int getColumnCount() {
			return array[0].length - 1;
		}

		@Override
		public int getWidth(int column) {
			return width;
		}

		@Override
		public int getHeight(int row) {
			return height;
		}

		

		@Override
		public int getItemViewType(int row, int column) {
			if (row < 0) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(int row, int column, View convertView,	ViewGroup parent) {
			final View view;
			
			if (row < 0 || column < 0) return convertView;
			
			switch (getItemViewType(row, column)) {
				case 0:
					view = getLayoutInflater().inflate(R.layout.item_table1_header, parent, false);
					((TextView) view.findViewById(android.R.id.text1)).setText(array[0][column]);
				break;
				case 1:
					view =  getLayoutInflater().inflate(R.layout.item_table1, parent, false);
					if (row < 0) row = 0;
					if (column < 0) column = 0;
					((TextView) view.findViewById(android.R.id.text1)).setText(array[row][column]);
				break;				
				default:
					throw new RuntimeException("wtf?");
			}			
			return view;
		}
	}

}
