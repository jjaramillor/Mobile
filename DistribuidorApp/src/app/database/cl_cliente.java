package app.database;

//Destino
public class cl_cliente {

	private int cliCodigo;	
	private String cliNombre;
	
	private int hrCodigo;
	private int pedCodigo;

	
	private String dedNomVia;
	private int desCodigo;
	private int PedIdDireccion;
	
	private String hrdFecParti ;
	private String hrdFecLlega;
	private Boolean hdrFlgLiq ; 
	
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
	
	public Boolean getHdrFlgLiq() {
		return hdrFlgLiq;
	}
	public void setHdrFlgLiq(Boolean hdrFlgLiq) {
		this.hdrFlgLiq = hdrFlgLiq;
	}
	public int getCliCodigo() {
		return cliCodigo;
	}
	public void setCliCodigo(int cliCodigo) {
		this.cliCodigo = cliCodigo;
	}
	public String getCliNombre() {
		return cliNombre;
	}
	public void setCliNombre(String cliNombre) {
		this.cliNombre = cliNombre;
	}
	public String getDedNomVia() {
		return dedNomVia;
	}
	public void setDedNomVia(String dedNomVia) {
		this.dedNomVia = dedNomVia;
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
	
	//private boolean checked;
	
	
}
