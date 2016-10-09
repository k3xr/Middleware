 import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class ClienteImpl extends UnicastRemoteObject implements InterfazCliente, MessageListener{

	private static final long serialVersionUID = -2229824783620700770L;
	String loggedAs = null;
	private static InterfazServidor stub;
	private static String rmiUrl = "rmi://localhost/RED";
	GUI interfazGrafica;
	
	
	TopicSession            topicSession = null;
	TopicPublisher			topicPublisher = null;
	Context                 jndiContext = null;
	TopicConnectionFactory  topicConnectionFactory = null;
	TopicConnection         topicConnection = null;
	Topic                   topic = null;

	public ClienteImpl() throws RemoteException{

		super();
		
		this.loggedAs = null;
		try {
			stub = (InterfazServidor) Naming.lookup(rmiUrl);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}	
		interfazGrafica = new GUI(this);
	}

	public void notificar(String mensaje, String user) throws RemoteException {
		interfazGrafica.visibleVentanaPpal(mensaje, user);
	}

	public void notificarNuevoAmigo(String user)throws RemoteException{
		interfazGrafica.NuevaSolicitud(user);
	}

	public boolean login(String id, String passwd){
		try {
			if(stub.comprobarDatos(id, passwd)){
				loggedAs = id;
				stub.registerForCallback(this, id);
				return true;
			}
			else{
				return false;
			}

		} catch (RemoteException e) {	
			e.printStackTrace();	
		}	
		return false;
	}

	/*
	 * deuelve: 1 - password incorrecto
	 * 			2 - nombre incorrecto, password ok
	 * 			3 - id incorrecto, password ok, nombre ok
	 * 			4 - error, password ok, nombre ok, id ok
	 * 			0 - Datos correctos
	 */
	public int registrarUsuario(String id, String password, String nombre){

		// password tiene que tener mas de 5 caracteres
		if (password == null || password.length() < 5){
			return 1;
		}

		// nombre e id tienen que tener mas de 3 caracteres
		if (nombre == null || nombre.length() < 3){
			return 2;
		}	

		if (id == null){
			return 3;
		}

		// id no tiene que contener espacios ni estar ya en la bd
		String ident = id.toLowerCase();
		ident.replaceAll(" ", "");

		if (ident.length() < 3){
			return 3;
		}
		try {
			if(stub.nuevoUsuario(ident,  password,  nombre)){
				loggedAs = ident;
				return 0;

			}
			return 4;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 4;
	}

	public void logout(){
		loggedAs = null;
		try {
			stub.unregisterForCallback(this, interfazGrafica.getUser());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void publicar(String id, String publicacion){
		
		TextMessage message;
		try {
			message = this.topicSession.createTextMessage();
			message.setText(id+","+ publicacion+"*");
			System.out.println("Publishing message: " + message.getText());
			topicPublisher.publish(message);
		} catch (JMSException e1) {
			e1.printStackTrace();
		}
	}

	public void agregarAmigo(String id, String idAmigo){
		try {
			stub.agregarAmigo(id, idAmigo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void notificarAgregarAmigo(String id, String idAmigo){
		try {
			stub.notificarAgregarAmigo(id, idAmigo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve un array de String con pares fechas impares mensajes, saca tantos mensajes como indique numero del usuario id.
	 * @param numero : numero de mensajes a extraer
	 * @param id	 : usuario del que se quieren extraer
	 * @return
	 */
	public String[] sacarPublicaciones(int numero, String id){
		try {
			return stub.dameMuro(numero, id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Usuario[] buscarUsers(String cadena){
		try {
			return stub.buscarUsuarios(cadena);
		} catch (RemoteException e) {
			System.out.println("Error al buscar usuarios");
			e.printStackTrace();
		}
		System.out.println("Error al buscar");
		return null;
	}

	public void modificarAtributo(String id, String new_value, String atributo){
		if(atributo == "nombre"){
			try {
				stub.modificarNombre(id, new_value);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if(atributo == "password"){
			try {
				stub.modificarPAssword(id, new_value);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if(atributo == "privacidad"){
			try {
				stub.modificarPrivacidad(id);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void cambioPrivi(String id){
		try {
			stub.modificarPrivacidad(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Usuario dameUser(String id){
		try {
			return stub.dameUser(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] dameAmigos(String id){
		try {
			return stub.dameAmigos(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	//Recibir mensajes de un topic suscrito
	public void onMessage(Message mensaje) {
		try {
			TextMessage mensajeTexto = (TextMessage) mensaje;
			String msn = mensajeTexto.getText();			
			System.out.println("mensaje recibido: "+msn);
			
			String user = "";
			String cadena = "";
			
			int i=0;
			
			for(i=0; !(msn.charAt(i)==(',')) ;i++){
				user = user + "" + msn.charAt(i);
			}
			i++;
			for(int j=i; !(msn.charAt(j)==('*')) ;j++){
				cadena = cadena + "" + msn.charAt(j);
			}
			try {
				notificar(cadena, user);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}
	public void SuscribirseATopics(String[] topics){

		for(int i=0; i<topics.length;i++){

			String           		topicName = topics [i];
			TopicSession            topicSession = null;
			TopicSubscriber			topicSubscriber = null;
			Context                 jndiContext = null;
			TopicConnectionFactory  topicConnectionFactory = null;
			TopicConnection         topicConnection = null;
			Topic                   topic = null;
			MessageListener         messageListener = null;

			System.out.println("conectado a  " + topicName);
			/* 
			 * Create a JNDI API InitialContext object if none exists
			 * yet.
			 */
			try {
				jndiContext = new InitialContext();
			} catch (NamingException e) {
				System.out.println("Could not create JNDI API " + "context: " + e.toString());
				e.printStackTrace();
				System.exit(1);
			}

			/* 
			 * Look up connection factory and topic.  If either does
			 * not exist, exit.
			 */
			try {
				topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
				topic = (Topic) jndiContext.lookup(topicName);
			} catch (NamingException e) {
				System.out.println("JNDI API lookup failed: " + e.toString());
				e.printStackTrace();
				System.exit(1);
			}


			try {
				topicConnection = topicConnectionFactory.createTopicConnection();
				topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
				topicSubscriber = topicSession.createSubscriber(topic);
				messageListener = this;
				topicSubscriber.setMessageListener(messageListener);
				topicConnection.start();
			} catch (JMSException e) {
				System.out.println("Exception occurred: " + e.toString());
			}
		}
	}

	public void registrarTopicPublicaciones(String id){

		String topicName = id;
		
		System.out.println("conectado a  " + topicName);
		/* 
		 * Create a JNDI API InitialContext object if none exists
		 * yet.
		 */
		try {
			jndiContext = new InitialContext();
		} catch (NamingException e) {
			System.out.println("Could not create JNDI API " + "context: " + e.toString());
			e.printStackTrace();
			System.exit(1);
		}

		/* 
		 * Look up connection factory and topic.  If either does
		 * not exist, exit.
		 */
		try {
			topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
			topic = (Topic) jndiContext.lookup(topicName);
		} catch (NamingException e) {
			System.out.println("JNDI API lookup failed: " + e.toString());
			e.printStackTrace();
			System.exit(1);
		}

		try {
			topicConnection = topicConnectionFactory.createTopicConnection();
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			topicPublisher = topicSession.createPublisher(topic);
			topicConnection.start();
		} catch (JMSException e) {
			System.out.println("Exception occurred: " + e.toString());
		}
	}

}
