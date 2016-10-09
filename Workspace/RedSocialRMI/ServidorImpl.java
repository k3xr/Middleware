import java.io.Serializable;	
import java.rmi.RemoteException;	
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;


public class ServidorImpl extends UnicastRemoteObject 
implements InterfazServidor, Serializable {	

	private BaseDatos DB;
	private static final String rutadb = "DBSocial.sqlite";
	private Connection conn;
	private static final long serialVersionUID = -8842791558575037427L;
	private InterfazCliente[] clientList;
	private String[] userList;
	private static int posicionCallbackActual = 0;



	public ServidorImpl() throws RemoteException {
		super();
		DB =new BaseDatos();
		conn = DB.conectarA(rutadb);
		clientList = new InterfazCliente[20];
		userList = new String[20];
	}

	@Override
	public void registerForCallback(InterfazCliente callbackClientObject, String user) throws RemoteException {
		userList[posicionCallbackActual]=user;
		clientList[posicionCallbackActual] =callbackClientObject;
		posicionCallbackActual++;
	}

	@Override
	public void unregisterForCallback(InterfazCliente callbackClientObject, String user) throws RemoteException {
		for(int i=0; i<userList.length;i++){
			if(userList[i] != null && userList[i].equals(user)){
				userList[i] = null;
				clientList[i] = null;
			}
		}
	}

	/**
	 * Comprueba si el usuario con nick, nick y password, passwd se encuentra en la base de datos
	 * @param str = nombre de usuario
	 * @param conn conexi�n con la base de datos
	 * @return True= usuario con nick = nick existe.
	 */
	public boolean comprobarDatos(String id, String passwd) throws RemoteException{
		//Obtenemos la conexion con la base de datos
		Usuario usr = DB.getUsuario(id, conn);
		if(usr == null){
			return false;
		}
		else{
			return(usr.getPassword().equals(passwd));
		}
	}

	@Override
	public boolean nuevoUsuario(String id, String passwd, String nombre) throws RemoteException {
		Usuario nuevoUsuario = new Usuario(id, passwd, "", nombre, 0, "");
		return(DB.resgistrarUsuario(nuevoUsuario, conn));
	}

	@Override
	public boolean nuevaPublicacion(String id, String publicacion) throws RemoteException {
		String listaAmigos = DB.devuelveAmigos(id, conn);
		String [] lista = listaAmigos.split(";");

		for(int i=0; i<lista.length; i++){
			for(int j=0;j<userList.length; j++){
				if(userList[j] == null){
					continue;
				}
				if(lista[i].equals(userList[j])){
					System.out.println("notifico a: "+ userList[j]);
					clientList[j].notificar(publicacion, id);
				}
			}
		}
		return DB.publicar(id, publicacion, conn);
	}

	@Override
	public boolean modificarNombre(String id, String nuevoNombre)throws RemoteException {
		return DB.modificarAtri(id, nuevoNombre, "Nombre", conn);
	}

	@Override
	public boolean modificarPrivacidad(String id)throws RemoteException {
		return DB.modificaPrivi(id, conn);
	}

	@Override
	public boolean modificarPAssword(String id, String newPassword)	throws RemoteException {
		return DB.modificarAtri(id, newPassword, "Pass", conn);
	}

	@Override
	public Usuario[] buscarUsuarios(String cadena) throws RemoteException {
		return DB.buscaUsuarios(cadena, conn);
	}

	@Override
	public boolean notificarAgregarAmigo(String id, String idAmigo)	throws RemoteException {
		for(int j=0;j<userList.length; j++){
			if(userList[j] == null){
				continue;
			}
			if(userList[j].equals(idAmigo)){
				System.out.println("notifico amistad: "+ userList[j]);
				clientList[j].notificarNuevoAmigo(id);
			}
		}
		//return DB.addAmigo(id, idAmigo, conn);
		return true;
	}

	@Override
	public String[] dameMuro(int numero, String id) throws RemoteException {
		return DB.extraePublicaciones(numero, id, conn);
	}

	@Override
	public Usuario dameUser(String id) throws RemoteException {
		return DB.getUsuario(id, conn);
	}

	@Override
	public boolean agregarAmigo(String id, String idAmigo)
			throws RemoteException {
		return DB.addAmigo(id, idAmigo, conn);
	}


}