package app.database;


import java.util.ArrayList;

public class cl_pedido_data_holder {

	private ArrayList<cl_pedido> pedidos;

	
	public ArrayList<cl_pedido> getData() {
		return pedidos;
	}

	public void setData(ArrayList<cl_pedido> data) {
		this.pedidos = data;
	}

	private static final cl_pedido_data_holder holder = new cl_pedido_data_holder();

	public static cl_pedido_data_holder getInstance() {
		return holder;
	}
}
