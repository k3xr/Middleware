import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class UDPServidor {

	final int tamBuff = 12;
	DatagramSocket socket;
	String peticion;
	String respuesta;
	InetAddress dirCliente;
	int puertoCliente;
	
	public UDPServidor(DatagramSocket s){
		this.socket = s;
	}

	public void getPeticion(){
		try{
			byte[] recvBuff = new byte[tamBuff];
			DatagramPacket datagrama = new DatagramPacket(recvBuff, tamBuff);
			socket.receive(datagrama);
			recvBuff = datagrama.getData();
			peticion = new String(recvBuff, 0, recvBuff.length);
			this.dirCliente = datagrama.getAddress();
			this.puertoCliente = datagrama.getPort();
		}
		catch (IOException ex){
			System.err.println("IOException en getPeticion()");
		}
	}

	public void procesarPeticion(){
		if(peticion.equals("Tienes hora?")){
			Calendar calendario = GregorianCalendar.getInstance();
			respuesta = "Son las " + calendario.get(Calendar.HOUR_OF_DAY) + ":" +
									 calendario.get(Calendar.MINUTE) + " exactamente...";
		}
		else
			respuesta = "Mmm, no te he entendido";
	}
	
	public void enviarRespuesta(){
		try{
			byte[] sendBuff = new byte[tamBuff];
			sendBuff = respuesta.getBytes();
			DatagramPacket datagrama = new DatagramPacket(sendBuff, 
					                                       sendBuff.length,
					                                       this.dirCliente,
					                                       this.puertoCliente);
			socket.send(datagrama);
		}
		catch (IOException ex){
			System.err.println("IOException en enviarRespuesta()");
		}
	}
			
	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		final int puertoServidor = 5432;
		DatagramSocket sock = new DatagramSocket(puertoServidor);
		while(true){
			UDPServidor servidor = new UDPServidor(sock);
			servidor.getPeticion();
			servidor.procesarPeticion();
			servidor.enviarRespuesta();
		}
	}

}
