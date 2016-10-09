import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GUI {

	private static String user;
	
	public String getUser() {
		return user;
	}

	private static String pass;
	private static String nombre;
	private static String publicacion;
	private JFrame ventanaPrincipal;
	private JDialog ventanaLogin;
	private JDialog ventanaRegistro;
	private static ClienteImpl clienteImpl = null;
	private JLabel muroLabel;
	private JLabel ultiLabel;
	private JButton button_1, button_2, button_3, button_4, button_5;
	private JButton button_6, button_7, button_8, button_9, button_10;
	private JPanel panel;
	private JButton botonAgregar;
	private JCheckBox checkPrivi;


	public GUI(ClienteImpl clienteImp){

		clienteImpl = clienteImp;

		// Construcción de ventana login
		ventanaLogin = new JDialog(ventanaPrincipal,"Login");
		ventanaLogin.setModal(true);
		ventanaLogin.setBounds(100, 100, 450, 300);
		ventanaLogin.getContentPane().setLayout(null);

		//----------------------------------------labelDatosCorrectos
		final JLabel labelDatosCorrectos = new JLabel("Datos correctos ;)");
		labelDatosCorrectos.setVisible(false);
		labelDatosCorrectos.setHorizontalAlignment(SwingConstants.CENTER);
		labelDatosCorrectos.setBounds(159, 159, 141, 14);
		ventanaLogin.getContentPane().add(labelDatosCorrectos);

		//----------------------------------------labelDatosIncorrectos
		final JLabel labelDatosIncorrectos = new JLabel("Datos incorrectos");
		labelDatosIncorrectos.setVisible(false);
		labelDatosIncorrectos.setHorizontalAlignment(SwingConstants.CENTER);
		labelDatosIncorrectos.setBounds(159, 159, 141, 14);
		ventanaLogin.getContentPane().add(labelDatosIncorrectos);

		//----------------------------------------txtUser
		final JTextField txtUser = new JTextField();
		txtUser.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				txtUser.selectAll();
				user = txtUser.getText();
			}
			public void focusGained(FocusEvent e) {
			}
		});
		txtUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUser.selectAll();
				user = txtUser.getText();
				boolean ok = clienteImpl.login(user, pass);
				if(!ok){
					labelDatosIncorrectos.setVisible(true);
				}
				else{
					ventanaLogin.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});

		txtUser.setBounds(138, 85, 178, 26);
		ventanaLogin.getContentPane().add(txtUser);
		txtUser.setColumns(10);

		//----------------------------------------btnLogin
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ok = clienteImpl.login(user, pass);
				if(!ok){
					labelDatosIncorrectos.setVisible(true);
				}
				else{
					ventanaLogin.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});

		//----------------------------------------passwordField
		final JPasswordField passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				pass = new String(passwordField.getPassword());
			}

			@Override
			public void focusGained(FocusEvent arg0) {

			}
		});
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pass = new String(passwordField.getPassword());
				boolean ok = clienteImpl.login(user, pass);
				if(!ok){
					labelDatosIncorrectos.setVisible(true);
				}
				else{
					ventanaLogin.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});
		passwordField.setBounds(138, 122, 178, 26);
		ventanaLogin.getContentPane().add(passwordField);
		btnLogin.setBounds(159, 194, 141, 23);
		ventanaLogin.getContentPane().add(btnLogin);

		//----------------------------------------botonRegistro
		JButton botonRegistro = new JButton("Registrarse");
		botonRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ventanaLogin.setVisible(false);
				ventanaRegistro.setVisible(true);
			}
		});
		botonRegistro.setBounds(159, 228, 141, 23);
		ventanaLogin.getContentPane().add(botonRegistro);

		//----------------------------------------lblUser
		JLabel lblUser = new JLabel("User:");
		lblUser.setLabelFor(txtUser);
		lblUser.setBounds(82, 91, 46, 14);
		ventanaLogin.getContentPane().add(lblUser);

		//----------------------------------------lblPassword
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(59, 128, 69, 14);
		ventanaLogin.getContentPane().add(lblPassword);

		JLabel lblOscarsRedsocial = new JLabel("Oscar's RedSocial");
		lblOscarsRedsocial.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOscarsRedsocial.setBackground(Color.BLACK);
		lblOscarsRedsocial.setBounds(159, 28, 235, 46);
		ventanaLogin.getContentPane().add(lblOscarsRedsocial);		

		//FIN: ventana Login

		// Construcción de ventana principal
		ventanaPrincipal = new JFrame("RedSocial");
		ventanaPrincipal.setBounds(100, 100, 1026, 663);
		ventanaPrincipal.getContentPane().setLayout(null);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		ventanaPrincipal.setContentPane(contentPane);
		contentPane.setLayout(null);

		//INI: Panel Multipestaña
		JTabbedPane tabbedPane1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane1.setBounds(0, 0, 1000, 624);
		contentPane.add(tabbedPane1);

		panel = new JPanel();
		tabbedPane1.addTab("Mi perfil", null, panel, null);
		panel.setLayout(null);

		//INI: Campo FOTO
		//Agregaremos una foto fija.
		String direccion = "foto.jpg";
		ImageIcon icon = new ImageIcon(direccion);
		JLabel lblNewLabel = new JLabel("Foto");
		lblNewLabel.setIcon(icon);
		lblNewLabel.setBounds(28, 11, 98, 93);
		panel.add(lblNewLabel);
		//FIN: Campo FOTO

		//INI: Campo publicar
		//JtexField donde se introducirá el mensaje a publicar
		final JTextField txtquEstaPasando = new JTextField();
		txtquEstaPasando.setText("\u00BFQu\u00E9 está pasando?");
		txtquEstaPasando.setBounds(28, 496, 836, 75);
		//Añadimos el objeto al panel
		panel.add(txtquEstaPasando);
		txtquEstaPasando.setColumns(10);

		txtquEstaPasando.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				txtquEstaPasando.selectAll();
				publicacion = txtquEstaPasando.getText();
			}
			public void focusGained(FocusEvent e) {
			}
		});
		txtquEstaPasando.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtquEstaPasando.selectAll();
				publicacion = txtquEstaPasando.getText();
				if(publicacion != ""){
					clienteImpl.publicar (user, publicacion);
					visibleVentanaPpal(null, null);
				}

			}
		});

		JButton botonPublicar = new JButton("Publicar");
		botonPublicar.setBounds(885, 522, 89, 23);
		panel.add(botonPublicar);
		botonPublicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtquEstaPasando.selectAll();
				publicacion = txtquEstaPasando.getText();
				if(publicacion != ""){
					clienteImpl.publicar (user, publicacion);
					visibleVentanaPpal(null, null);
				}
			}
		});
		//FIN: Campo publicar

		//INI: Componentes Perfil
		muroLabel = new JLabel();
		muroLabel.setBounds(28, 140, 473, 334);
		muroLabel.setVerticalAlignment(SwingConstants.TOP);
		panel.add(muroLabel);

		JLabel lblTuMuro = new JLabel("Tu Muro:");
		lblTuMuro.setBounds(28, 115, 190, 14);
		panel.add(lblTuMuro);

		//INI: JTexField de UltimasNoticias
		ultiLabel = new JLabel();
		ultiLabel.setVerticalAlignment(SwingConstants.TOP);
		ultiLabel.setBounds(540, 39, 445, 435);
		panel.add(ultiLabel);
		ultiLabel.setText("");
		//FIN: JTexField UltimasNoticias

		JLabel lblNewLabel_2 = new JLabel("Ultimas noticias:");
		lblNewLabel_2.setBounds(540, 14, 190, 14);
		panel.add(lblNewLabel_2);
		//FIN: Componentes Perfil
		
		//INI: Pestaña NuevosAmigos en multipestaña
		JPanel nuevosAmigos = new JPanel();
		//Agregamos al tabbedPane, el panel multipestaña
		tabbedPane1.addTab("Nuevos Amigos", null, nuevosAmigos, null);
		nuevosAmigos.setLayout(null);
		
		botonAgregar = new JButton("Agregar");
		botonAgregar.setBounds(80, 176, 89, 23);
		nuevosAmigos.add(botonAgregar);
		botonAgregar.setVisible(false);

		//INI: Pestaña Opciones en multipestaña
		JPanel panelOpciones = new JPanel();
		//Agregamos al tabbedPane, el panel multipestaña
		tabbedPane1.addTab("Opciones", null, panelOpciones, null);
		panelOpciones.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(42, 44, 77, 14);
		panelOpciones.add(lblNombre);
		
		final JTextField textField_3 = new JTextField();
		textField_3.setBounds(129, 41, 86, 20);
		panelOpciones.add(textField_3);
		textField_3.setColumns(10);

		JLabel lblPassword1 = new JLabel("Password:");
		lblPassword1.setBounds(42, 86, 77, 14);
		panelOpciones.add(lblPassword1);

		//INI: Opciones -> Privado
		checkPrivi = new JCheckBox("Privado");
		checkPrivi.setBounds(150, 120, 80, 30);
		panelOpciones.add(checkPrivi);

		checkPrivi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Cambiamos la privacidad
				clienteImpl.cambioPrivi(user);
			}
		});
		//FIN: Opciones -> Privado

		final JPasswordField passwordField_4 = new JPasswordField();
		passwordField_4.setBounds(129, 83, 86, 20);
		panelOpciones.add(passwordField_4);
		passwordField_4.setColumns(10);
		
		final JLabel actualizado = new JLabel("Actualizado!");
		actualizado.setBounds(57, 220, 265, 23);
		panelOpciones.add(actualizado);
		actualizado.setVisible(false);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(80, 176, 89, 23);
		panelOpciones.add(btnGuardar);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_3.getText() != null && textField_3.getText() != "" && textField_3.getText().length() > 3){
					clienteImpl.modificarAtributo(user, textField_3.getText(), "nombre");
					actualizado.setVisible(true);
				}
				if(passwordField_4.getPassword() != null && textField_3.getText() != "" && passwordField_4.getPassword().length > 5){
					String nuevacontra = new String(passwordField_4.getPassword());
					clienteImpl.modificarAtributo(user, nuevacontra, "password");
					actualizado.setVisible(true);
				}
			}
		});
		//FIN: Pestaña Opciones en multipestaña

		//INI: Pestaña Opciones en multipestaña
		JPanel panelBuscar = new JPanel();
		//Agregamos al tabbedPane, el panel multipestaña
		tabbedPane1.addTab("Buscar", null, panelBuscar, null);
		panelBuscar.setLayout(null);

		final JTextField textFieldBuscar;
		textFieldBuscar = new JTextField();
		textFieldBuscar.setText("Introduzca criterio de b\u00FAsqueda");
		textFieldBuscar.setColumns(10);
		textFieldBuscar.setBounds(57, 25, 757, 20);
		panelBuscar.add(textFieldBuscar);
		textFieldBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldBuscar.selectAll();
				String busqueda = textFieldBuscar.getText();
				if(busqueda != "" && busqueda != null){
					Usuario[] resultadoBusqueda = clienteImpl.buscarUsers(busqueda);
					ResultadosBusqueda(resultadoBusqueda);
				}
			}
		});

		JButton buttonBuscar = new JButton("Buscar");
		buttonBuscar.setBounds(838, 24, 89, 23);
		panelBuscar.add(buttonBuscar);

		buttonBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldBuscar.selectAll();
				String busqueda = textFieldBuscar.getText();
				if(busqueda != "" && busqueda != null){
					Usuario[] resultadoBusqueda = clienteImpl.buscarUsers(busqueda);
					ResultadosBusqueda(resultadoBusqueda);
				}
			}
		});
		
		final JLabel lblNewLabel_3 = new JLabel("MURO");
		lblNewLabel_3.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_3.setBounds(387, 96, 540, 456);
		panelBuscar.add(lblNewLabel_3);

		button_1 = new JButton("");
		button_1.setBounds(57, 156, 265, 23);
		panelBuscar.add(button_1);
		button_1.setVisible(false);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] fechaMen = new String[100];
				String concatenado = "<html>";
				fechaMen = clienteImpl.sacarPublicaciones(50, button_1.getText());		
				for(int i=0; fechaMen.length > i; i++){
					if(fechaMen[i]!=null){
						concatenado = concatenado + fechaMen[i]+"<br>";
					}
				}
				concatenado = concatenado + "</html>";
				lblNewLabel_3.setText(concatenado);
			}
		});

		button_2 = new JButton("");
		button_2.setBounds(57, 220, 265, 23);
		panelBuscar.add(button_2);
		button_2.setVisible(false);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] fechaMen = new String[100];
				String concatenado = "<html>";
				fechaMen = clienteImpl.sacarPublicaciones(50, button_2.getText());		
				for(int i=0; fechaMen.length > i; i++){
					if(fechaMen[i]!=null){
						concatenado = concatenado + fechaMen[i]+"<br>";
					}
				}
				concatenado = concatenado + "</html>";
				lblNewLabel_3.setText(concatenado);
			}
		});

		button_3 = new JButton("");
		button_3.setBounds(57, 285, 265, 23);
		panelBuscar.add(button_3);
		button_3.setVisible(false);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] fechaMen = new String[100];
				String concatenado = "<html>";
				fechaMen = clienteImpl.sacarPublicaciones(50, button_3.getText());		
				for(int i=0; fechaMen.length > i; i++){
					if(fechaMen[i]!=null){
						concatenado = concatenado + fechaMen[i]+"<br>";
					}
				}
				concatenado = concatenado + "</html>";
				lblNewLabel_3.setText(concatenado);
			}
		});

		button_4 = new JButton("");
		button_4.setBounds(57, 347, 265, 23);
		panelBuscar.add(button_4);
		button_4.setVisible(false);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] fechaMen = new String[100];
				String concatenado = "<html>";
				fechaMen = clienteImpl.sacarPublicaciones(50, button_4.getText());		
				for(int i=0; fechaMen.length > i; i++){
					if(fechaMen[i]!=null){
						concatenado = concatenado + fechaMen[i]+"<br>";
					}
				}
				concatenado = concatenado + "</html>";
				lblNewLabel_3.setText(concatenado);
			}
		});

		button_5 = new JButton("");
		button_5.setBounds(57, 404, 265, 23);
		panelBuscar.add(button_5);
		button_5.setVisible(false);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String[] fechaMen = new String[100];
				String concatenado = "<html>";
				fechaMen = clienteImpl.sacarPublicaciones(50, button_5.getText());		
				for(int i=0; fechaMen.length > i; i++){
					if(fechaMen[i]!=null){
						concatenado = concatenado + fechaMen[i]+"<br>";
					}
				}
				concatenado = concatenado + "</html>";
				lblNewLabel_3.setText(concatenado);
			}
		});

		button_6 = new JButton("");
		button_6.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		button_6.setBackground(Color.GREEN);
		button_6.setForeground(Color.BLACK);
		button_6.setBounds(332, 156, 33, 23);
		panelBuscar.add(button_6);
		button_6.setVisible(false);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clienteImpl.notificarAgregarAmigo(user, button_1.getText());
			}
		});

		JLabel lblAgregarAmigo = new JLabel("AGREGAR AMIGO");
		lblAgregarAmigo.setBounds(262, 96, 103, 14);
		panelBuscar.add(lblAgregarAmigo);

		button_7 = new JButton("");
		button_7.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		button_7.setForeground(Color.BLACK);
		button_7.setBackground(Color.GREEN);
		button_7.setBounds(332, 220, 33, 23);
		panelBuscar.add(button_7);
		button_7.setVisible(false);
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clienteImpl.notificarAgregarAmigo(user, button_2.getText());
			}
		});

		button_8 = new JButton("");
		button_8.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		button_8.setForeground(Color.BLACK);
		button_8.setBackground(Color.GREEN);
		button_8.setBounds(332, 285, 33, 23);
		panelBuscar.add(button_8);
		button_8.setVisible(false);
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clienteImpl.notificarAgregarAmigo(user, button_3.getText());
			}
		});

		button_9 = new JButton("");
		button_9.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		button_9.setForeground(Color.BLACK);
		button_9.setBackground(Color.GREEN);
		button_9.setBounds(332, 347, 33, 23);
		panelBuscar.add(button_9);
		button_9.setVisible(false);
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clienteImpl.notificarAgregarAmigo(user, button_4.getText());
			}
		});

		button_10 = new JButton("");
		button_10.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		button_10.setForeground(Color.BLACK);
		button_10.setBackground(Color.GREEN);
		button_10.setBounds(332, 404, 33, 23);
		panelBuscar.add(button_10);
		button_10.setVisible(false);
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clienteImpl.notificarAgregarAmigo(user, button_5.getText());
			}
		});

		//FIN: Pestaña Opciones en multipestaña

		// Construcción de ventana registro
		ventanaRegistro = new JDialog(ventanaPrincipal,"Registro");
		ventanaRegistro.setModal(true);
		ventanaRegistro.setBounds(100, 100, 450, 300);
		ventanaRegistro.getContentPane().setLayout(null);

		//----------------------------------------label
		final JLabel label6 = new JLabel("User:");
		label6.setBounds(50, 39, 271, 14);
		label6.setHorizontalAlignment(SwingConstants.LEFT);
		ventanaRegistro.getContentPane().add(label6);

		//----------------------------------------label
		final JLabel label_1 = new JLabel("Password:");
		label_1.setBounds(50, 93, 271, 14);
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		ventanaRegistro.getContentPane().add(label_1);

		//----------------------------------------label
		final JLabel label_2 = new JLabel("Nombre completo:");
		label_2.setBounds(50, 141, 271, 14);
		label_2.setHorizontalAlignment(SwingConstants.LEFT);
		ventanaRegistro.getContentPane().add(label_2);

		//----------------------------------------txtId
		final JTextField txtId = new JTextField();
		txtId.setBounds(172, 59, 180, 27);
		ventanaRegistro.getContentPane().add(txtId);
		txtId.setColumns(10);
		txtId.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				txtId.selectAll();
				user = txtId.getText();
			}
			public void focusGained(FocusEvent e) {
			}
		});
		txtId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtId.selectAll();
				user = txtId.getText();
				if (pass == null || pass.length() < 5){
					label_1.setText("contraseña no válida");
				}
				else{
					label_1.setText("contraseña ok");
				}
				if (nombre == null || nombre.length() < 3){
					label_2.setText("Nombre no válido");
				}	
				else{
					label_2.setText("nombre ok");
				}
				int res = clienteImpl.registrarUsuario(user, pass, nombre);
				if(res!=0){
					label6.setText("Nombre de usuario no válido"+res);
				}
				else{
					ventanaRegistro.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});

		//----------------------------------------txtNombre
		final JTextField txtNombre = new JTextField();
		txtNombre.setBounds(172, 160, 180, 27);
		txtNombre.setColumns(10);
		ventanaRegistro.getContentPane().add(txtNombre);
		txtNombre.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				txtNombre.selectAll();
				nombre = txtNombre.getText();
			}
			public void focusGained(FocusEvent e) {
			}
		});
		txtNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNombre.selectAll();
				nombre = txtNombre.getText();
				if (pass == null || pass.length() < 5){
					label_1.setText("contraseña no válida");
				}
				else{
					label_1.setText("contraseña ok");
				}
				if (nombre == null || nombre.length() < 3){
					label_2.setText("Nombre no válido");
				}	
				else{
					label_2.setText("nombre ok");
				}
				int res = clienteImpl.registrarUsuario(user, pass, nombre);
				if(res!=0){
					label6.setText("Nombre de usuario no válido"+res);
				}
				else{
					ventanaRegistro.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});

		//----------------------------------------txtPassword
		final JPasswordField passwordField6 = new JPasswordField();
		passwordField6.setBounds(172, 110, 180, 26);
		ventanaRegistro.getContentPane().add(passwordField6);
		passwordField6.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				pass = new String(passwordField6.getPassword());
			}
			public void focusGained(FocusEvent e) {
			}
		});
		passwordField6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pass = new String(passwordField6.getPassword());
				if (pass == null || pass.length() < 5){
					label_1.setText("contraseña no válida");
				}
				else{
					label_1.setText("contraseña ok");
				}
				if (nombre == null || nombre.length() < 3){
					label_2.setText("Nombre no válido");
				}	
				else{
					label_2.setText("nombre ok");
				}
				int res = clienteImpl.registrarUsuario(user, pass, nombre);
				if(res!=0){
					label6.setText("Nombre de usuario no válido"+res);
				}
				else{
					ventanaRegistro.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});


		//----------------------------------------botonRegistro
		JButton botonRegistro6 = new JButton("Registrarse");
		botonRegistro6.setBounds(191, 214, 141, 23);
		ventanaRegistro.getContentPane().add(botonRegistro6);
		botonRegistro6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pass == null || pass.length() < 5){
					label_1.setText("contraseña no válida");
				}
				else{
					label_1.setText("contraseña ok");
				}
				if (nombre == null || nombre.length() < 3){
					label_2.setText("Nombre no válido");
				}	
				else{
					label_2.setText("nombre ok");
				}
				int res = clienteImpl.registrarUsuario(user, pass, nombre);
				if(res!=0){
					label6.setText("Nombre de usuario no válido"+res);
				}
				else{
					ventanaRegistro.setVisible(false);
					visibleVentanaPpal(null, null);
				}
			}
		});

		ventanaRegistro.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ventanaRegistro.setVisible(false);
				ventanaLogin.setVisible(true);
			}

			public void windowClosed(WindowEvent e) {
				ventanaRegistro.setVisible(false);
				ventanaLogin.setVisible(true);
			}
		});

		ventanaLogin.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				clienteImpl.logout();
				System.exit(0);
			}

			public void windowClosed(WindowEvent e) {
				clienteImpl.logout();
				System.exit(0);
			}
		});
		
		ventanaPrincipal.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				clienteImpl.logout();
				System.exit(0);
			}

			public void windowClosed(WindowEvent e) {
				clienteImpl.logout();
				System.exit(0);
			}
		});

		ventanaPrincipal.setVisible(false);
		ventanaRegistro.setVisible(false);
		ventanaLogin.setVisible(true);

	}

	public void visibleVentanaPpal(String alerta, String usuario){
		ventanaPrincipal.setVisible(true);
		checkPrivi.setSelected(clienteImpl.dameUser(user).getPrivado());
		JLabel lblNewLabel_1 = new JLabel(user);
		lblNewLabel_1.setBounds(136, 14, 365, 14);
		panel.add(lblNewLabel_1);
		String[] fechaMen = new String[100];
		String concatenado = "<html>";
		fechaMen = clienteImpl.sacarPublicaciones(50, user);
		for(int i=0; fechaMen.length > i; i++){
			if(fechaMen[i]!=null){
				concatenado = concatenado + fechaMen[i]+"<br>";
			}
		}
		concatenado = concatenado + "</html>";
		muroLabel.setText(concatenado);
		
		if(alerta != null && usuario != null){
			String alertasPrevias = ultiLabel.getText();
			String html = "<html>";
			ultiLabel.setText(html + alerta +" ---------  Publicado por: \""+ usuario +"\"<br>" + alertasPrevias);
		}

	}
	
	public void NuevaSolicitud(final String usuario){
		botonAgregar.setText(usuario);
		botonAgregar.setVisible(true);
		botonAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clienteImpl.agregarAmigo(usuario, user);
				clienteImpl.agregarAmigo(user, usuario);
				botonAgregar.setVisible(false);
			}
		});
	}

	public void ResultadosBusqueda(Usuario[] resultadoBusqueda2){

		JButton[] arrayBotones = new JButton[10];
		arrayBotones[0] = button_1;
		arrayBotones[1] = button_2;
		arrayBotones[2] = button_3;
		arrayBotones[3] = button_4;
		arrayBotones[4] = button_5;
		arrayBotones[5] = button_6;
		arrayBotones[6] = button_7;
		arrayBotones[7] = button_8;
		arrayBotones[8] = button_9;
		arrayBotones[9] = button_10;

		for(int i = 0; i<5 && resultadoBusqueda2[i] != null; i++){
			arrayBotones[i].setText(resultadoBusqueda2[i].getId());
			arrayBotones[i].setVisible(true);
			if(resultadoBusqueda2[i].getPrivado()){
				arrayBotones[i].setEnabled(false);
			}
			arrayBotones[i+5].setVisible(true);
		}
	}	
}