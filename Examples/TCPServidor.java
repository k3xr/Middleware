import java.net.*;
import java.io.*;

public class TCPServidor {

	Socket socket;
	InputStream recvStream;
	OutputStream sendStream;
	String peticion;
	String respuesta;
	
	TCPServidor (Socket s) throws IOException, UnknownHostException {
		socket = s;
		recvStream = socket.getInputStream();
		sendStream = socket.getOutputStream();
	}
	
	void getPeticion(){
		try {
			int tamData;
			while ((tamData = recvStream.available())==0);
			byte[] recvBuff = new byte[tamData];
			recvStream.read(recvBuff, 0, tamData);
			peticion = new String(recvBuff, 0, tamData);
		}
		catch (IOException ex){
			System.err.println("IOException en getPeticion()");
		}
	}
	
	void procesar(){
		if(peticion.equals("Hola caracola")) {
			respuesta = "Hola cliente";
		}
		else
			respuesta = "Hola desconocido";
	}
	
	void enviarRespuesta(){
		try{
			byte[] sendBuff = new byte[respuesta.length()];
			sendBuff = respuesta.getBytes();
			sendStream.write(sendBuff, 0, sendBuff.length);
		}
		catch (IOException ex){
			System.err.println("IOException en enviarRespuesta");
		}
	}
	
	void cerrar(){
		try{
			recvStream.close();
			sendStream.close();
			socket.close();
		}
		catch (IOException ex){
			System.err.println("IOException en cerrar");
		}
	}
	
	public static void main(String[] args) throws IOException{
		final int puerto = 4322;
		ServerSocket socketServidor = new ServerSocket(puerto);
		while(true){
			TCPServidor servidor = new TCPServidor(socketServidor.accept());
			servidor.getPeticion();
			servidor.procesar();
			servidor.enviarRespuesta();
			servidor.cerrar();
		}

	}

}
