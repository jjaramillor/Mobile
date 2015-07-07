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
import app.database.cl_estadistica;
import app.utils.cl_util;

public class cl_estadistica_adapter extends BaseAdapter {
	Context context;
	List<cl_estadistica> _lista;
	private static LayoutInflater inflater = null;
	
	public cl_estadistica_adapter(Context context, List<cl_estadistica> list) {
		this.context = context;		
		_lista = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _lista ==null? 0 : _lista.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {		
		//// http://www.learn2crack.com/2014/01/android-custom-gridview.html
		final cl_estadistica item = _lista.get(position);

		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.supervisor_estadistica_item_ui, null);
		}		
		
		if(item != null){
			
		
		TextView txtNro = (TextView) convertView.findViewById(R.id.txtNro);
		txtNro.setText(String.valueOf(position + 1));
		
		TextView txtCodMotivo = (TextView) convertView.findViewById(R.id.txtCodMotivo);
		txtCodMotivo.setText(item.getCodMotivo());
		
		TextView txtMotivo = (TextView) convertView.findViewById(R.id.txtMotivo);
		txtMotivo.setText(String.valueOf(item.getMotivo()));
		
		TextView txtCantidad = (TextView) convertView.findViewById(R.id.txtCantidad);
		txtCantidad.setText(String.valueOf(item.getCantidad()));
		
		convertView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				//cl_util.DisplayMessage(context, item.getCodMotivo(), "L");
				
				Bundle bundle = new Bundle();
				bundle.putString("paramCodMotivo", item.getCodMotivo());
				bundle.putString("paramMotivo", item.getMotivo());

				Intent menu = new Intent(context, cl_supervisor_estadistica_detalle.class);
				menu.putExtras(bundle);				
				context.startActivity(menu);
				((Activity)context).finish();
				
			}
		});
		}	
		
		return convertView;
		
	}

}
