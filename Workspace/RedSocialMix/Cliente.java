import java.net.MalformedURLException;	
import java.rmi.*;


public class Cliente {

	public static void main(String args[]) {
		
		String rmiUrl = "rmi://localhost/callback";
		
		try {			
			ClienteImpl c = new ClienteImpl();
			try {
				Naming.rebind(rmiUrl, c);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}