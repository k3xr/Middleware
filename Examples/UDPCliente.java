import java.net.*;
import java.io.*;

public class UDPCliente {

	final int tamBuff = 12;
	DatagramSocket socket;
	String peticion;
	String respuesta;
	InetAddress dirServidor;
	int puertoServidor;
	
	public UDPCliente(DatagramSocket s, String servidor, int puerto) throws UnknownHostException{
		this.socket = s;
		this.dirServidor = InetAddress.getByName(servidor);
		this.puertoServidor = puerto;
	}
	
	public void prepararPeticion(){
		this.peticion = "Tienes hora?";
	}
	
	public void enviarPeticion(){
		try{
			byte[] sendBuff = new byte[tamBuff];
			sendBuff = peticion.getBytes();
			DatagramPacket datagrama = new DatagramPacket(sendBuff, 
					                                       sendBuff.length,
					                                       this.dirServidor,
					                                       this.puertoServidor);
			socket.send(datagrama);
		}
		catch (IOException ex){
			System.err.println("IOException en enviarPeticion()");
		}
	}
	
	public void getRespuesta(){
		try{
			byte[] recvBuff = new byte[tamBuff];
			DatagramPacket datagrama = new DatagramPacket(recvBuff, tamBuff);
			socket.receive(datagrama);
			recvBuff = datagrama.getData();
			respuesta = new String(recvBuff, 0, recvBuff.length);
		}
		catch (IOException ex){
			System.err.println("IOException en getRespuesta()");
		}
	}
	
	public void usarRespuesta(){
		System.out.println("El servidor ha contestado: "+ respuesta);
	}
	
	public void cerrar(){
		socket.close();
	}
	
	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		final int puertoServidor = 5432;
		final String nombreServidor = "localhost";
		DatagramSocket sock = new DatagramSocket();
		UDPCliente cliente = new UDPCliente(sock, nombreServidor, puertoServidor);
		cliente.prepararPeticion();
		cliente.enviarPeticion();
		cliente.getRespuesta();
		cliente.usarRespuesta();
		cliente.cerrar();
	}
}
