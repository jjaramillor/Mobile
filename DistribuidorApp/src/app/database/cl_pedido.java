package app.database;

import java.sql.Date;

//import java.sql.Time;
//import java.util.Date;

public class cl_pedido {
	
	private int cliCodigo; 
			
	private int pedCodigo; 	
	private String pedGuiasGen;
	private String pedNropedido;
	private String pedEstado;
	
	private int desCodigo;
	private int PedIdDireccion;
	private String dedNomVia;
	
	//Atributos de viewmodel
	private String hrNumeroHoja;
	private String cliNombre;
	private String docAbrevia;
	private String desNombre;
	private boolean pedActualizar;
	
	//Atributos para Sync
	                             
	private String usrCodMod; 	                              
	private String hrdCodTipLiq;  
	private String hrdCodMotivo;
	private String hrdHoraParti; 
	private String hrdHoraLlega; 
	private String hrdHoraDes;
	private String hrdHoraPreLiq;
	private String fecMod;
	private String hrdFecPreLiq;
	
	private int hrCodigo;
	//end sync
	
	private Boolean appEstadoSync;
	private Date appFechaSync;
	
	public int getCliCodigo() {
		return cliCodigo;
	}	
	
	public void setCliCodigo(int cliCodigo) {
		this.cliCodigo = cliCodigo;
	}
	public int getPedCodigo() {
		return pedCodigo;
	}
	public void setPedCodigo(int pedCodigo) {
		this.pedCodigo = pedCodigo;
	}
	public String getPedGuiasGen() {
		return pedGuiasGen;
	}
	public void setPedGuiasGen(String pedGuiasGen) {
		this.pedGuiasGen = pedGuiasGen;
	}
	public String getPedNropedido() {
		return pedNropedido;
	}
	public void setPedNropedido(String pedNropedido) {
		this.pedNropedido = pedNropedido;
	}
	public String getPedEstado() {
		return pedEstado;
	}
	public void setPedEstado(String pedEstado) {
		this.pedEstado = pedEstado;
	}
	
	public int getDesCodigo() {
		return desCodigo;
	}
	public void setDesCodigo(int desCodigo) {
		this.desCodigo = desCodigo;
	}
	public int getPedIdDireccion() {
		return PedIdDireccion;
	}
	public void setPedIdDireccion(int pedIdDireccion) {
		PedIdDireccion = pedIdDireccion;
	}
	public String getDedNomVia() {
		return dedNomVia;
	}
	public void setDedNomVia(String dedNomVia) {
		this.dedNomVia = dedNomVia;
	}
	public String getHrNumeroHoja() {
		return hrNumeroHoja;
	}
	public void setHrNumeroHoja(String hrNumeroHoja) {
		this.hrNumeroHoja = hrNumeroHoja;
	}
	public String getCliNombre() {
		return cliNombre;
	}
	public void setCliNombre(String cliNombre) {
		this.cliNombre = cliNombre;
	}
	public String getDocAbrevia() {
		return docAbrevia;
	}
	public void setDocAbrevia(String docAbrevia) {
		this.docAbrevia = docAbrevia;
	}
	public String getDesNombre() {
		return desNombre;
	}
	public void setDesNombre(String desNombre) {
		this.desNombre = desNombre;
	}
	
	public boolean isPedActualizar() {
		return pedActualizar;
	}
	public void setPedActualizar(boolean pedActualizar) {
		this.pedActualizar = pedActualizar;
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

	//Para Sync
	
	public String getUsrCodMod() {
		return usrCodMod;
	}

	public void setUsrCodMod(String usrCodMod) {
		this.usrCodMod = usrCodMod;
	}

	public String getHrdCodTipLiq() {
		return hrdCodTipLiq;
	}

	public void setHrdCodTipLiq(String hrdCodTipLiq) {
		this.hrdCodTipLiq = hrdCodTipLiq;
	}

	public String getHrdCodMotivo() {
		return hrdCodMotivo;
	}

	public void setHrdCodMotivo(String hrdCodMotivo) {
		this.hrdCodMotivo = hrdCodMotivo;
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

	public String getFecMod() {
		return fecMod;
	}

	public void setFecMod(String fecMod) {
		this.fecMod = fecMod;
	}

	public String getHrdFecPreLiq() {
		return hrdFecPreLiq;
	}

	public void setHrdFecPreLiq(String hrdFecPreLiq) {
		this.hrdFecPreLiq = hrdFecPreLiq;
	}			
	//end Sync

	public int getHrCodigo() {
		return hrCodigo;
	}

	public void setHrCodigo(int hrCodigo) {
		this.hrCodigo = hrCodigo;
	}
	

}
