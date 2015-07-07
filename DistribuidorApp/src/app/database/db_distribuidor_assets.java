package app.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import app.utils.cl_util;

public class db_distribuidor_assets extends SQLiteOpenHelper {

	static String DB_PATH = "/data/data/app.main/databases/";
	static String DB_NAME = "distribuidor.sqlite";

	private SQLiteDatabase myDataBase;
	private final Context mycontext;
	
	static final int DataBase_vesion = 1;
	String TAG = "db_distribuidor";

	static final String tablaCliente = "tblCliente";
	static final String tablaPedido = "tblPedido";
	static final String tablaUserEntity = "tblUserEntity";
	static final String tablaHojaRuta = "tblHojaRuta";
	static final String tablaHojaRutaDetalle = "tblHojaRutaDetalle";
	static final String tablaMotivoNoEntrega = "tblMotivoNoEntrega";
	
	public db_distribuidor_assets(Context context) {
		super(context, DB_NAME, null, DataBase_vesion);

		this.mycontext = context;

		// Verificamos si existe la db, si no la creamos
		boolean dbExiste = checkDataBase();
		if (dbExiste) {
			openDataBase("R");
		} else {
			createDataBase();
		}
	}	

	// ************************************
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	private void createDataBase() {
		// Abrimos la base de datos
		this.getReadableDatabase();

		try {
			copyDataBase();
		} catch (Exception e) {
			throw new Error("Error copiando base de datos");
		}
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = mycontext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;

		OutputStream myOuput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;

		while ((length = myInput.read(buffer)) > 0) {
			myOuput.write(buffer, 0, length);
		}
		// liberamos los streams
		myOuput.flush();
		myOuput.close();
		myInput.close();

	}

	private void openDataBase(String Tipo) {
		String myPath = DB_PATH + DB_NAME;
		if (Tipo == "R")
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		else if (Tipo == "W")
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

	}

	private boolean checkDataBase() {
		// TODO Auto-generated method stub
		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public List<String> getAllClientes() {
		List<String> Clientes = new ArrayList<String>();

		String tsql = "SELECT cliNombre FROM " + tablaCliente;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(tsql, null);
		if (cursor.moveToFirst()) {
			do {
				Clientes.add(cursor.getString(0).toString());
			} while (cursor.moveToNext());
		}
		db.close();
		return Clientes;

	}
	
	public List<String> getClientesByHojaRutaStr(int hrId) {
		List<String> Clientes = new ArrayList<String>();

		String tsql = "SELECT C.cliNombre FROM tblCliente C ";
			   tsql+=" INNER JOIN tblHojaRutaDetalle HD on C.cliCodigo=HD.cliCodigo";
			   tsql+=" WHERE HD.hrCodigo="+hrId;
			   tsql+=" GROUP BY C.cliNombre";
			   
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(tsql, null);
		if (cursor.moveToFirst()) {
			do {
				Clientes.add(cursor.getString(0).toString());
			} while (cursor.moveToNext());
		}
		db.close();
		return Clientes;

	}
	
	public ArrayList<cl_cliente> getClientesByHojaRuta(int hrId) {
		ArrayList<cl_cliente> Clientes = new ArrayList<cl_cliente>();
		/*
		String tsql = "SELECT C.cliCodigo,C.cliNombre FROM tblCliente C ";
			   tsql+=" INNER JOIN tblHojaRutaDetalle HD on C.cliCodigo=HD.cliCodigo";
			   tsql+=" WHERE HD.hrCodigo="+hrId;
			   tsql+=" GROUP BY C.cliCodigo,C.cliNombre";
		 */
		String tsql = "SELECT C.cliCodigo,HD.hrCodigo,HD.pedCodigo,HD.hrdCodigo,C.cliNombre, P.desCodigo, P.PedIdDireccion, P.dedNomVia ";
			tsql+=" ,HD.hrdFecParti ,HD.hrdFecLlega,(case when sum(ifnull(HD.hdrFlgLiq,0))==count(1) then 1 else 0 end) as hdrFlgLiq   ";
			tsql+=" FROM tblCliente C ";
		   tsql+=" INNER JOIN tblHojaRutaDetalle HD on C.cliCodigo=HD.cliCodigo";
		   tsql+=" INNER JOIN tblPedido P on  C.cliCodigo = P.cliCodigo and HD.pedCodigo = P.pedCodigo";
		   tsql+=" WHERE HD.hrCodigo="+hrId;
		   tsql+=" GROUP BY C.cliCodigo,HD.hrdCodigo, C.cliNombre,P.desCodigo, P.PedIdDireccion, P.dedNomVia,HD.hrdFecParti ,HD.hrdFecLlega ";
			   
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(tsql, null);
		if (cursor.moveToFirst()) {
			do {
				cl_cliente cliente=new cl_cliente();
				cliente.setCliCodigo(cursor.getInt(cursor.getColumnIndex("cliCodigo")));
				cliente.setHrCodigo(cursor.getInt(cursor.getColumnIndex("hrCodigo")));
				cliente.setPedCodigo(cursor.getInt(cursor.getColumnIndex("pedCodigo")));
				cliente.setCliNombre(cursor.getString(cursor.getColumnIndex("cliNombre")));
				cliente.setDesCodigo(cursor.getInt(cursor.getColumnIndex("desCodigo")));
				cliente.setPedIdDireccion(cursor.getInt(cursor.getColumnIndex("PedIdDireccion")));
				cliente.setDedNomVia(cursor.getString(cursor.getColumnIndex("dedNomVia")));
				
				cliente.setHrdFecParti(cursor.getString(cursor.getColumnIndex("hrdFecParti")));
				cliente.setHrdFecLlega(cursor.getString(cursor.getColumnIndex("hrdFecLlega")));
				cliente.setHdrFlgLiq(cl_util.convertToBoolean(cursor.getString(cursor.getColumnIndex("hdrFlgLiq"))));
				
				Clientes.add(cliente);
			} while (cursor.moveToNext());
		}
		db.close();
		return Clientes;

	}

	public List<cl_pedido> getAllPedidos(int idCliente, int desCodigo, int pedIdDireccion ) {
		List<cl_pedido> Pedidos = new ArrayList<cl_pedido>();
	
		String tsql = " SELECT pedCodigo, pedNropedido, pedEstado,cliCodigo,pedGuiasGen FROM tblPedido ";
			   tsql += " WHERE cliCodigo =" + idCliente;
			   tsql += " and  desCodigo =" + desCodigo;
			   tsql += " and  PedIdDireccion =" + pedIdDireccion;
				
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(tsql, null);
		if (cursor.moveToFirst()) {
			do {
				cl_pedido _pedido = new cl_pedido();
				_pedido.setPedCodigo(cursor.getInt(0));
				_pedido.setPedNropedido(cursor.getString(1).toString());
				_pedido.setPedEstado(cursor.getString(2));
				_pedido.setCliCodigo(cursor.getInt(3));
				_pedido.setPedGuiasGen(cursor.getString(4));
				
				Pedidos.add(_pedido);
			} while (cursor.moveToNext());
		}
		db.close();
		return Pedidos;

	}
	
	public ArrayList<cl_pedido> getPedidosNoLiquidados(int idCliente, int desCodigo, int pedIdDireccion ) {
		ArrayList<cl_pedido> Pedidos = new ArrayList<cl_pedido>();
	
		String tsql = " SELECT pedCodigo, pedNropedido, pedEstado,cliCodigo FROM tblPedido ";
			   tsql += " WHERE cliCodigo =" + idCliente;
			   tsql += " and  desCodigo =" + desCodigo;
			   tsql += " and  PedIdDireccion =" + pedIdDireccion;
			   tsql += " and  pedEstado is null";
				
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(tsql, null);
		if (cursor.moveToFirst()) {
			do {
				cl_pedido _pedido = new cl_pedido();
				_pedido.setPedCodigo(cursor.getInt(0));
				_pedido.setPedNropedido(cursor.getString(1).toString());
				_pedido.setPedEstado(cursor.getString(2));
				_pedido.setCliCodigo(cursor.getInt(3));
				Pedidos.add(_pedido);
			} while (cursor.moveToNext());
		}
		db.close();
		return Pedidos;

	}

	// HOJA DE RUTA
	public ArrayList<cl_hoja_ruta> getHojaRutaListar() {
		try {

			ArrayList<cl_hoja_ruta> hojaRutas = new ArrayList<cl_hoja_ruta>();

			String tsql = "SELECT * FROM " + tablaHojaRuta;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			if (cursor.moveToFirst()) {
				do {
					boolean value = (cursor.getInt(cursor.getColumnIndex("hrFlgLiq")) == 1);
					cl_hoja_ruta hojaRuta = new cl_hoja_ruta();
					hojaRuta.setHrCodigo(cursor.getInt(cursor
							.getColumnIndex("hrCodigo")));
					hojaRuta.setHrNumeroHoja(cursor.getString(cursor
							.getColumnIndex("hrNumeroHoja")));
					hojaRuta.setHrKilometraje(cursor.getDouble(cursor
							.getColumnIndex("hrKilometraje")));
					hojaRuta.setHrFlgLiq(value);
					hojaRutas.add(hojaRuta);
				} while (cursor.moveToNext());
			}
			db.close();
			return hojaRutas;
			
		} catch (SQLiteException e) {
			return null;
		}

	}
	public ArrayList<cl_hoja_ruta> getHojaRutaIniciadaListar() {
		try {

			ArrayList<cl_hoja_ruta> hojaRutas = new ArrayList<cl_hoja_ruta>();

			String tsql = "SELECT * FROM " + tablaHojaRuta+" WHERE hrKilometraje > 0";
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			if (cursor.moveToFirst()) {
				do {
					boolean value = (cursor.getInt(cursor.getColumnIndex("hrFlgLiq")) == 1);
					cl_hoja_ruta hojaRuta = new cl_hoja_ruta();
					hojaRuta.setHrCodigo(cursor.getInt(cursor
							.getColumnIndex("hrCodigo")));
					hojaRuta.setHrNumeroHoja(cursor.getString(cursor
							.getColumnIndex("hrNumeroHoja")));
					hojaRuta.setHrKilometraje(cursor.getDouble(cursor
							.getColumnIndex("hrKilometraje")));
					hojaRuta.setHrFlgLiq(value);

					hojaRutas.add(hojaRuta);
				} while (cursor.moveToNext());
			}
			db.close();
			return hojaRutas;
			
		} catch (SQLiteException e) {
			return null;
		}

	}
	public int IniciarHojaRuta(int hrCodigo,String usrCodMod){
		int result=0;
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("hrKilometraje", 1);
		values.put("usrCodMod", usrCodMod);
		values.put("hrFechaInicio",new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		values.put("hrHoraInicio",new SimpleDateFormat("HH:mm:ss").format(new Date()));
		
		result=db.update(tablaHojaRuta, values,"hrCodigo="+hrCodigo , null);
		db.close();
		
		return result;
	}
	
	public ArrayList<String> getPedidoDetalle(int pedCodigo) {
		ArrayList<String> result = new ArrayList<String>();

		try {

			String query="select H.hrNumeroHoja,P.desNombre as Cliente,P.dedNomVia as Direccion ";
					query+=" ,ifnull(HRD.hrdHoraParti,'') as HoraPartida ";
					query+=" ,ifnull(HRD.hrdHoraLlega,'') as HoraLLegada ";
					query+=" ,(case when P.pedEstado='00008' then 'Total' when P.pedEstado='00009' then 'Parcial' when P.pedEstado='00010' then 'No Entregado' else '' end) as Liquidado ";
					query+=" ,M.Descripcion as Motivo ";
					query+=" ,P.pedGuiasGen as NroGuia ";
					query+=" from tblPedido P ";
					query+=" inner join tblHojaRutaDetalle HRD on P.cliCodigo=HRD.cliCodigo and P.pedCodigo=HRD.pedCodigo ";
					query+=" inner join tblHojaRuta H on HRD.hrCodigo=H.hrCodigo ";
					query+=" left join tblMotivoNoEntrega M on HRD.hrdCodMotivo=M.CodReg ";
					query+=" where P.pedCodigo="+pedCodigo;
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(query, null);

			if (cursor != null && cursor.moveToFirst()) {

				result.add(cursor.getString(cursor.getColumnIndex("hrNumeroHoja")));
				result.add(cursor.getString(cursor.getColumnIndex("Cliente")));
				result.add(cursor.getString(cursor.getColumnIndex("Direccion")));
				result.add(cursor.getString(cursor.getColumnIndex("HoraPartida")));
				result.add(cursor.getString(cursor.getColumnIndex("HoraLLegada")));
				result.add(cursor.getString(cursor.getColumnIndex("Liquidado")));
				result.add(cursor.getString(cursor.getColumnIndex("Motivo")));
				result.add(cursor.getString(cursor.getColumnIndex("NroGuia")));

				cursor.close();
			}
			db.close();
			return result;
		} catch (SQLiteException e) {
			return result;
		}

	}
	
	public cl_hoja_ruta getHojaRuta(int hrCodigo) {
		cl_hoja_ruta result = new cl_hoja_ruta();

		try {

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT hrCodigo, hrNumeroHoja,hrKilometraje,hrKilometrajeIni, usrCodMod, hrFechaInicio FROM " + tablaHojaRuta
					+ " WHERE hrCodigo=" + hrCodigo, null);

			if (cursor != null && cursor.moveToFirst()) {

				result.setHrCodigo(cursor.getInt(cursor
						.getColumnIndex("hrCodigo")));
				result.setHrNumeroHoja(cursor.getString(cursor
						.getColumnIndex("hrNumeroHoja")));
				result.setHrKilometraje(cursor.getDouble(cursor
						.getColumnIndex("hrKilometraje")));
				result.setHrKilometrajeIni(cursor.getDouble(cursor
						.getColumnIndex("hrKilometrajeIni")));
				result.setUsrCodMod(cursor.getString(cursor
						.getColumnIndex("usrCodMod")));
				result.setFecMod(cursor.getString(cursor.getColumnIndex("hrFechaInicio")));

				cursor.close();
			}
			db.close();
			return result;
		} catch (SQLiteException e) {
			return result;
		}

	}
	public cl_hoja_ruta_detalle getHojaRutaDetalle(int hrCodigo,int cliCodigo,int pedCodigo) {
		cl_hoja_ruta_detalle result = new cl_hoja_ruta_detalle();

		try {

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM " + tablaHojaRutaDetalle
					+ " WHERE hrCodigo=" + hrCodigo+ " and cliCodigo=" + cliCodigo+ " and pedCodigo=" + pedCodigo, null);

			if (cursor != null && cursor.moveToFirst()) {

				result.setHrCodigo(cursor.getInt(cursor.getColumnIndex("hrCodigo")));
				result.setHrdCodigo(cursor.getInt(cursor.getColumnIndex("hrdCodigo")));
				result.setHrdFecParti(cursor.getString(cursor.getColumnIndex("hrdFecParti")));//String dateString = SimpleDateFormat("yyyy-MM-dd").format(new Date(result.getHrdFecParti()));
				result.setHrdHoraParti(cursor.getString(cursor.getColumnIndex("hrdHoraParti")));
				result.setHrdFecLlega(cursor.getString(cursor.getColumnIndex("hrdFecLlega")));
				result.setHrdHoraLlega(cursor.getString(cursor.getColumnIndex("hrdHoraLlega")));

				cursor.close();
			}
			db.close();
			return result;
		} catch (SQLiteException e) {
			return result;
		}

	}
	/*
	public String updHojaRutaPartida(int hrCodigo,int cliCodigo,int pedCodigo){
		String result="";
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		String hora=new SimpleDateFormat("HH:mm:ss").format(new Date());
		values.put("hrdFecParti", fecha);
		values.put("hrdHoraParti",hora );
		
		//int exec=db.update(tablaHojaRutaDetalle, values,"hrCodigo="+hrCodigo+" and cliCodigo="+cliCodigo+" and pedCodigo="+pedCodigo , null);
		int exec=db.update(tablaHojaRutaDetalle, values,"hrCodigo="+hrCodigo+" and cliCodigo="+cliCodigo , null);
		db.close();
		
		if(exec>0)
		{
			result=fecha+" "+hora;
		}
		
		return result;
	}*/
	public String updHojaRutaPartida(int hrCodigo,List<cl_pedido>pedidos){
		String result="";
		//-------------------
		try {
			boolean success = false;

			if (!pedidos.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					
					String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
					String hora=new SimpleDateFormat("HH:mm:ss").format(new Date());
					
					for (cl_pedido pedido : pedidos) {

						ContentValues values = new ContentValues();
						
						values.put("hrdFecParti", fecha);
						values.put("hrdHoraParti",hora );
						
						success=db.update(tablaHojaRutaDetalle, values,"hrCodigo="+hrCodigo+" and cliCodigo="+pedido.getCliCodigo()+" and pedCodigo="+pedido.getPedCodigo() , null)>0;

						if (!success)
							break;
					}

					if (success){
						db.setTransactionSuccessful();
						result=fecha+" "+hora;
					}
						

				} finally {
		
					db.endTransaction();
					db.close();
				}
			}
			return result;

		} catch (SQLiteException e) {
			return "";
		}
		//-------------
		
		//return result;
		
		
	}

	public String updHojaRutaLlegada(int hrCodigo,List<cl_pedido>pedidos){
		String result="";
		
		//-------------------
		try {
			boolean success = false;

			if (!pedidos.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					
					String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
					String hora=new SimpleDateFormat("HH:mm:ss").format(new Date());
					
					for (cl_pedido pedido : pedidos) {

						ContentValues values = new ContentValues();
						
						values.put("hrdFecLlega", fecha);
						values.put("hrdHoraLlega", hora);
						
						success=db.update(tablaHojaRutaDetalle, values,"hrCodigo="+hrCodigo+" and cliCodigo="+pedido.getCliCodigo()+" and pedCodigo="+pedido.getPedCodigo() , null)>0;

						if (!success)
							break;
					}

					if (success){
						db.setTransactionSuccessful();
						result=fecha+" "+hora;
					}
						

				} finally {
		
					db.endTransaction();
					db.close();
				}
			}
			return result;

		} catch (SQLiteException e) {
			return "";
		}
		//-------------
	}
	/*
	public String updHojaRutaLlegada(int hrCodigo,int cliCodigo,int pedCodigo){
		String result="";
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		String hora=new SimpleDateFormat("HH:mm:ss").format(new Date());
		
		values.put("hrdFecLlega", fecha);
		values.put("hrdHoraLlega", hora);
		
		//int exec =db.update(tablaHojaRutaDetalle, values,"hrCodigo="+hrCodigo+" and cliCodigo="+cliCodigo+" and pedCodigo="+pedCodigo , null);
		int exec =db.update(tablaHojaRutaDetalle, values,"hrCodigo="+hrCodigo+" and cliCodigo="+cliCodigo , null);
		db.close();
		
		if(exec>0)
		{
			result=fecha+" "+hora;
		}
		
		return result;
	}*/
	// Usuario
	public boolean insertUserEntity(cl_user_entity user) {
		boolean success = false;

		ContentValues values = new ContentValues();
		values.put("vehCodigo", user.getVehCodigo());
		values.put("traNombre", user.getTraNombre());
		values.put("traApePat", user.getTraApePat());
		values.put("traNumDoc", user.getTraNumDoc());
		values.put("vehPlaca", user.getVehPlaca());
		values.put("traCodigo", user.getTraCodigo());
		values.put("traNomCorto", user.getTraNomCorto());
		values.put("usrCodMod", user.getUsrCodMod());

		SQLiteDatabase db = this.getWritableDatabase();
		success = db.insert(tablaUserEntity, null, values) > 0;
		db.close();
		return success;
	}

	public cl_user_entity getUserEntity() {

		cl_user_entity result = new cl_user_entity();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + tablaUserEntity, null);

		if (cursor != null && cursor.moveToFirst()) {

			result.setTraNumDoc(cursor.getString(cursor.getColumnIndex("traNumDoc")));
			result.setVehPlaca(cursor.getString(cursor.getColumnIndex("vehPlaca")));
			result.setUsrCodMod(cursor.getString(cursor.getColumnIndex("usrCodMod")));
			result.setTraCodigo(cursor.getInt(cursor.getColumnIndex("traCodigo")));
			result.setTraNomCorto(cursor.getString(cursor.getColumnIndex("traNomCorto")));
			result.setTraApePat(cursor.getString(cursor.getColumnIndex("traApePat")));
			result.setTraNombre(cursor.getString(cursor.getColumnIndex("traNombre")));

			cursor.close();
		}
		db.close();
		return result;

	}

	public void deleteUserEntity() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tablaUserEntity, null, null);
		db.close();

	}

	// ****** Sincronizacion******
	public int verificarItemsPorSincronizar() {

		try {
			int result = 0;

			String tsql = "SELECT COUNT(1) FROM " + tablaHojaRuta
					+ " WHERE hrFlgLiq=1 AND appEstadoSync=0";
			SQLiteDatabase db = this.getReadableDatabase();
			SQLiteStatement st = db.compileStatement(tsql);
			// Cursor cursor = db.rawQuery(tsql, null);
			result = (int) st.simpleQueryForLong();
			db.close();
			return result;

		} catch (SQLiteException e) {
			return 0;
		}

	}

	// Eliminar data
	public Boolean deletePedidosSync() {

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(tablaPedido, null, null);
			db.close();
			return true;

		} catch (SQLiteException e) {
			return false;
		}

	}

	public Boolean deleteHojaRutaDetalleSync() {

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(tablaHojaRutaDetalle, null, null);
			db.close();

			return true;

		} catch (SQLiteException e) {
			return false;
		}

	}

	public Boolean deleteHojaRutaSync() {

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(tablaHojaRuta, null, null);
			db.close();
			return true;
		} catch (SQLiteException e) {
			return false;
		}
	}
	
	public Boolean deleteDestinosHojaRutaSync() {

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(tablaCliente, null, null);
			db.close();
			return true;
		} catch (SQLiteException e) {
			return false;
		}
	}
	
	// Insertar Data
	public Boolean insertHojaRuta(List<cl_hoja_ruta> hojasRutas) {

		try {
			boolean success = false;

			if (!hojasRutas.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					for (cl_hoja_ruta hoja : hojasRutas) {

						ContentValues values = new ContentValues();
						values.put("hrCodigo", hoja.getHrCodigo());
						values.put("hrNumeroHoja", hoja.getHrNumeroHoja());
						values.put("vehplaca", hoja.getVehplaca());
						values.put("hrKilometraje", hoja.getHrKilometraje());
						values.put("hrKilometrajeIni", hoja.getHrKilometrajeIni());

						success = db.insert(tablaHojaRuta, null, values) > 0;

						if (!success)
							break;
					}

					if (success)
						db.setTransactionSuccessful();

				} finally {
		
					db.endTransaction();
					db.close();
				}
			}
			return success;

		} catch (SQLiteException e) {
			return false;
		}
	}
	public Boolean insertHojaRutaDetalle(List<cl_hoja_ruta_detalle> hrDetalles) {

		try {
			boolean success = false;

			if (!hrDetalles.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					for (cl_hoja_ruta_detalle detalle : hrDetalles) {

						ContentValues values = new ContentValues();
						values.put("cliCodigo", detalle.getCliCodigo());						
						values.put("hrCodigo",detalle.getHrCodigo());
						values.put("pedCodigo", detalle.getPedCodigo());

						success = db.insert(tablaHojaRutaDetalle, null, values) > 0;

						if (!success)
							break;
					}

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}

			return success;

		} catch (SQLiteException e) {
			return false;
		}
	}
	public Boolean insertHojaRutaClientes(List<cl_cliente> hrClientes) {

		try {
			boolean success = false;

			if (!hrClientes.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					for (cl_cliente cliente : hrClientes) {

						ContentValues values = new ContentValues();
						values.put("cliCodigo", cliente.getCliCodigo());
						values.put("cliNombre", cliente.getCliNombre());
						
						//TODO:Verificar si existe el cliente, en la db local
						//if(ExisteDestino(cliente.getCliCodigo()))
						success = db.insert(tablaCliente, null, values) > 0;

						if (!success)
							break;
					}

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}

			return success;

		} catch (SQLiteException e) {
			return false;
		}
	}
	public Boolean insertHojaRutaPedidos(List<cl_hoja_ruta_pedidos> hrPedidos) {

		try {
			boolean success = false;

			if (!hrPedidos.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					for (cl_hoja_ruta_pedidos detalle : hrPedidos) {

						ContentValues values = new ContentValues();
												
						values.put("pedCodigo",detalle.getPedCodigo());
						//values.put("cliNombre",detalle.getCliNombre());
						values.put("cliCodigo",detalle.getCliCodigo());
						values.put("desNombre", detalle.getDesNombre());						
						values.put("pedNropedido",detalle.getPedNropedido());
						
						values.put("desCodigo",detalle.getDesCodigo());
						values.put("PedIdDireccion",detalle.getPedIdDireccion());
						values.put("dedNomVia",detalle.getDedNomVia());
						values.put("pedGuiasGen",detalle.getPedGuiasGen());
												
						success = db.insert(tablaPedido, null, values) > 0;

						if (!success)
							break;
					}

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}

			return success;

		} catch (SQLiteException e) {
			return false;
		}
	}

	// Actualizar Data Local
	public Boolean updateHojaRuta(cl_hoja_ruta hr) {

		try {
			boolean success = false;
			if (hr != null) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
						ContentValues values = new ContentValues();						
						values.put("appEstadoSync", hr.getAppEstadoSync());
						values.put("appFechaSync", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
						
						success = db.update(tablaHojaRuta, values,"hrCodigo =" + hr.getHrCodigo() , null) > 0;

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}
			}
			return success;

		} catch (SQLiteException e) {
			return false;
		}
	}

	public Boolean updateHojaRutaDetalle(int pedCodigo, int hrCodigo){
		try {
			boolean success = false;
			if (pedCodigo > 0 && hrCodigo > 0) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
						String where = "pedCodigo =" + pedCodigo + " and hrCodigo =" + hrCodigo;
						ContentValues values = new ContentValues();						
						values.put("appEstadoSync", true);
						values.put("appFechaSync", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
						success = db.update(tablaHojaRutaDetalle, values, where , null) > 0;

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}
			}
			return success;

		} catch (SQLiteException e) {
			return false;
		}
	} 
	
	public Boolean updatePedido(int pedCodigo) {

		try {
			boolean success = false;
			if (pedCodigo > 0) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
						ContentValues values = new ContentValues();						
						values.put("appEstadoSync", true);
						values.put("appFechaSync", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
						success = db.update(tablaPedido, values,"pedCodigo =" + pedCodigo , null) > 0;

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}
			}
			return success;

		} catch (SQLiteException e) {
			return false;
		}
	}
	
	//Poblar DB Local: Obtener Data de Servicio 
	public Boolean getHojaRutaDeServicioSync() {

		try {
			// Obtener Usuario
			cl_user_entity user = getUserEntity();
			boolean success = false;

			// Obtener Hojas de Rutas del Servicio
			HttpClient Client = new DefaultHttpClient();
			String host = cl_util.ObtenerHost(mycontext);

			String uri = host
					+ "distribuidor/hojaruta/HojasDeRutasPorTransportista?dni="
					+ user.getTraNumDoc() + "&placa=" + user.getVehPlaca();
					//+ "&fecha=2015-04-29";

			HttpGet httpHr = new HttpGet(uri);

			httpHr.setHeader("content-type", "application/json");

			HttpResponse resp = Client.execute(httpHr);
			String respStr = EntityUtils.toString(resp.getEntity());

			if (!respStr.equals("null")) {

				JSONArray hojasRutasJs = new JSONArray(respStr);
				List<cl_hoja_ruta> hojasRutas = new ArrayList<cl_hoja_ruta>();

				for (int i = 0; i < hojasRutasJs.length(); i++) {
					JSONObject item = hojasRutasJs.getJSONObject(i);
					cl_hoja_ruta hojaRuta = new cl_hoja_ruta();
					
					hojaRuta.setHrCodigo(item.getInt("hrCodigo"));
					hojaRuta.setHrNumeroHoja(item.getString("hrNumeroHoja"));					
					hojaRuta.setVehplaca(item.getString("vehplaca"));
					hojaRuta.setHrKilometraje(null);
					//hojaRuta.setHrKilometrajeIni(item.getDouble("hrKilometrajeIni"));
					// hojaRuta.sethr(item.getString("hrNumeroHoja"));
					hojasRutas.add(hojaRuta);
				}

				success = insertHojaRuta(hojasRutas);
				if (success) {
					// llamar al servicio de hoja de ruta detalle e insertar en DB
										
					//success = getClientesHojaRutaSync(hojasRutas, host);
					//success = getHojaRutaDetalleSync(hojasRutas, host);
					
					success = getClientesHojaRutaSync(user.getTraNumDoc(), user.getVehPlaca(), host);
					success = getHojaRutaDetalleSync(user.getTraNumDoc(), user.getVehPlaca(), host);
					
					if (success) {
						// llamar al servicio de pedidos de hoja de ruta
						success = getPedidosHojaRutaSync(user.getTraNumDoc(), user.getVehPlaca(), host);
					}
				}
			}

			return success;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}
	public Boolean getHojaRutaDetalleSync(String dni, String placa, String host) {

		try {			
			List<cl_hoja_ruta_detalle> hrDetalles = new ArrayList<cl_hoja_ruta_detalle>();
			
				HttpClient Client = new DefaultHttpClient();
				String uri = host
						+ "distribuidor/hojaruta/HojaDeRutaDetalle?dni="
						+ dni + "&placa=" + placa;					

				HttpGet httpHr = new HttpGet(uri);
				httpHr.setHeader("content-type", "application/json");

				HttpResponse resp = Client.execute(httpHr);
				String respStr = EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("null")) {

					JSONArray HrDetalleJs = new JSONArray(respStr);					

					for (int i = 0; i < HrDetalleJs.length(); i++) {
						JSONObject item = HrDetalleJs.getJSONObject(i);
						cl_hoja_ruta_detalle detalle = new cl_hoja_ruta_detalle();

						detalle.setCliCodigo(item.getInt("cliCodigo"));
						detalle.setHrCodigo(item.getInt("hrCodigo"));
						detalle.setPedCodigo(item.getInt("pedCodigo"));
						hrDetalles.add(detalle);
					}
			}			
			if (!hrDetalles.isEmpty())
				insertHojaRutaDetalle(hrDetalles);

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}
	public Boolean getClientesHojaRutaSync(String dni, String placa, String host) {

		try {
				List<cl_cliente> hrClientes = new ArrayList<cl_cliente>();
				HttpClient Client = new DefaultHttpClient();	
				String uri = host
						+ "distribuidor/hojaruta/DestinosPorHojaDeRuta?dni="
						+ dni + "&placa=" + placa;

				HttpGet httpHr = new HttpGet(uri);
				httpHr.setHeader("content-type", "application/json");

				HttpResponse resp = Client.execute(httpHr);
				String respStr = EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("null")) {
					JSONArray DestinosHrJs = new JSONArray(respStr);

					for (int i = 0; i < DestinosHrJs.length(); i++) {
						JSONObject item = DestinosHrJs.getJSONObject(i);
						cl_cliente cliente = new cl_cliente();
						cliente.setCliCodigo(item.getInt("cliCodigo"));
						cliente.setCliNombre(item.getString("cliNombre"));						
						hrClientes.add(cliente);
					}
				}
			
			if (!hrClientes.isEmpty())
				insertHojaRutaClientes(hrClientes);

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}
	public Boolean getPedidosHojaRutaSync(String dni, String placa, String host) {

		try {			
			List<cl_hoja_ruta_pedidos> hrPedidos = new ArrayList<cl_hoja_ruta_pedidos>();			
			
				HttpClient Client = new DefaultHttpClient();				
				String uri = host
						+ "distribuidor/hojaruta/PedidosPorHojaDeRuta?dni="
						+ dni + "&placa=" + placa;
					
				HttpGet httpHr = new HttpGet(uri);
				httpHr.setHeader("content-type", "application/json");

				HttpResponse resp = Client.execute(httpHr);
				String respStr = EntityUtils.toString(resp.getEntity());

				if (!respStr.equals("null")) {

					JSONArray PedidosHrJs = new JSONArray(respStr);

					for (int i = 0; i < PedidosHrJs.length(); i++) {
						JSONObject item = PedidosHrJs.getJSONObject(i);
						cl_hoja_ruta_pedidos pedido = new cl_hoja_ruta_pedidos();
						
						pedido.setPedCodigo(item.getInt("pedCodigo"));
						pedido.setCliNombre(item.getString("cliNombre"));
						pedido.setCliCodigo(item.getInt("cliCodigo"));
						pedido.setHrNumeroHoja(item.getString("hrNumeroHoja"));						
						pedido.setPedNropedido(item.getString("pedNropedido"));
						
						pedido.setDesCodigo(item.getInt("desCodigo"));
						pedido.setPedIdDireccion(item.getInt("PedIdDireccion"));
						pedido.setDedNomVia(item.getString("dedNomVia"));
						pedido.setDesNombre(item.getString("desNombre"));
						
						pedido.setPedGuiasGen(item.getString("pedGuiasGen"));
				
						
						hrPedidos.add(pedido);
					}
				}
			
			if (!hrPedidos.isEmpty())
				insertHojaRutaPedidos(hrPedidos);

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	//LIQUIDAR
	public Boolean updLiquidarPedido(ArrayList<cl_pedido> pedidos,String estado) {

		try {
			boolean success = false;
			if (!pedidos.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {

					for (cl_pedido pedido : pedidos) {

						ContentValues values = new ContentValues();
						values.put("pedEstado", estado);

						success = db.update(tablaPedido, values, "pedCodigo="+ pedido.getPedCodigo(), null) > 0;

						if (!success)
							break;
					}

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}
			return success;

		} catch (SQLiteException e) {
			return false;
		}

	}
	public Boolean updLiquidarHojaRutaDetalle(ArrayList<cl_pedido> pedidos,String motivo,int hrCodigo,String hrdCodTipLiq,String usrCodMod) {

		try {
			boolean success = false;
			if (!pedidos.isEmpty()) {

				SQLiteDatabase db = this.getWritableDatabase();
				db.beginTransaction();
				try {
					
					String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
					String hora=new SimpleDateFormat("HH:mm:ss").format(new Date());

					for (cl_pedido pedido : pedidos) {

						ContentValues values = new ContentValues();
						values.put("hrdCodMotivo", motivo);
						values.put("hrdCodTipLiq", hrdCodTipLiq);
						values.put("hrdHoraPreLiq", hora);
						values.put("hrdFecPreLiq", fecha);
						values.put("hrdHoraDes", hora);
						values.put("hrdFecDes", fecha);
						values.put("usrCodMod", usrCodMod);
						values.put("hdrFlgLiq", 1);
						
						success = db.update(tablaHojaRutaDetalle, values, "hrCodigo="+ hrCodigo+" and cliCodigo="+pedido.getCliCodigo()+" and pedCodigo="+pedido.getPedCodigo(), null) > 0;

						if (!success)
							break;
					}

					if (success)
						db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}
			return success;

		} catch (SQLiteException e) {
			return false;
		}

	}
	public Boolean verificarLiquidarHojaRuta(int hrCodigo){
		
		
        Boolean result=true;
		try {

			SQLiteDatabase db = this.getReadableDatabase();
			String query="select count(HD.hrCodigo) as NroPedidos,sum(case when P.pedEstado is null then 0 else 1 end) as NroLiquidado";
				   query+=" from tblHojaRutaDetalle HD ";
				   query+=" inner join tblCliente C on HD.cliCodigo=C.cliCodigo";
				   query+=" inner join tblPedido P on C.cliCodigo=P.cliCodigo and HD.pedCodigo=P.pedCodigo";
				   query+=" where HD.hrCodigo="+hrCodigo;
						   	
			Cursor cursor = db.rawQuery(query, null);
			int nroPedidos=0;
		    int nroLiquidados=0;
			if (cursor != null && cursor.moveToFirst()) {

				nroPedidos=cursor.getInt(cursor.getColumnIndex("NroPedidos"));
				nroLiquidados=cursor.getInt(cursor.getColumnIndex("NroLiquidado"));

				cursor.close();
			}
			db.close();
			
			if(nroPedidos==0 &&nroLiquidados==0)
			{
				result=false;
			}else if(nroPedidos!=nroLiquidados){
				result=false;
			}
			
			return result;
		} catch (SQLiteException e) {
			return false;
		}
		
	}
	public int updLiquidarHojaRuta(int hrCodigo){
		int result=0;
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("hrFlgLiq", 1);
		
		result=db.update(tablaHojaRuta, values,"hrCodigo="+hrCodigo , null);
		db.close();
		
		return result;
	}
	public ArrayList<cl_motivos> getMotivosNoEntrega() {
		try {

			ArrayList<cl_motivos> lista = new ArrayList<cl_motivos>();

			String tsql = "SELECT * FROM " + tablaMotivoNoEntrega+ " where CodReg!='00038'";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			if (cursor.moveToFirst()) {
				do {
					
					cl_motivos item = new cl_motivos();
					
					item.setCodReg(cursor.getString(cursor.getColumnIndex("CodReg")));
					item.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));
					
					lista.add(item);
				} while (cursor.moveToNext());
			}
			db.close();
			return lista;
			
		} catch (SQLiteException e) {
			return null;
		}

	}
		
	//CONSULTAS
	public float[] getCumplimientoDelDia() {
		try {

			float[] lista = new float[2];

			/*
			String tsql = "select round(sum(case when pedEstado is not null then 1.00 else 0 end) *100/count(pedCodigo),2) as 'Cumplido',";
					tsql+=" round(sum(case when pedEstado is null then 1.00 else 0 end)*100/count(pedCodigo),2) as 'Pendiente'";
					tsql+=" from " + tablaPedido;
			*/
			String tsql = "select sum(case when pedEstado is not null then 1 else 0 end)  as 'Cumplido',";
			tsql+=" sum(case when pedEstado is null then 1 else 0 end) as 'Pendiente'";
			tsql+=" from " + tablaPedido;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			
			if (cursor != null && cursor.moveToFirst()) {

				
				lista[0]=cursor.getFloat(cursor.getColumnIndex("Cumplido"));
				lista[1]=cursor.getFloat(cursor.getColumnIndex("Pendiente"));
				
				cursor.close();
			}
			db.close();
			return lista;
			
		} catch (SQLiteException e) {
			return null;
		}
		
		

	}
	public float[] getEstadoEntregaDelDia() {
		try {

			float[] lista = new float[3];
			/*
			String tsql = "select round(sum(case when pedEstado ='00008' then 1.00 else 0 end) *100/count(pedCodigo),2) as 'Total'";
					tsql+=" ,round(sum(case when pedEstado ='00009' then 1.00 else 0 end) *100/count(pedCodigo),2) as 'Parcial'";
					tsql+=" ,round(sum(case when pedEstado ='00010' then 1.00 else 0 end) *100/count(pedCodigo),2) as 'NoEntregado'";
					tsql+=" from " + tablaPedido;
					tsql+=" where pedEstado is not null";
			*/
			String tsql = "select sum(case when pedEstado ='00008' then 1 else 0 end)  as 'Total'";
			tsql+=" ,sum(case when pedEstado ='00009' then 1 else 0 end) as 'Parcial'";
			tsql+=" ,sum(case when pedEstado ='00010' then 1 else 0 end) as 'NoEntregado'";
			tsql+=" from " + tablaPedido;
			tsql+=" where pedEstado is not null";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			
			if (cursor != null && cursor.moveToFirst()) {

				
				lista[0]=cursor.getFloat(cursor.getColumnIndex("Total"));
				lista[1]=cursor.getFloat(cursor.getColumnIndex("Parcial"));
				lista[2]=cursor.getFloat(cursor.getColumnIndex("NoEntregado"));
				
				cursor.close();
			}
			db.close();
			return lista;
			
		} catch (SQLiteException e) {
			return null;
		}

	}
	
	//Sincronizacion remota
	public Boolean HojaRutaSync(cl_hoja_ruta hr){
			try {
				boolean success = false;	
				HttpClient Client = new DefaultHttpClient();
				String host = cl_util.ObtenerHost(mycontext);
				String fecMod =new SimpleDateFormat("dd/MM/yyyy").format(new Date());

				String uri = host
						+ "distribuidor/Sync/HojaRutaSync?hrCodigo="+ hr.getHrCodigo()
						+ "&hrKilometrajeIni=" + 1 //hr.getHrKilometrajeIni()
						+ "&usrCodMod=" + hr.getUsrCodMod()						
						+ "&FecMod="+ hr.getFecMod().toString();
						
				HttpGet httpHr = new HttpGet(uri);
				httpHr.setHeader("content-type", "application/json");

				HttpResponse resp = Client.execute(httpHr);
				String respStr = EntityUtils.toString(resp.getEntity());			
				
				if (!respStr.equals("null")) {				
					
				//Actualizar tblHojaRuta
					if(resp.getStatusLine().getStatusCode() == 200){
						hr.setAppEstadoSync(true);
						//hr.setAppFechaSync((java.sql.Date) new Date());
						success = updateHojaRuta(hr);
						
					}	
				}

				return success;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

	public ArrayList<cl_hoja_ruta> getHojaRutaIniciadaListarSync() {
		try {

			ArrayList<cl_hoja_ruta> hojaRutas = new ArrayList<cl_hoja_ruta>();

			String tsql =" SELECT a.hrCodigo, a.hrNumeroHoja, count(1) as total,count(p.appEstadoSync) as sync ";
			tsql +=" FROM  tblHojaRuta a ";
			tsql +=" inner join tblHojaRutaDetalle b on a.hrCodigo = b.hrCodigo ";
			tsql +=" left join tblPedido p on b.pedCodigo = p.pedCodigo and  p.appEstadoSync=1 ";
			tsql +=" WHERE a.hrKilometraje > 0 and a.hrFlgLiq = 1 ";
			tsql +=" group by a.hrCodigo, a.hrNumeroHoja ";
			
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			if (cursor.moveToFirst()) {
				do {
					
					cl_hoja_ruta hojaRuta = new cl_hoja_ruta();
					hojaRuta.setHrCodigo(cursor.getInt(cursor
							.getColumnIndex("hrCodigo")));
					hojaRuta.setHrNumeroHoja(cursor.getString(cursor
							.getColumnIndex("hrNumeroHoja")));
					hojaRuta.setTotal(cursor.getInt(cursor
							.getColumnIndex("total")));
					hojaRuta.setSync(cursor.getInt(cursor
							.getColumnIndex("sync")));
					
					hojaRutas.add(hojaRuta);
				} while (cursor.moveToNext());
			}
			db.close();
			return hojaRutas;
			
		} catch (SQLiteException e) {
			return null;
		}

	}

	public Boolean PedidoSync(int hr, int cliCodigo){
		boolean success = false;
		ArrayList<cl_pedido> listPed = new ArrayList<cl_pedido>();
		String host = cl_util.ObtenerHost(mycontext);
		int totalReg = 0;
		int nroSuccess = 0;
		//Obtener la lista de pedidos por hr		
		listPed = cliCodigo == 0 ? getPedidoLiquidadoSync(hr): getPedidoSyncAuto(hr,cliCodigo);
		totalReg = listPed.size();
				
		try {
			
			for (cl_pedido ped : listPed) {					
			HttpClient Client = new DefaultHttpClient();		

			String uri = host					
					 + "distribuidor/Sync/PedidoSync?pedCodigo="+ ped.getPedCodigo()
					+ "&usrCodMod=" + ped.getUsrCodMod()
					+ "&pedEstado="+ ped.getPedEstado()
					+ "&hrdCodTipLiq="+ ped.getHrdCodTipLiq()
					+ "&hrdCodMotivo="+ ped.getHrdCodMotivo()
					
					+ "&hrdHoraParti=" + ped.getHrdHoraParti()
					+ "&hrdHoraLlega=" + ped.getHrdHoraLlega()					
					+ "&hrdHoraDes="+ ped.getHrdHoraDes()
					+ "&hrdHoraPreLiq="+ ped.getHrdHoraPreLiq()
					+ "&fecMod="+ ped.getFecMod();
				
					
			HttpGet httpHr = new HttpGet(uri);
			httpHr.setHeader("content-type", "application/json");

			HttpResponse resp = Client.execute(httpHr);
			String respStr = EntityUtils.toString(resp.getEntity());			
			
			if (!respStr.equals("null")) {				
				if(resp.getStatusLine().getStatusCode() == 200){
					nroSuccess +=1;
					//Actualizo estado Sync de: tblPedidoCab y tblHojaRutaDetalle 
					updatePedido(ped.getPedCodigo());
					updateHojaRutaDetalle(ped.getPedCodigo(), hr);
					//Debe retornar True si todos los pedidos de la HR fueron sincronizados
					if(totalReg == nroSuccess)
					success = true; 
					
				}			
			}
		}
			return success; //Retornamos true para actualizar la tblHojaruta
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	private ArrayList<cl_pedido> getPedidoLiquidadoSync(int hr){
		try {

			ArrayList<cl_pedido> obj = new ArrayList<cl_pedido>();

			String   tsql = " SELECT p.pedCodigo, a.usrCodMod, b.hrdCodTipLiq, b.hrdCodMotivo, p.pedEstado, " ;
					tsql += " b.hrdHoraParti, b.hrdHoraLlega, b.hrdHoraDes, b.hrdHoraPreLiq, hrdFecPreLiq ";
					tsql += " FROM  tblHojaRuta a ";
					tsql += " INNER JOIN tblHojaRutaDetalle b on a.hrCodigo = b.hrCodigo "; 
					tsql += " INNER JOIN tblPedido p on b.pedCodigo = p.pedCodigo and  b.hdrFlgLiq =1 and p.appEstadoSync=0 ";
					tsql += " WHERE a.hrCodigo = " + hr;
					tsql += " and  a.hrFlgLiq = 1";
			
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			if (cursor.moveToFirst()) {
				do {
					
					cl_pedido ped = new cl_pedido();
					ped.setPedCodigo(cursor.getInt(cursor.getColumnIndex("pedCodigo")));
					ped.setUsrCodMod(cursor.getString(cursor.getColumnIndex("usrCodMod")));
					ped.setHrdCodTipLiq(cursor.getString(cursor.getColumnIndex("hrdCodTipLiq")));
					ped.setHrdCodMotivo(cursor.getString(cursor.getColumnIndex("hrdCodMotivo")));
					ped.setPedEstado(cursor.getString(cursor.getColumnIndex("pedEstado")));
					
					ped.setHrdHoraParti(cursor.getString(cursor.getColumnIndex("hrdHoraParti")));
					ped.setHrdHoraLlega(cursor.getString(cursor.getColumnIndex("hrdHoraLlega")));
					ped.setHrdHoraDes(cursor.getString(cursor.getColumnIndex("hrdHoraDes")));
					ped.setHrdHoraPreLiq(cursor.getString(cursor.getColumnIndex("hrdHoraPreLiq")));
					ped.setFecMod(cursor.getString(cursor.getColumnIndex("hrdFecPreLiq")));
					ped.setHrdFecPreLiq(cursor.getString(cursor.getColumnIndex("hrdFecPreLiq")));
					
					obj.add(ped);
				} while (cursor.moveToNext());
			}
			db.close();
			return obj;
			
		} catch (SQLiteException e) {
			return null;
		}
	}
	
	private ArrayList<cl_pedido> getPedidoSyncAuto(int hr, int cliCodigo){
		try {

			ArrayList<cl_pedido> obj = new ArrayList<cl_pedido>();

			String   tsql = " SELECT p.pedCodigo, a.usrCodMod, b.hrdCodTipLiq, b.hrdCodMotivo, p.pedEstado, " ;
					tsql += " b.hrdHoraParti, b.hrdHoraLlega, b.hrdHoraDes, b.hrdHoraPreLiq, hrdFecPreLiq ";
					tsql += " FROM  tblHojaRuta a ";
					tsql += " INNER JOIN tblHojaRutaDetalle b on a.hrCodigo = b.hrCodigo "; 
					tsql += " INNER JOIN tblPedido p on b.pedCodigo = p.pedCodigo and  b.hdrFlgLiq =1 and p.appEstadoSync=0 ";
					tsql += " WHERE a.hrCodigo = " + hr;
					tsql += " and  b.cliCodigo =" + cliCodigo ;
			
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(tsql, null);
			if (cursor.moveToFirst()) {
				do {
					
					cl_pedido ped = new cl_pedido();
					ped.setPedCodigo(cursor.getInt(cursor.getColumnIndex("pedCodigo")));
					ped.setUsrCodMod(cursor.getString(cursor.getColumnIndex("usrCodMod")));
					ped.setHrdCodTipLiq(cursor.getString(cursor.getColumnIndex("hrdCodTipLiq")));
					ped.setHrdCodMotivo(cursor.getString(cursor.getColumnIndex("hrdCodMotivo")));
					ped.setPedEstado(cursor.getString(cursor.getColumnIndex("pedEstado")));
					
					ped.setHrdHoraParti(cursor.getString(cursor.getColumnIndex("hrdHoraParti")));
					ped.setHrdHoraLlega(cursor.getString(cursor.getColumnIndex("hrdHoraLlega")));
					ped.setHrdHoraDes(cursor.getString(cursor.getColumnIndex("hrdHoraDes")));
					ped.setHrdHoraPreLiq(cursor.getString(cursor.getColumnIndex("hrdHoraPreLiq")));
					ped.setFecMod(cursor.getString(cursor.getColumnIndex("hrdFecPreLiq")));
					ped.setHrdFecPreLiq(cursor.getString(cursor.getColumnIndex("hrdFecPreLiq")));
					
					obj.add(ped);
				} while (cursor.moveToNext());
			}
			db.close();
			return obj;
			
		} catch (SQLiteException e) {
			return null;
		}
	}

	//Supervisor
	public ArrayList<cl_avance> getAvances(String fecha, String nivel, String trans) {
		
		ArrayList<cl_avance> lista = new ArrayList<cl_avance>();
		String host = cl_util.ObtenerHost(mycontext);			
				
		try {	
				HttpClient Client = new DefaultHttpClient();
				String finalString = URLEncoder.encode(trans, "UTF-8");
				String uri = host					
						 + "distribuidor/Supervisor/SupAvance?fecha="+ fecha
						+ "&nivel=" + nivel
						+ "&trans="+ finalString;			
						
				HttpGet httpHr = new HttpGet(uri);
				httpHr.setHeader("content-type", "application/json");
	
				HttpResponse resp = Client.execute(httpHr);
				String respStr = EntityUtils.toString(resp.getEntity());			
				
				if (!respStr.equals("null")) {				
					if(resp.getStatusLine().getStatusCode() == 200){
						
						JSONArray js = new JSONArray(respStr);
						

						for (int i = 0; i < js.length(); i++) {
							JSONObject item = js.getJSONObject(i);
							cl_avance obj = new cl_avance();
							
							obj.setTransportista(item.getString("Transportista"));
							obj.setDocumentos(item.getInt("Documentos"));					
							obj.setAvance(item.getInt("Avance"));
							
							if(nivel == "1"){
								
								obj.setPlaneado(item.getString("Planeado")) ;
								obj.setEfectuado(item.getString("Efectuado")) ;
								obj.setHR(item.getString("HR")) ;
								obj.setDocumento(item.getString("Documento")) ;
								obj.setPedido(item.getString("Pedido")) ;
								obj.setCliente(item.getString("Cliente")) ;
								obj.setDestinatario(item.getString("Destinatario")) ;
								obj.setFlgLiquida(item.getInt("FlgLiquida")) ;
								obj.setPartida(item.getString("Partida")) ;
								obj.setLlegada(item.getString("Llegada")) ;
								obj.setDescargo(item.getString("Descargo")) ;
								obj.setLiquidado(item.getString("Liquidado")) ;
								obj.setCodMotivo(item.getString("CodMotivo")) ;
								obj.setMotivo(item.getString("Motivo")) ;
							}
													
							lista.add(obj);
						}						
					}		
				}										
			
			return lista; 
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	public ArrayList<cl_estadistica> getEstadisticas(String fecha, String nivel, String motivo) {
		
		ArrayList<cl_estadistica> lista = new ArrayList<cl_estadistica>();
		String host = cl_util.ObtenerHost(mycontext);			
				
		try {	
				HttpClient Client = new DefaultHttpClient();
				String finalString = URLEncoder.encode(motivo, "UTF-8");
				String uri = host					
						 + "distribuidor/Supervisor/SupEstadistica?fecha="+ fecha
						+ "&nivel=" + nivel
						+ "&motivo="+ finalString;			
						
				HttpGet httpHr = new HttpGet(uri);				
				httpHr.setHeader("content-type", "application/json");
	
				HttpResponse resp = Client.execute(httpHr);
				String respStr = EntityUtils.toString(resp.getEntity());			
				
				if (!respStr.equals("null")) {				
					if(resp.getStatusLine().getStatusCode() == 200){
						
						JSONArray js = new JSONArray(respStr);
						

						for (int i = 0; i < js.length(); i++) {
							JSONObject item = js.getJSONObject(i);
							cl_estadistica obj = new cl_estadistica();
							
							obj.setCodMotivo(item.getString("CodMotivo"));
							obj.setMotivo(item.getString("Motivo"));
							obj.setCantidad(item.getInt("Cantidad"));
							
							if(nivel == "1"){
																
								obj.setPlaneado(item.getString("Planeado"));
								obj.setEfectuado(item.getString("Efectuado"));
								obj.setTransportista(item.getString("Transportista"));
								obj.setHR(item.getString("HR"));
								obj.setDocumento(item.getString("Documento"));
								obj.setPedido(item.getString("Pedido"));
								obj.setCliente(item.getString("Cliente"));
								obj.setDestinatario(item.getString("Destinatario"));
								obj.setFlgLiquida(item.getInt("FlgLiquida"));
								obj.setPartida(item.getString("Partida"));
								obj.setLlegada(item.getString("Llegada"));
								obj.setDescargo(item.getString("Descargo"));
								obj.setLiquidado(item.getString("Liquidado"));
							}
													
							lista.add(obj);
						}						
					}		
				}										
			
			return lista; 
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

}
