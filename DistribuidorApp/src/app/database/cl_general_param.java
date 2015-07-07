package app.database;

public class cl_general_param {

	private String usrCodMod;
	private String usrNombreCompleto;

	// usrCodMod
	public String getUsrCodMod() {
		return usrCodMod;
	}
	public void setUsrCodMod(String usrCodMod) {
		this.usrCodMod = usrCodMod;
	}
	
	public String getUsrNombreCompleto() {
		return usrNombreCompleto;
	}
	public void setUsrNombreCompleto(String usrNombreCompleto) {
		this.usrNombreCompleto = usrNombreCompleto;
	}



	private static final cl_general_param holder = new cl_general_param();

	public static cl_general_param getInstance() {
		return holder;
	}
}
