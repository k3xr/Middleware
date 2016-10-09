package HolaMundoApp;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class Cliente {
	public static void main(String[] args){
		try {
			//Crear una insancia del ORB
			ORB orb = ORB.init(args, null);
			
			//Obtener contexto de nombreado y transformar referencia
			org.omg.CORBA.Object objRef;
			objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			//Resolver la referencia al objeto en el Servicio de Nombres
			String nombre = "Hola";
			Hola s = HolaHelper.narrow(ncRef.resolve_str(nombre));
			System.out.println("Obtenido manejador para el objeto servidor: " + s);
			
			//invocar la operacion decirHola() del servidor
			System.out.println("El servidor dice: " + s.decirHola());
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
