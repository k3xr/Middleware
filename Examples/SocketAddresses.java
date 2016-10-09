import java.net.*;
import java.io.*;

public class SocketAddresses {

	public static void main(String[] args) throws UnknownHostException {
		InetAddress local = InetAddress.getLocalHost();
		int puerto = 65000;
		InetSocketAddress socketAddress = new InetSocketAddress(local, puerto);
		System.out.println(socketAddress);
	}
}
