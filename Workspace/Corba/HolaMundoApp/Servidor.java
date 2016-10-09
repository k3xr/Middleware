package HolaMundoApp;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Servidor {
	public static void main(String[] args){
		try{
			//Crear e inicializar un objeto ORB
			ORB orb=ORB.init(args, null);
			
			//Obtener una referencia al POA (RootPOA) y activar POAManager
			POA rootpoa= POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			//Instanciar el objeto servant
			HolaImpl hola = new HolaImpl();
			
			//Obtener la referencia de objeto asociada al servant y convertirla en una ref. CORBA
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(hola);
			Hola href = HolaHelper.narrow(ref);
			
			//Obtener el contexto de nombrado raiz
			//NameService está definido para todos los ORB CORBA
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			//Se utiliza NamingContextExt, que es parte de la especificacion de 
			//Servicio de nombrado Interoperable (INS)
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			//Se registra el servant en el servicio de nobres,
			//enlazando la referencia del objeto con el nombre
			String nombre = "Hola";
			NameComponent[] path = ncRef.to_name(nombre);
			ncRef.rebind(path, href);
			
			//Y se pone el ORB en marcha, esperando las invocaciones de los clientes
			orb.run();
		} catch (Exception e){
			e.printStackTrace();
		}
		 
	}
}
