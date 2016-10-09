import java.io.Serializable;	
import java.rmi.RemoteException;	
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class ServidorImpl extends UnicastRemoteObject implements InterfazServidor, Serializable, MessageListener {	

	private BaseDatos DB;
	private static final String rutadb = "DBSocial.sqlite";
	private Connection conn;
	private static final long serialVersionUID = -8842791558575037427L;
	private InterfazCliente[] clientList;
	private String[] userList;
	private static int posicionCallbackActual = 0;

	public ServidorImpl() throws RemoteException {
		super();
		DB =new BaseDatos();
		conn = DB.conectarA(rutadb);
		clientList = new InterfazCliente[20];
		userList = new String[20];
		String[] topics = new String[2];
		topics[0] = "user1";
		topics[1] = "user2";
		suscribirseATopics(topics);
	}

	@Override
	public void registerForCallback(InterfazCliente callbackClientObject, String user) throws RemoteException {
		userList[posicionCallbackActual]=user;
		clientList[posicionCallbackActual] =callbackClientObject;
		posicionCallbackActual++;
	}

	@Override
	public void unregisterForCallback(InterfazCliente callbackClientObject, String user) throws RemoteException {
		for(int i=0; i<userList.length;i++){
			if(userList[i] != null && userList[i].equals(user)){
				userList[i] = null;
				clientList[i] = null;
			}
		}
	}

	/**
	 * Comprueba si el usuario con nick, nick y password, passwd se encuentra en la base de datos
	 * @param str = nombre de usuario
	 * @param conn conexión con la base de datos
	 * @return True= usuario con nick = nick existe.
	 */
	public boolean comprobarDatos(String id, String passwd) throws RemoteException{
		//Obtenemos la conexion con la base de datos
		Usuario usr = DB.getUsuario(id, conn);
		if(usr == null){
			return false;
		}
		else{
			return(usr.getPassword().equals(passwd));
		}
	}

	@Override
	public boolean nuevoUsuario(String id, String passwd, String nombre) throws RemoteException {
		Usuario nuevoUsuario = new Usuario(id, passwd, "", nombre, 0, "");
		return(DB.resgistrarUsuario(nuevoUsuario, conn));
	}

	@Override
	public boolean modificarNombre(String id, String nuevoNombre)throws RemoteException {
		return DB.modificarAtri(id, nuevoNombre, "Nombre", conn);
	}

	@Override
	public boolean modificarPrivacidad(String id)throws RemoteException {
		return DB.modificaPrivi(id, conn);
	}

	@Override
	public boolean modificarPAssword(String id, String newPassword)	throws RemoteException {
		return DB.modificarAtri(id, newPassword, "Pass", conn);
	}

	@Override
	public Usuario[] buscarUsuarios(String cadena) throws RemoteException {
		return DB.buscaUsuarios(cadena, conn);
	}

	@Override
	public boolean notificarAgregarAmigo(String id, String idAmigo)	throws RemoteException {
		for(int j=0;j<userList.length; j++){
			if(userList[j] == null){
				continue;
			}
			if(userList[j].equals(idAmigo)){
				System.out.println("notifico amistad: "+ userList[j]);
				clientList[j].notificarNuevoAmigo(id);
			}
		}
		//return DB.addAmigo(id, idAmigo, conn);
		return true;
	}
	
	@Override
	public String[] dameAmigos(String id) throws RemoteException {
		return DB.devuelveAmigos(id, conn);
	}

	@Override
	public String[] dameMuro(int numero, String id) throws RemoteException {
		return DB.extraePublicaciones(numero, id, conn);
	}

	@Override
	public Usuario dameUser(String id) throws RemoteException {
		return DB.getUsuario(id, conn);
	}

	@Override
	public boolean agregarAmigo(String id, String idAmigo)
			throws RemoteException {
		return DB.addAmigo(id, idAmigo, conn);
	}

	public void onMessage(Message message) {
		TextMessage msg = null;
		String id = "";
		String publicacion = "";
		try {
			if (message instanceof TextMessage) {
				msg = (TextMessage) message;
				System.out.println("Reading message: " +  msg.getText());

				int i =0;
				for(i=0; !(msg.getText().charAt(i)==(',')) ;i++){
					id = id + "" + msg.getText().charAt(i);
				}
				i++; // Salta la coma
				for(int j=i; !(msg.getText().charAt(j)==('*')) ;j++){
					publicacion = publicacion + "" + msg.getText().charAt(j);
				}				

			} else {
				System.out.println("Message of wrong type: " +message.getClass().getName());
			}
		} catch (JMSException e) {
			System.out.println("JMSException in onMessage(): " + e.toString());
		} catch (Throwable t) {
			System.out.println("Exception in onMessage():" + t.getMessage());
		}

		DB.publicar(id, publicacion, conn);
	}

	public void suscribirseATopics(String[] topics){

		for(int i=0; i<topics.length;i++){

			String                  topicName = topics[i]; // provisional
			Context                 jndiContext = null;
			TopicConnectionFactory  topicConnectionFactory = null;
			TopicConnection         topicConnection = null;
			TopicSession            topicSession = null;
			Topic                   topic = null;
			TopicSubscriber         topicSubscriber = null;
			MessageListener         messageListener = null;

			System.out.println("Topic name is " + topicName);

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
				System.out.println("Exception occurred: " +e.toString());
			}
		}
	}

}