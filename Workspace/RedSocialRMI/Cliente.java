import java.net.MalformedURLException;	
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Cliente {

	public static void main(String args[]) {
		
		String rmiUrl = "rmi://localhost/callback";

		try {			
			ClienteImpl c = new ClienteImpl();
			try {
				Naming.rebind(rmiUrl, c);
				Registry registry= LocateRegistry.getRegistry(1099);
				registry.list( );
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}