import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ClienteImpl extends UnicastRemoteObject implements InterfazCliente{

	private static final long serialVersionUID = -2229824783620700770L;
	String loggedAs = null;
	private static InterfazServidor stub;
	private static String rmiUrl = "//localhost/RED";
	GUI interfazGrafica;

	public ClienteImpl() throws RemoteException{
		super();
		this.loggedAs = null;
		try {
			stub = (InterfazServidor) Naming.lookup(rmiUrl);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}	
		interfazGrafica = new GUI(this);
	}

	public void notificar(String mensaje, String user) throws RemoteException {
		interfazGrafica.visibleVentanaPpal(mensaje, user);
	}
	
	public void notificarNuevoAmigo(String user)throws RemoteException{
		interfazGrafica.NuevaSolicitud(user);
	}

	public boolean login(String id, String passwd){
		try {
			if(stub.comprobarDatos(id, passwd)){
				loggedAs = id;
				stub.registerForCallback(this, id);
				return true;
			}
			else{
				return false;
			}

		} catch (RemoteException e) {	
			e.printStackTrace();	
		}	
		return false;
	}

	/*
	 * deuelve: 1 - password incorrecto
	 * 			2 - nombre incorrecto, password ok
	 * 			3 - id incorrecto, password ok, nombre ok
	 * 			4 - error, password ok, nombre ok, id ok
	 * 			0 - Datos correctos
	 */
	public int registrarUsuario(String id, String password, String nombre){

		// password tiene que tener mas de 5 caracteres
		if (password == null || password.length() < 5){
			return 1;
		}

		// nombre e id tienen que tener mas de 3 caracteres
		if (nombre == null || nombre.length() < 3){
			return 2;
		}	

		if (id == null){
			return 3;
		}

		// id no tiene que contener espacios ni estar ya en la bd
		String ident = id.toLowerCase();
		ident.replaceAll(" ", "");

		if (ident.length() < 3){
			return 3;
		}
		try {
			if(stub.nuevoUsuario(ident,  password,  nombre)){
				loggedAs = ident;
				return 0;

			}
			return 4;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 4;
	}

	public void logout(){
		loggedAs = null;
		try {
			stub.unregisterForCallback(this, interfazGrafica.getUser());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void publicar(String id, String publicacion){
		try {
			stub.nuevaPublicacion(id, publicacion);
		} catch (RemoteException e) {
			System.out.println("Error al publicar");
			e.printStackTrace();
		}
	}

	public void agregarAmigo(String id, String idAmigo){
		try {
			stub.agregarAmigo(id, idAmigo);
		} catch (RemoteException e) {
			System.out.println("Error al publicar");
			e.printStackTrace();
		}
	}
	
	public void notificarAgregarAmigo(String id, String idAmigo){
		try {
			stub.notificarAgregarAmigo(id, idAmigo);
		} catch (RemoteException e) {
			System.out.println("Error al publicar");
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve un array de String con pares fechas impares mensajes, saca tantos mensajes como indique numero del usuario id.
	 * @param numero : numero de mensajes a extraer
	 * @param id	 : usuario del que se quieren extraer
	 * @return
	 */
	public String[] sacarPublicaciones(int numero, String id){
		try {
			return stub.dameMuro(numero, id);
		} catch (RemoteException e) {
			System.out.println("Error al publicar");
			e.printStackTrace();
		}
		System.out.println("Error al publicar");
		return null;
	}

	public Usuario[] buscarUsers(String cadena){
		try {
			return stub.buscarUsuarios(cadena);
		} catch (RemoteException e) {
			System.out.println("Error al buscar usuarios");
			e.printStackTrace();
		}
		System.out.println("Error al buscar");
		return null;
	}

	public void modificarAtributo(String id, String new_value, String atributo){
		if(atributo == "nombre"){
			try {
				stub.modificarNombre(id, new_value);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if(atributo == "password"){
			try {
				stub.modificarPAssword(id, new_value);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if(atributo == "privacidad"){
			try {
				stub.modificarPrivacidad(id);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void cambioPrivi(String id){
		try {
			stub.modificarPrivacidad(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Usuario dameUser(String id){
		try {
			return stub.dameUser(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

}
