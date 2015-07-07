package app.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sft.lib.Expandable.ActionSlideExpandableListView;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.database.cl_cliente;
import app.database.cl_pedido;
import app.database.cl_pedido_data_holder;
import app.database.db_distribuidor_assets;
import app.utils.cl_util;

public class cl_pedido_lista extends ListActivity {

	List<cl_pedido> Pedidos = new ArrayList<cl_pedido>();
	Context context;
	// *******Parametros de Navegacion*******
	String paramHrDesc = "";
	int paramHrId = 0;
	String paramcliNombre = "";
	int paramCliId = 0;
	int paramDesCodigo = 0;
	int paramPedIdDireccion = 0;
	int esHojaRutaUnica = 0;
	int paramOpMenu = 0;
	
		

	// **************************************
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedido_list_ui);
		context = this;

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			paramHrDesc = bundle.getString("paramHrDesc");
			paramHrId = bundle.getInt("paramHrId");
			paramcliNombre = bundle.getString("paramcliNombre");
			paramCliId = bundle.getInt("paramCliId");
			paramDesCodigo = bundle.getInt("paramDesCodigo");
			paramPedIdDireccion = bundle.getInt("paramPedIdDireccion");
			esHojaRutaUnica = bundle.getInt("esHojaRutaUnica");
			paramOpMenu = bundle.getInt("paramOpMenu");

			TextView text = (TextView) findViewById(R.id.txtDescripcion);
			text.setText(paramcliNombre);
		}

		//*******************Boton Regresar***************************
		ImageView btnHome = (ImageView)findViewById(R.id.imvHome);
		btnHome.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent menu = new Intent( context,cl_menu_main.class);
				startActivity(menu);
				finish();
			}
		});
		//****************************************************************
				
		ImageView btnAtras = (ImageView) findViewById(R.id.imbAtras);
		btnAtras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("paramHrDesc", paramHrDesc);
				bundle.putInt("paramHrId", paramHrId);
				bundle.putString("paramcliNombre", paramcliNombre);
				bundle.putInt("paramCliId", paramCliId);
				bundle.putInt("paramDesCodigo", paramDesCodigo);
				bundle.putInt("paramPedIdDireccion", paramPedIdDireccion);
				bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
				bundle.putInt("paramOpMenu", paramOpMenu);
				Intent menu = new Intent(cl_pedido_lista.this,cl_cliente_lista.class);
				menu.putExtras(bundle);
				startActivity(menu);
				finish();
			}
		});

		final ListView list = (ListView) findViewById(android.R.id.list);
		cl_pedido_adapter adapter = getPedAdapter(paramCliId,paramDesCodigo,paramPedIdDireccion);
		list.setAdapter(adapter);

		//LIQUIDAR
		Button btnLiquidar = (Button) findViewById(R.id.btnLiquidar);
		btnLiquidar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				bundle.putString("paramHrDesc", paramHrDesc);
				bundle.putInt("paramHrId", paramHrId);
				bundle.putString("paramcliNombre", paramcliNombre);
				bundle.putInt("paramCliId", paramCliId);
				bundle.putInt("paramDesCodigo", paramDesCodigo);
				bundle.putInt("paramPedIdDireccion", paramPedIdDireccion);
				bundle.putInt("esHojaRutaUnica", esHojaRutaUnica);
				bundle.putInt("paramOpMenu", paramOpMenu);
				bundle.putInt("paramLiquidarDirecto",0);
				
				cl_pedido_adapter adapter = (cl_pedido_adapter) list.getAdapter();
				ArrayList<cl_pedido> lista = new ArrayList<cl_pedido>();
				
				for (int i = 0; i < adapter.getCount(); i++) {
					cl_pedido pedido = (cl_pedido) adapter.getItem(i);
					//CheckBox chk = (CheckBox) adapter.getView(i, null, null).findViewById(R.id.chbPedido);
					
					if (pedido.getPedEstado()!=null &&pedido.getPedEstado()!="" && pedido.isPedActualizar()) {
						lista.add(pedido);
					}

				}

				// bundle.putSerializable("listaPedidos", lista);
				if(lista.size()>0){
					cl_pedido_data_holder.getInstance().setData(lista);
					Intent menu = new Intent(cl_pedido_lista.this,cl_liquidar.class);
					menu.putExtras(bundle);
					//menu.putExtra("listaPedidos", new cl_pedido_data_holder(lista));
					startActivity(menu);
					//startActivityForResult(menu, 0);
					finish();
				}else{
					cl_util.DisplayMessage(context, "Debe seleccionar al menos un pedido", "L");
				}
				
			}
		});

	}
	
	public cl_pedido_adapter getPedAdapter(int paramCliId, int desCodigo, int pedIdDireccion ){
		//context = this;		
		db_distribuidor_assets db=new db_distribuidor_assets(cl_pedido_lista.this);
		List<cl_pedido> lista=db.getAllPedidos(paramCliId, desCodigo, pedIdDireccion );
		cl_pedido_adapter adapter=new cl_pedido_adapter(cl_pedido_lista.this,lista);
		return adapter;
	}

}
