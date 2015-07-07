package app.database;

import java.sql.Date;

public class cl_hoja_ruta_pedidos {

	private int PedCodigo;
   
	private String hrNumeroHoja;
    private int cliCodigo;
	private String cliNombre;
    private String docAbrevia;
    private String pedNroDocRef;
    private String pedNropedido;
    private String desNombre;
    private String PedEstado;
    private String Estado;
    private String Motivo;
    private String hrdcodMotivo;
    
    private int desCodigo;
	private int PedIdDireccion;
	private String dedNomVia;
	
	private String pedGuiasGen;
    
	public String getPedGuiasGen() {
		return pedGuiasGen;
	}
	public void setPedGuiasGen(String pedGuiasGen) {
		this.pedGuiasGen = pedGuiasGen;
	}
	public int getPedCodigo() {
		return PedCodigo;
	}
	public void setPedCodigo(int pedCodigo) {
		PedCodigo = pedCodigo;
	}
	public String getHrNumeroHoja() {
		return hrNumeroHoja;
	}
	public void setHrNumeroHoja(String hrNumeroHoja) {
		this.hrNumeroHoja = hrNumeroHoja;
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
	public String getDocAbrevia() {
		return docAbrevia;
	}
	public void setDocAbrevia(String docAbrevia) {
		this.docAbrevia = docAbrevia;
	}
	public String getPedNroDocRef() {
		return pedNroDocRef;
	}
	public void setPedNroDocRef(String pedNroDocRef) {
		this.pedNroDocRef = pedNroDocRef;
	}
	public String getPedNropedido() {
		return pedNropedido;
	}
	public void setPedNropedido(String pedNropedido) {
		this.pedNropedido = pedNropedido;
	}
	public String getDesNombre() {
		return desNombre;
	}
	public void setDesNombre(String desNombre) {
		this.desNombre = desNombre;
	}
	public String getPedEstado() {
		return PedEstado;
	}
	public void setPedEstado(String pedEstado) {
		PedEstado = pedEstado;
	}
	public String getEstado() {
		return Estado;
	}
	public void setEstado(String estado) {
		Estado = estado;
	}
	public String getMotivo() {
		return Motivo;
	}
	public void setMotivo(String motivo) {
		Motivo = motivo;
	}
	public String getHrdcodMotivo() {
		return hrdcodMotivo;
	}
	public void setHrdcodMotivo(String hrdcodMotivo) {
		this.hrdcodMotivo = hrdcodMotivo;
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
    
    

}
