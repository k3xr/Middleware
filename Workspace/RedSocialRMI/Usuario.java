import java.io.Serializable;

/*
 * version: 1.5
 */
public class Usuario implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String password;
	private String photo;
	private String nombre;
	private Boolean privado;
	private String listaAmigos;


	public String getListaAmigos() {
		return listaAmigos;
	}


	public void setListaAmigos(String listaAmigos) {
		this.listaAmigos = listaAmigos;
	}


	public Usuario(String id, String password, String photo, String nombre, int privado, String listaAmigos) {
		this.id = id;
		this.password = password;
		this.photo = photo;
		this.nombre = nombre;
		if(privado == 0){
			this.privado = false;
		}
		else{
			this.privado = true;
		}
		
		this.listaAmigos = listaAmigos;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Boolean getPrivado() {
		return privado;
	}


	public void setPrivado(Boolean privado) {
		this.privado = privado;
	}

}
