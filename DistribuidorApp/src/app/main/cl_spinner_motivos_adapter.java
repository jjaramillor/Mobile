package app.main;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.database.cl_hoja_ruta;
import app.database.cl_motivos;

public class cl_spinner_motivos_adapter extends BaseAdapter {

	ArrayList<cl_motivos> motivos;
	private static LayoutInflater inflater = null;
	Context context;

	public cl_spinner_motivos_adapter(Context context,
			ArrayList<cl_motivos> list) {

		this.context = context;
		motivos = list;

	}

	@Override
	public int getCount() {

		return motivos.size();
	}

	@Override
	public cl_motivos getItem(int position) {

		return motivos.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final cl_motivos obj = motivos.get(position);

		TextView label = new TextView(context);
		// label.setTextColor(Color.BLACK);
		label.setText(obj.getDescripcion());
		label.setTag(obj.getCodReg());
		return label;

		// return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		final cl_motivos obj = motivos.get(position);

		TextView label = new TextView(context);
		// label.setTextColor(Color.BLACK);
		label.setText(obj.getDescripcion());
		label.setTag(obj.getCodReg());
		return label;
	}
}
