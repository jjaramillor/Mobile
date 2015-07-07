package app.main;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.database.cl_avance;

public class cl_avance_detalle_adapter extends BaseAdapter {

	Context context;
	List<cl_avance> _lista;
	private static LayoutInflater inflater = null;
	
	public cl_avance_detalle_adapter(Context context, List<cl_avance> list) {
		this.context = context;		
		_lista = list;
	}

	@Override
	public int getCount() {
		
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final cl_avance item = _lista.get(position);

		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.supervisor_avance_detalle_item_ui, null);
		}		
		
		if(item != null){			
		
		TextView txtTransportista1 = (TextView) convertView.findViewById(R.id.txtTransportista1);
		txtTransportista1.setText(String.valueOf(item.getTransportista()));
		
		TextView txtPlaneado1 = (TextView) convertView.findViewById(R.id.txtPlaneado1);
		txtPlaneado1.setText(item.getPlaneado());
		
		TextView txtEfectuado1 = (TextView) convertView.findViewById(R.id.txtEfectuado1);
		txtEfectuado1.setText(String.valueOf(item.getEfectuado()));
		
		TextView txtHR1 = (TextView) convertView.findViewById(R.id.txtHR1);
		txtHR1.setText(String.valueOf(item.getHR()));
		
		TextView txtDocumento1 = (TextView) convertView.findViewById(R.id.txtDocumento1);
		txtDocumento1.setText(String.valueOf(item.getDocumento()));
		
		TextView txtPedido1 = (TextView) convertView.findViewById(R.id.txtPedido1);
		txtPedido1.setText(String.valueOf(item.getPedido()));
		
		TextView txtCliente1 = (TextView) convertView.findViewById(R.id.txtCliente1);
		txtCliente1.setText(String.valueOf(item.getCliente()));
		
		TextView txtDestinatario1 = (TextView) convertView.findViewById(R.id.txtDestinatario1);
		txtDestinatario1.setText(String.valueOf(item.getDestinatario()));
		
		TextView txtFlgLiquida1 = (TextView) convertView.findViewById(R.id.txtFlgLiquida1);
		txtFlgLiquida1.setText(String.valueOf(item.getFlgLiquida()));
		
		TextView txtPartida1 = (TextView) convertView.findViewById(R.id.txtPartida1);
		txtPartida1.setText(String.valueOf(item.getPartida()));
		
		TextView txtLlegada1 = (TextView) convertView.findViewById(R.id.txtLlegada1);
		txtLlegada1.setText(String.valueOf(item.getLlegada()));
		
		TextView txtDescargo1 = (TextView) convertView.findViewById(R.id.txtDescargo1);
		txtDescargo1.setText(String.valueOf(item.getDescargo()));
		
		TextView txtLiquidado1 = (TextView) convertView.findViewById(R.id.txtLiquidado1);
		txtLiquidado1.setText(String.valueOf(item.getLiquidado()));
		
		TextView txtCodMotivo1 = (TextView) convertView.findViewById(R.id.txtCodMotivo1);
		txtCodMotivo1.setText(String.valueOf(item.getCodMotivo()));
		
		TextView txtMotivo1 = (TextView) convertView.findViewById(R.id.txtMotivo1);
		txtMotivo1.setText(String.valueOf(item.getMotivo()));
						
		}
		return convertView;
		
	}

}
