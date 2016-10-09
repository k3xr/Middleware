/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Practica implements MessageListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JMSException {
           
        try{
        //InitialContext Creado sin argumentos
        //Utiliza servidor Glassfish local
        //Si es remoto, arrancar la aplicacion con las opciones
        //-Dorg.omg.CORBA.ORBInitialHost=dirIP
        //-Dorg.omg.CORBA.ORBInitialHost=port
        Context jndiContext = new InitialContext();
        System.out.println("Contexto creado");
        
        ConnectionFactory cf = (ConnectionFactory)jndiContext.lookup("jms/Factory-Connection-Pool");
        
        System.out.println("CF accedida");
        
        Connection conexion = cf.createConnection();
        Session sesion = conexion.createSession();
        
        /*Esta parte en las que tendremos que hacer a nuestra manera*/
        
        //Obtener el Topic
        Topic usuario1 = (Topic)jndiContext.lookup("jms/A1");
        
        //Cambia esto por la linea que va acontinuacion ->sesion.createProducer(usuario1);
        MessageProducer productor = sesion.createProducer(usuario1);
        //TextMessage msg = sesion.createTextMessage("hola");
        MapMessage msg = sesion.createMapMessage();
        msg.setString("mensaje", "hola");
        msg.setObject("objeto", null);
        
        /*Fin de la parte que tendremos que hacer a nuestra manera*/
        
        Session sesionReceptor = conexion.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumidor = sesionReceptor.createConsumer(usuario1);
        consumidor.setMessageListener(new Practica());
        conexion.start();
        
        }catch (NamingException ex){
            Logger.getLogger(Practica.class.getName()).log(Level.SEVERE,null, ex);
        }
}

    @Override
    public void onMessage(Message msg1) {
        MapMessage msg2 = (MapMessage)message;
        System.out.println(msg2.getString("Mensaje"));
    }
}
