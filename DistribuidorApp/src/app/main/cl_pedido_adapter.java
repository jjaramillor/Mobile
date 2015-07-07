package app.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.database.cl_cliente;
import app.database.cl_general_param;
import app.database.cl_pedido;
import app.database.db_distribuidor_assets;
import app.utils.cl_util;

public class cl_pedido_adapter extends BaseAdapter {
	Context context;
	List<cl_pedido> _lista;
	private static LayoutInflater inflater = null;
	public HashMap<String,String> checked = new HashMap<String,String>();
	
	public cl_pedido_adapter(Context context, List<cl_pedido> list) {

		this.context = context;
		_lista = list;
	}

	@Override
	public int getCount() {

		return _lista.size();
	}

	@Override
	public Object getItem(int position) {

		return _lista.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final cl_pedido obj=_lista.get(position);
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.pedido_list_item_ui, null);

		}
		
		String pedEstado=obj.getPedEstado();

		TextView txtPedido = (TextView) convertView.findViewById(R.id.txtPedido);
		txtPedido.setText(obj.getPedNropedido()+"/"+obj.getPedGuiasGen());
		
		CheckBox chbPedido= (CheckBox) convertView.findViewById(R.id.chbPedido);
		Boolean estado=pedEstado!=null?true:false;
		chbPedido.setChecked(estado);
		chbPedido.setEnabled(!estado);
		
		ImageView imvCheck=(ImageView) convertView.findViewById(R.id.imvCheckPedido);
		
		if(pedEstado!=null){
			chbPedido.setVisibility(View.INVISIBLE);
			imvCheck.setVisibility(View.VISIBLE);
			if(pedEstado.equals("00008"))
			{
				//chbPedido.setBackgroundColor(Color.rgb(153, 204, 0));
				imvCheck.setImageResource(R.drawable.chk_total_32);
				
			}else if(pedEstado.equals("00009")){
				//chbPedido.setBackgroundColor(Color.rgb(255, 187, 51));
				imvCheck.setImageResource(R.drawable.chk_parcial_32);
			}
			else if(pedEstado.equals("00010")){
				//chbPedido.setBackgroundColor(Color.rgb(255, 68, 68));
				imvCheck.setImageResource(R.drawable.chk_no_entregado_32);
			}
		}
		
		obj.setPedActualizar(!estado);
		
		chbPedido.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String valor=isChecked?"1":"";
				obj.setPedEstado(valor);
				
			}
		});
		
		//******DETALLE********************
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				db_distribuidor_assets db= new db_distribuidor_assets(context);
				ArrayList<String> detalle=db.getPedidoDetalle(obj.getPedCodigo());
				
				//*******Se crea mensaje a mostrar**************************
				final Dialog dialog=new Dialog(context);
				dialog.setContentView(R.layout.pedido_item_detalle);
				dialog.setTitle("Detalle del Pedido "+obj.getPedNropedido());

				LinearLayout layoutDialog=(LinearLayout)dialog.findViewById(R.id.layoutPedidoItemDetalle);
				
				TextView txtNroGuia = (TextView) dialog.findViewById(R.id.txtPedNroGuia);
				txtNroGuia.setText(detalle.get(7));
				
				TextView txtHr = (TextView) dialog.findViewById(R.id.txtPedHojaRuta);
				txtHr.setText(detalle.get(0));
				
				TextView txtCliente = (TextView) dialog.findViewById(R.id.txtPedCliente);
				txtCliente.setText(detalle.get(1));
				
				TextView txtDireccion = (TextView) dialog.findViewById(R.id.txtPedDireccion);
				txtDireccion.setText(detalle.get(2));
				
				TextView txtHoraPartida = (TextView) dialog.findViewById(R.id.txtPedHoraPartida);
				txtHoraPartida.setText(detalle.get(3));
				
				TextView txtHoraLlegada = (TextView) dialog.findViewById(R.id.txtPedHoraLlegada);
				txtHoraLlegada.setText(detalle.get(4));
				
				TextView txtLiquidado = (TextView) dialog.findViewById(R.id.txtPedLiquidado);
				txtLiquidado.setText(detalle.get(5));
				
				TextView txtMotivo = (TextView) dialog.findViewById(R.id.txtPedMotivo);
				txtMotivo.setText(detalle.get(6));
				
				
				layoutDialog.setOnClickListener(new OnClickListener() {
				
					@Override
					public void onClick(View v) {
					dialog.dismiss();
					}
				});
				dialog.show();
				//************************************************************
				
			}
		});
		
		return convertView;
	}
	
	public void setCheckedItem(int item) {
        if (checked.containsKey(String.valueOf(item))){
            checked.remove(String.valueOf(item));
        }
        else {
            checked.put(String.valueOf(item), String.valueOf(item));
        }
    }
        public HashMap<String, String> getCheckedItems(){
        return checked;
    }
	
}
