
import java.rmi.Remote;	
import java.rmi.RemoteException;	


public interface InterfazServidor extends Remote {

	public boolean comprobarDatos(String id, String passwd)throws RemoteException;
	
	public boolean nuevoUsuario(String id, String passwd, String nombre)throws RemoteException;
	
	public boolean modificarNombre(String id, String nuevoNombre)throws RemoteException;
	
	public boolean modificarPrivacidad(String id)throws RemoteException;
	
	public boolean modificarPAssword(String id, String newPassword)throws RemoteException;
	
	public boolean agregarAmigo(String id, String idAmigo)throws RemoteException;
	
	public Usuario[] buscarUsuarios(String cadena)throws RemoteException;
	
	public void registerForCallback(InterfazCliente callbackClientObject, String user) throws RemoteException;
	
	public void unregisterForCallback(InterfazCliente callbackClientObject, String user) throws RemoteException;
	
	public String[] dameMuro(int numero, String id)throws RemoteException;

	public Usuario dameUser(String id)throws RemoteException;

	public boolean notificarAgregarAmigo(String id, String idAmigo)throws RemoteException;
	
	public String[] dameAmigos(String id) throws RemoteException;
}