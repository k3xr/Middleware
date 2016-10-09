import java.rmi.*;

public interface InterfazCliente extends Remote{
	
	public void notificar(String mensaje, String user)throws RemoteException;
	
	public void notificarNuevoAmigo(String user)throws RemoteException;
	
}
