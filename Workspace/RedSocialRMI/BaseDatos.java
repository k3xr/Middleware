import java.sql.Connection;	
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BaseDatos{

	/**
	 * Conecta a una base de datos en la ruta pasada como argumento.
	 * Si el archivo señalado en la ruta no existe, se crea.
	 * @param ruta - La ruta de la DB a conectar.
	 * @return La conexión a la ruta.
	 */
	public Connection conectarA(String ruta){

		try {
			//Aquí cargamos el driver de SQLITE.
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException e) {
			//Esto se ejecuta si hay un error con el driver de la base de datos.
			e.printStackTrace();
		}

		//Declaramos la conexión:
		Connection conn = null;

		try {
			//Aquí se obtiene la conexión:
			conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
			//Un mensaje en la consola para saber si se realizó la conexión y donde está el archivo:
			System.out.println("Conexión realizada correctamente - Ruta de base de datos: " + ruta);
		}
		catch (SQLException e) {
			//Esto se ejecuta si hay un error en la base de datos:
			e.printStackTrace();

		}
		//Devolvemos la conexión:
		return conn;
	}


	/**
	 * Crea objeto Usuario con todos los campos de la base de datos a partir del id
	 * @param str
	 * @param conn
	 * @return Usuario con todos sus campos
	 */
	public Usuario getUsuario (String str, Connection conn){
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT id, Pass, Nombre, Photo, Privado, ListaAmigos FROM Usuarios WHERE id='"+str+"'");
			ResultSet rs = ps.executeQuery();
			if(rs == null){
				System.out.println("No se encontró el usuario");
				return null;
			}
			Usuario usuario=null;
			if(rs.next()){
				int privado= 0;
				if(rs.getBoolean(5) == true){
					privado = 1;
				}
				usuario = new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), privado, rs.getString(6));
			}
			rs.close();
			ps.close();
			return usuario;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}


	/**
	 * Busca todos los usuarios que contienen en el id o nombre el str pasado como argumento
	 * @param str
	 * @param conn
	 * @return aUsers: array de usuarios encontrados
	 */

	public Usuario[] buscaUsuarios (String str, Connection conn){

		//Array donde meteremos todos los usuario que coinciden en nombre o id en la busqueda
		Usuario[] aUsers = null;
		//Contador para rellenar array
		int i = 0;
		try{
			PreparedStatement ps = conn.prepareStatement("SELECT id, Pass, Nombre, Photo, Privado, ListaAmigos FROM Usuarios WHERE id LIKE '%"+str+"%' OR Nombre LIKE '%"+str+"%'");
			ResultSet rs = ps.executeQuery();

			//Numero de columnas = numero de usuarios
			aUsers = new Usuario[5];
			//Pasamos de row en row (filas) creando un usuario con todos los datos.
			int contador= 0;
			while(rs.next() && contador < 5){
				int privado= 0;
				if(rs.getBoolean(5) == true){
					privado = 1;
				}
				aUsers[i] = new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), privado, rs.getString(6));
				i++;
				contador++;
			}
		}catch (SQLException e1){
			e1.printStackTrace();
		}
		if(aUsers != null){
			return aUsers;
		}
		return null;
	}

	/**
	 * Crea muro y usuario en la base de datos a partir del objeto usuario
	 * @param user
	 * @param conn
	 * @return true si exito
	 */
	public boolean resgistrarUsuario(Usuario user, Connection conn){

		//comprobamos si ya existe el usuario que se intenta registrar
		Usuario repetido = getUsuario(user.getId(), conn);
		if(repetido != null){
			return false;
		}
		try {
			//Añadimos usuario a la tabla de usuarios
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Usuarios VALUES ('"+user.getId()+"','"+user.getPassword()+"','"+user.getNombre()+"','"+user.getPhoto()+"',"+0+",'"+user.getListaAmigos()+"')");
			ps.executeUpdate();
			//Cremos tabla muro asociado al usuario creado
			ps = conn.prepareStatement("CREATE TABLE \""+user.getId()+"muro\" (\"Fecha\" TEXT NOT NULL ,\"Mensaje\" TEXT NOT NULL)");
			ps.executeUpdate();
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("No se puedo registrar el usuario, compruebe los campos ingresados");
		return false;
	}

	/**
	 * Este metodo cambia la privacidad, si esta es privada(1) la cambia a visible(0) y viceversa. La llamada a este metodo invierte la privacidad
	 * @param id
	 * @param conn
	 * @return
	 */
	public boolean modificaPrivi (String id, Connection conn){
		//Objeto usuario para usar sobre el gets
		Usuario repetido = getUsuario(id, conn);
		if(repetido == null){
			return false;
		}
		try {
			//Modificamos el campo privacidad el usuario "repetido"
			PreparedStatement ps = conn.prepareStatement("SELECT Privado FROM Usuarios WHERE id='"+repetido.getId()+"'");
			ResultSet rs = ps.executeQuery();
			int aux = rs.getInt(1);
			if(aux == 0){
				PreparedStatement ps1 = conn.prepareStatement("UPDATE Usuarios SET Privado="+1+" WHERE id='"+repetido.getId()+"'");
				ps1.executeUpdate();
			}else{
				PreparedStatement ps1 = conn.prepareStatement("UPDATE Usuarios SET Privado="+0+" WHERE id='"+repetido.getId()+"'");
				ps1.executeUpdate();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	/**
	 * Introduce el new_value para usuario id al atributo que puede ser nombre o password
	 * @param id
	 * @param new_value
	 * @param conn
	 * @return
	 */
	public boolean modificarAtri (String id, String new_value, String atributo, Connection conn){

		Usuario repetido = getUsuario(id, conn);
		if(repetido == null){
			return false;
		}
		try {
			//Escribimos la nueva password o nombre
			PreparedStatement ps1 = conn.prepareStatement("UPDATE Usuarios SET "+atributo+"='"+new_value+"' WHERE id='"+repetido.getId()+"'");
			ps1.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	/**
	 * Agrega un idAmigo a ListaAmigos del usuario pasado como id, busca que este no se encuentra ya en la lista y lo agrega, tambien controla el caso 
	 * en el que el usuario no tenia amigos previamente.
	 * @param id
	 * @param idAmigo
	 * @param conn
	 * @return
	 */
	public boolean addAmigo (String id, String idAmigo, Connection conn){

		Usuario repetido = getUsuario(id, conn);
		if(repetido == null){
			return false;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT ListaAmigos FROM Usuarios WHERE id='"+repetido.getId()+"'");
			ResultSet rs = ps.executeQuery();
			String listaCompleta = rs.getString(1);

			//ListaCompleta es un string de la forma: amigo1;amigo2;amigo3;... 
			if(!listaCompleta.isEmpty()){
				
				//Cadena de id's (Nombres de usuario)
				String [] amigo = listaCompleta.split(";");

				//Se busca si el amigo esta ya en la lista
				for(int i = 0; i<amigo.length; i++){
					//En caso de que el usuario este ya agreado o seas tu mismo
					if(amigo[i].equalsIgnoreCase(idAmigo) || amigo[i].equalsIgnoreCase(id)){
						return false;
					}
				}// En caso de que no lo este se agrega al final del string listaCompleta. No es necesario actualizar el array pues era simplemente para
				// poder separar los elementos e iterar sobre ellos.
				listaCompleta = listaCompleta.concat(idAmigo);
				PreparedStatement ps1 = conn.prepareStatement("UPDATE Usuarios SET ListaAmigos='"+listaCompleta+";' WHERE id='"+repetido.getId()+"'");
				ps1.executeUpdate();

			}else{//Caso de que no tubiera aun amigos
				//Actualizamos la lista en la base de datos
				PreparedStatement ps1 = conn.prepareStatement("UPDATE Usuarios SET ListaAmigos='"+idAmigo+";' WHERE id='"+repetido.getId()+"'");
				ps1.executeUpdate();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return true;
	}


	/**
	 * Devuelve la lista de amigos de un usuario id.
	 * @param id
	 * @param conn
	 * @return
	 */
	public String devuelveAmigos (String id, Connection conn){

		Usuario repetido = getUsuario(id, conn);
		String lista = null;
		if(repetido == null){
			return lista;
		}
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT ListaAmigos FROM Usuarios WHERE id='"+repetido.getId()+"'");
			ResultSet rs = ps.executeQuery();
			lista = rs.getString(1);

			return lista;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return lista;
	}

	/**
	 * Añade un mensaje al muro del usuario id
	 * @param id 	 = id del usuario asociado al muro
	 * @param cadena = mensaje del muro
	 * @param conn
	 * @return
	 */
	public boolean publicar (String id, String cadena, Connection conn){
		Usuario repetido = getUsuario(id, conn);
		if(repetido == null){
			return false;
		}
		try {
			//añadimos campo a la tabla muro
			PreparedStatement ps = conn.prepareStatement("INSERT INTO "+repetido.getId()+"muro (Fecha,Mensaje) VALUES ('"+GetDateM().toString()+"','"+cadena+"')");
			ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("publicado con éxito");
		return true;
	}

	/**
	 * Este metodo coge un numero del muro del usuario pasado como id. En caso de que no tengamos mensajes
	 * @param numero
	 * @param id
	 * @param conn
	 * @return
	 */
	public String[] extraePublicaciones(int numero, String id, Connection conn){
		try {
			//Hacemos la peticion a la base de datos 
			PreparedStatement ps = conn.prepareStatement("SELECT Fecha,Mensaje FROM "+id+"muro ORDER BY rowid DESC");
			ResultSet rs = ps.executeQuery();

			//En caso de no encontrarse error
			if(rs == null){
				System.out.println("No se encontró el usuario o el muro esta vacio");
				return null;
			}
			//String con el numero de publicaciones que queremos
			String[] fechaMensaje = new String[numero*2];
			int cont = 1;
			int otro = 0;

			while(rs.next()){
				fechaMensaje[otro] = rs.getString(cont);
				fechaMensaje[otro+1] = rs.getString(cont+1);
				//System.out.println(rs.getString(cont)+"    "+rs.getString(cont+1));
				otro = otro+2;
			}
			//Mientras haya siguiente columna y el numero de pares fecha-mensaje sea menor que cont (contador de columnas), 2 por cada par
			//Fecha-mensaje
			rs.close();
			ps.close();
			return fechaMensaje;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * Devuelve fecha
	 * @return
	 */
	private String GetDateM() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		//get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		return(dateFormat.format(cal.getTime()));

	}


}