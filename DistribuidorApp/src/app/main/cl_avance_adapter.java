package app.main;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.database.cl_avance;
import app.utils.cl_util;

public class cl_avance_adapter extends BaseAdapter {
	
	Context context;
	List<cl_avance> _lista;
	private static LayoutInflater inflater = null;
	
	
	public cl_avance_adapter(Context context, List<cl_avance> list) {
		this.context = context;		
		_lista = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _lista ==null? 0 : _lista.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _lista.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {		
		//// http://www.learn2crack.com/2014/01/android-custom-gridview.html
		final cl_avance item = _lista.get(position);

		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.supervisor_avance_item_ui, null);
		}		
		
		if(item != null){
			
		
		TextView txtNro = (TextView) convertView.findViewById(R.id.txtNro);
		txtNro.setText(String.valueOf(position + 1));
		
		TextView txtTransportista = (TextView) convertView.findViewById(R.id.txtTransportista);
		txtTransportista.setText(item.getTransportista());
		
		TextView txtDocumentos = (TextView) convertView.findViewById(R.id.txtDocumentos);
		txtDocumentos.setText(String.valueOf(item.getDocumentos()));
		
		TextView txtAvance = (TextView) convertView.findViewById(R.id.txtAvance);
		txtAvance.setText(String.valueOf(item.getAvance()));
		
		convertView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				//cl_util.DisplayMessage(context, item.getTransportista(), "L");
				
				Bundle bundle = new Bundle();
				bundle.putString("paramTrans", item.getTransportista());	

				Intent menu = new Intent(context, cl_supervisor_avance_detalle.class);
				menu.putExtras(bundle);				
				context.startActivity(menu);
				((Activity)context).finish();
			}
		});
		}	
		
		return convertView;
		
	}

}
