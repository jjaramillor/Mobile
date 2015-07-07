package app.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.database.cl_cliente;
import app.database.cl_consultas_entity;
import app.database.cl_general_param;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.cl_constantes;
import app.utils.cl_util;

public class cl_consultas_adapter extends BaseAdapter {
	Context context;
	ArrayList<cl_consultas_entity> _lista;

	private static LayoutInflater inflater = null;

	public cl_consultas_adapter(Context context,ArrayList<cl_consultas_entity> list) {

		this.context = context;
		this._lista = list;

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
		cl_consultas_entity objItem = _lista.get(position);

		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.consultas_list_item, null);

		}

		TextView nameConsulta = (TextView) convertView.findViewById(R.id.nameConsulta);
		nameConsulta.setText(objItem.getDescripcion());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				cl_consultas_entity obj = _lista.get(position);
				
				if(obj.getIdConsulta()==1){
					
					Intent intent = new Intent(context, cl_rep_cumplimiento_dia.class);
					
					context.startActivity(intent);
					((Activity) context).finish();
				}else if(obj.getIdConsulta()==2){
					
					Intent intent = new Intent(context, cl_rep_estado_entrega_dia.class);
					
					context.startActivity(intent);
					((Activity) context).finish();
				}

			}
		});

		return convertView;
	}

}
