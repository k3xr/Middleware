import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Servidor{	

	public static void main(String[] args) {	

		try {
			//Registry registry = LocateRegistry.getRegistry();
			//registry.bind("RED", new SaludarImpl());
			ServidorImpl sa = new ServidorImpl();
			Naming.rebind("RED", sa); 
			startRegistry();
		} catch (RemoteException e) {	
			e.printStackTrace();	
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 	

	}	

	private static void startRegistry()throws RemoteException{
		try {
			Registry registry= LocateRegistry.getRegistry(1099);
			registry.list( );
		} catch (RemoteException e) {
		}

	}
}