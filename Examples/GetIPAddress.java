import java.net.*;
import java.io.*;

public class GetIPAddress {

	public static void main(String[] args) throws UnknownHostException {

		InetAddress mipc = InetAddress.getByName("fobos.ls.fi.upm.es");
		InetAddress local = InetAddress.getLocalHost();
		InetAddress dirIP = InetAddress.getByName("138.100.10.166");
		
		System.out.println(mipc);
		System.out.println(local);
		System.out.println(dirIP);

		System.out.println(mipc.getHostAddress());
		System.out.println(local.getHostName());
	}

}
