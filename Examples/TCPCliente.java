import java.net.*;
import java.io.*;

public class TCPCliente {

	Socket socket;
	OutputStream sendStream;
	InputStream recvStream;
	String peticion;
	String respuesta;
	
	public TCPCliente(String servidor, int puerto) throws UnknownHostException, IOException {
		socket = new Socket(servidor, puerto);
		sendStream = socket.getOutputStream();
		recvStream = socket.getInputStream();
	}
	
	void prepararPeticion() {
		peticion = "Hola caracola";
	}
	
	void enviarPeticion() {
		try {
			byte[] sendBuff = new byte[peticion.length()];
			sendBuff = peticion.getBytes();
			sendStream.write(sendBuff,0,sendBuff.length);
		}
		catch (IOException ex){
			System.err.println("IOException en enviarRespuesta()");
		}
	}
	
	void getRespuesta(){
		try{
			int tamData;
			while ((tamData = recvStream.available())==0);
			byte[] recvBuff = new byte[tamData];
			recvStream.read(recvBuff,0,tamData);
			respuesta = new String(recvBuff, 0, tamData);
		}
		catch (IOException ex){
			System.err.println("IOException en getRespuesta()");
		}
	}
	
	void usarRespuesta(){
		System.out.println("El servidor me ha contestado: " + respuesta);
	}
	
	void cerrar(){
		try{
			sendStream.close();
			recvStream.close();
			socket.close();
		}
		catch (IOException ex){
			System.err.println("IOException en cerrar()");
		}
	}
	
	public static void main(String [] args) throws IOException {
		final int puertoServidor = 4322;
		final String nombreServidor = "localhost";
		TCPCliente cliente = new TCPCliente(nombreServidor, puertoServidor);
		cliente.prepararPeticion();
		cliente.enviarPeticion();
		cliente.getRespuesta();
		cliente.usarRespuesta();
		cliente.cerrar();
	}
}
