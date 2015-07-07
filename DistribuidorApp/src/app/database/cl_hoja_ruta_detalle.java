package app.database;

import java.util.Date;

public class cl_hoja_ruta_detalle {

	private int hrdCodigo;
	private int cliCodigo;
	private int pedCodigo;
	private String hrdTipDespacho;
	private String hrdCodTipLiq;
	private String hrdHoraParti;
	private String hrdHoraLlega;
	private String hrdHoraDes;
	private String hrdHoraPreLiq;
	private String hrdHoraLiq;
	private String usrCodMod;
	private String hrdFecParti;
	private String hrdFecLlega;
	private Date hrdFecDes;
	private Date hrdFecPreLiq;
	private Date hrdFecLiq;
	private Boolean Activo;
	private Date appFechaSync;
	private Boolean appEstadoSync;
	private Boolean hdrFlgLiq;
	private int hrdIdCorrelativo;
	private int hrCodigo;
	
	public int getCliCodigo() {
		return cliCodigo;
	}
	public void setCliCodigo(int cliCodigo) {
		this.cliCodigo = cliCodigo;
	}
	public Boolean getActivo() {
		return Activo;
	}
	public void setActivo(Boolean activo) {
		Activo = activo;
	}
	public Boolean getAppEstadoSync() {
		return appEstadoSync;
	}
	public void setAppEstadoSync(Boolean appEstadoSinc) {
		this.appEstadoSync = appEstadoSinc;
	}
	public int getHrdCodigo() {
		return hrdCodigo;
	}
	public void setHrdCodigo(int hrdCodigo) {
		this.hrdCodigo = hrdCodigo;
	}
	
	
	public int getHrdIdCorrelativo() {
		return hrdIdCorrelativo;
	}
	public void setHrdIdCorrelativo(int hrdIdCorrelativo) {
		this.hrdIdCorrelativo = hrdIdCorrelativo;
	}

	
	public int getHrCodigo() {
		return hrCodigo;
	}
	public void setHrCodigo(int hrCodigo) {
		this.hrCodigo = hrCodigo;
	}
	public int getPedCodigo() {
		return pedCodigo;
	}
	public void setPedCodigo(int pedCodigo) {
		this.pedCodigo = pedCodigo;
	}
	public String getHrdTipDespacho() {
		return hrdTipDespacho;
	}
	public void setHrdTipDespacho(String hrdTipDespacho) {
		this.hrdTipDespacho = hrdTipDespacho;
	}
	public String getHrdCodTipLiq() {
		return hrdCodTipLiq;
	}
	public void setHrdCodTipLiq(String hrdCodTipLiq) {
		this.hrdCodTipLiq = hrdCodTipLiq;
	}
	public String getHrdHoraParti() {
		return hrdHoraParti;
	}
	public void setHrdHoraParti(String hrdHoraParti) {
		this.hrdHoraParti = hrdHoraParti;
	}
	public String getHrdHoraLlega() {
		return hrdHoraLlega;
	}
	public void setHrdHoraLlega(String hrdHoraLlega) {
		this.hrdHoraLlega = hrdHoraLlega;
	}
	public String getHrdHoraDes() {
		return hrdHoraDes;
	}
	public void setHrdHoraDes(String hrdHoraDes) {
		this.hrdHoraDes = hrdHoraDes;
	}
	public String getHrdHoraPreLiq() {
		return hrdHoraPreLiq;
	}
	public void setHrdHoraPreLiq(String hrdHoraPreLiq) {
		this.hrdHoraPreLiq = hrdHoraPreLiq;
	}
	public String getHrdHoraLiq() {
		return hrdHoraLiq;
	}
	public void setHrdHoraLiq(String hrdHoraLiq) {
		this.hrdHoraLiq = hrdHoraLiq;
	}
	public String getUsrCodMod() {
		return usrCodMod;
	}
	public void setUsrCodMod(String usrCodMod) {
		this.usrCodMod = usrCodMod;
	}
	public String getHrdFecParti() {
		return hrdFecParti;
	}
	public void setHrdFecParti(String hrdFecParti) {
		this.hrdFecParti = hrdFecParti;
	}
	public String getHrdFecLlega() {
		return hrdFecLlega;
	}
	public void setHrdFecLlega(String hrdFecLlega) {
		this.hrdFecLlega = hrdFecLlega;
	}
	public Date getHrdFecDes() {
		return hrdFecDes;
	}
	public void setHrdFecDes(Date hrdFecDes) {
		this.hrdFecDes = hrdFecDes;
	}
	public Date getHrdFecPreLiq() {
		return hrdFecPreLiq;
	}
	public void setHrdFecPreLiq(Date hrdFecPreLiq) {
		this.hrdFecPreLiq = hrdFecPreLiq;
	}
	public Date getHrdFecLiq() {
		return hrdFecLiq;
	}
	public void setHrdFecLiq(Date hrdFecLiq) {
		this.hrdFecLiq = hrdFecLiq;
	}
	public Date getPpFechaSinc() {
		return appFechaSync;
	}
	public void setPpFechaSinc(Date ppFechaSinc) {
		this.appFechaSync = ppFechaSinc;
	}
	public Boolean getHdrFlgLiq() {
		return hdrFlgLiq;
	}
	public void setHdrFlgLiq(Boolean hdrFlgLiq) {
		this.hdrFlgLiq = hdrFlgLiq;
	}

}
