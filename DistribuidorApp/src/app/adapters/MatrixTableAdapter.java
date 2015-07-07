package app.adapters;

import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MatrixTableAdapter<T> extends BaseTableAdapter {
	private static LayoutInflater inflater = null;
	
	private final static int WIDTH_DIP = 110;
	private final static int HEIGHT_DIP = 32;

	private final Context context;

	private T[][] table;

	private final int width;
	private final int height;

	public MatrixTableAdapter(Context context) {
		this(context, null);
	}

	public MatrixTableAdapter(Context context, T[][] table) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		Resources r = context.getResources();

		width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
		height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

		setInformation(table);
	}

	public void setInformation(T[][] table) {
		this.table = table;
	}

	@Override
	public int getRowCount() {
		return table.length - 1;
	}

	@Override
	public int getColumnCount() {
		return table[0].length - 1;
	}
	
	

	@Override
	public View getView(int row, int column, View convertView, ViewGroup parent) {
		/*
		if (convertView == null) {			
			convertView = new TextView(context);
			((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
			
		}	
		((TextView) convertView).setText(table[row + 1][column + 1].toString());
		
		if (row < 0 ) {
			((TextView) convertView).setBackgroundResource(R.color.holo_red_dark);
		}		
		return convertView;
		*/
		
		final View view;
	
			if(row < 0)	{		
				view = inflater.inflate(app.main.R.layout.item_table1_header, parent, false);
				((TextView) view.findViewById(android.R.id.text1)).setText(table[row + 1][column + 1].toString());
			}
			else{				
				view =  inflater.inflate(app.main.R.layout.item_table1, parent, false);				
				((TextView) view.findViewById(android.R.id.text1)).setText(table[row + 1][column + 1].toString());
			}			
		
		return view;
		
	}

	@Override
	public int getHeight(int row) {
		return height;
	}

	@Override
	public int getWidth(int column) {
		return width;
	}

	@Override
	public int getItemViewType(int row, int column) {
		return 0;		
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}
}
