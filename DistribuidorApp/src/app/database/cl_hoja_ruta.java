package app.database;

import java.sql.Date;
import java.sql.Time;

import android.database.sqlite.SQLiteDatabase;

public class cl_hoja_ruta {

	private int hrCodigo ;   
	private String hrNumeroHoja ;
	private Boolean hrFlgLiq ;	
	private Double hrKilometrajeIni ;
	private Double hrKilometraje ;
	
	private String usrCodMod ;
	private String FecMod ; 
	private Date hrFechaInicio;
	private Time hrHoraIniciao;
	private Boolean Activo ;
	private int vehCodigo ;
	private String vehplaca;
	
	private Boolean appEstadoSync;
	private Date appFechaSync;
	
	//Extras para Pendientes
	private int total;
	private int sync;
	
	public int getHrCodigo() {
		return hrCodigo;
	}
	public void setHrCodigo(int hrCodigo) {
		this.hrCodigo = hrCodigo;
	}
	public String getHrNumeroHoja() {
		return hrNumeroHoja;
	}
	public void setHrNumeroHoja(String hrNumeroHoja) {
		this.hrNumeroHoja = hrNumeroHoja;
	}
	public Boolean getHrFlgLiq() {
		return hrFlgLiq;
	}
	public void setHrFlgLiq(Boolean hrFlgLiq) {
		this.hrFlgLiq = hrFlgLiq;
	}
	public Double getHrKilometrajeIni() {
		return hrKilometrajeIni;
	}
	public void setHrKilometrajeIni(Double hrKilometrajeIni) {
		this.hrKilometrajeIni = hrKilometrajeIni;
	}
	public Double getHrKilometraje() {
		return hrKilometraje;
	}
	public void setHrKilometraje(Double hrKilometraje) {
		this.hrKilometraje = hrKilometraje;
	}
	public String getUsrCodMod() {
		return usrCodMod;
	}
	public void setUsrCodMod(String usrCodMod) {
		this.usrCodMod = usrCodMod;
	}
	public String getFecMod() {
		return FecMod;
	}
	public void setFecMod(String fecMod) {
		FecMod = fecMod;
	}
	public Date getHrFechaInicio() {
		return hrFechaInicio;
	}
	public void setHrFechaInicio(Date hrFechaInicio) {
		this.hrFechaInicio = hrFechaInicio;
	}
	public Time getHrHoraIniciao() {
		return hrHoraIniciao;
	}
	public void setHrHoraIniciao(Time hrHoraIniciao) {
		this.hrHoraIniciao = hrHoraIniciao;
	}
	public boolean isActivo() {
		return Activo;
	}
	public void setActivo(boolean activo) {
		Activo = activo;
	}
	public int getVehCodigo() {
		return vehCodigo;
	}
	public void setVehCodigo(int vehCodigo) {
		this.vehCodigo = vehCodigo;
	}
	public String getVehplaca() {
		return vehplaca;
	}
	public void setVehplaca(String vehplaca) {
		this.vehplaca = vehplaca;
	}
	public Boolean getAppEstadoSync() {
		return appEstadoSync;
	}
	public void setAppEstadoSync(Boolean appEstadoSync) {
		this.appEstadoSync = appEstadoSync;
	}
	public Date getAppFechaSync() {
		return appFechaSync;
	}
	public void setAppFechaSync(Date appFechaSync) {
		this.appFechaSync = appFechaSync;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSync() {
		return sync;
	}
	public void setSync(int sync) {
		this.sync = sync;
	}
	
	
	
}
