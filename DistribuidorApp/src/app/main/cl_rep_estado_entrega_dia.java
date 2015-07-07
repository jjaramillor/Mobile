package app.main;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DateSorter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import app.database.cl_hoja_ruta;
import app.database.db_distribuidor_assets;
import app.utils.PercentFormatterCustome;
import app.utils.cl_constantes;

public class cl_rep_estado_entrega_dia extends Activity{

	Context context;
	private RelativeLayout rlConsultas;
	private PieChart mChart;
	
	private float[] yData;
	private String[] xData={"Total","Parcial","No Entregado"};
	
	public void onCreate(Bundle savedData) {
		super.onCreate(savedData);
		this.setContentView(R.layout.rep_estado_entrega_dia_ui);
		context = this;
		
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
				
		// *******************Boton Regresar***************************
		ImageView btnAtras = (ImageView) findViewById(R.id.imbHrAtras);
		btnAtras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent menu = new Intent(cl_rep_estado_entrega_dia.this, cl_consultas.class);
				startActivity(menu);
				finish();
			}
		});
		// ****************************************************************
		//*****OBTENER DATA*****************
		db_distribuidor_assets db=new db_distribuidor_assets(context);
		yData=db.getEstadoEntregaDelDia();
		
		
		//**********************
		
		rlConsultas=(RelativeLayout)findViewById(R.id.rlEstadoEntregaDia);
		mChart=new PieChart(this);
		//add pie chart to rlConsultas
		rlConsultas.addView(mChart);
		rlConsultas.setBackgroundColor(Color.LTGRAY);
		
		//configure pie chart
		mChart.setUsePercentValues(true);
		mChart.setDescription("Estado de Entrega del Día");
		
		//enable hole and configure
		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(true);
		mChart.setHoleRadius(20);
		mChart.setTransparentCircleRadius(10);
		
		//enable rotation of the chart by touch
		mChart.setRotationAngle(0);
		mChart.setRotationEnabled(true);
		
		
		
		//add Data
		addData();
		
		//custome legends
		Legend l=mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(7);
		l.setYEntrySpace(5);
		
		
		//formato decimal
		final DecimalFormat df = new DecimalFormat("###.00");
		
		//show total Y
		mChart.setCenterText("Total:"+mChart.getYValueSum());
				
		//set a chart value selected listerner
		mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			
			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
					if(e==null)
						return;
					
					Toast.makeText(cl_rep_estado_entrega_dia.this, xData[e.getXIndex()]+" = "+df.format((e.getVal()*100/mChart.getYValueSum()))+"%", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onNothingSelected() {
				// TODO Auto-generated method stub
				
			}
		});
				
		
	}
	
	private void addData(){
		
		ArrayList<Entry> yVals1=new ArrayList<Entry>();
		
		for(int i=0;i<yData.length;i++)
			yVals1.add(new Entry(yData[i], i));
		
		
		ArrayList<String> xVals=new ArrayList<String>();
		
		for(int i=0;i<xData.length;i++)
			xVals.add(xData[i]);
		
		//create dataset
		PieDataSet dataSet=new PieDataSet(yVals1, "Leyenda");
		dataSet.setSliceSpace(3);
		dataSet.setSelectionShift(5);
		//dataSet.setColor(Color.BLACK);
		
		
		//add many colors
        
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(Color.RED);
		dataSet.setColors(colors);
		
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatterCustome());
        
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        
        mChart.setData(data);
        
        // undo all highlights
        mChart.highlightValues(null);

        //update pie chart
        mChart.invalidate();
		
	}
}
