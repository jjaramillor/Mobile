package app.services;

import java.util.ArrayList;
import app.database.cl_hoja_ruta;

public class ResultSync {
	
	private Boolean resultHR;
	private Boolean resultPedido;
	private ArrayList<cl_hoja_ruta> hojaRutaList;
	
	public ResultSync() {
		super();
		this.resultHR = false;
		this.resultPedido = false;
	}
	
	public Boolean getResultHR() {
		return resultHR;
	}
	public void setResultHR(Boolean resultHR) {
		this.resultHR = resultHR;
	}
	public Boolean getResultPedido() {
		return resultPedido;
	}
	public void setResultPedido(Boolean resultPedido) {
		this.resultPedido = resultPedido;
	}

	public ArrayList<cl_hoja_ruta> getHojaRutaList() {
		return hojaRutaList;
	}

	public void setHojaRutaList(ArrayList<cl_hoja_ruta> hojaRutaList) {
		this.hojaRutaList = hojaRutaList;
	}
	

}
