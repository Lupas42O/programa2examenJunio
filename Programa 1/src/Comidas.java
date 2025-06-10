import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;

public class Comidas {

	private JFrame frame;
	private JTable table;
	private JTextField textFieldNombre;
	private JTextField textFieldPrecio;
	private JTextField textFieldUds;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Comidas window = new Comidas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Comidas() {
		initialize();
	}


	Connection con = null;
	PreparedStatement pst;
	ResultSet rs;
	private JTextField textFieldId;

	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3307/comidas";
			String usuario = "alumno";
			String pswd = "Skylanders1";
			con = DriverManager.getConnection(url, usuario, pswd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Clear All
	public void clear() {
		textFieldNombre.setText("");
		textFieldPrecio.setText("");
		textFieldNombre.requestFocus();
	}
	

	// Load Table
	public void loadData() {
		try {
			pst = con.prepareStatement("Select * from carta");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * /** Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 671, 437);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("id");
		model.addColumn("Nombre");
		model.addColumn("Precio");
		
		JComboBox comboPlatos = new JComboBox();
		comboPlatos.setBounds(507, 252, 32, 24);
		frame.getContentPane().add(comboPlatos);
		

		try {
			connect();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM carta");
			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("id_comida");
				comboPlatos.addItem(rs.getInt("id_comida"));
				row[1] = rs.getString("nombre");
				row[2] = rs.getDouble("precio");
				model.addRow(row);
			}
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(84, 0, 495, 126);
		frame.getContentPane().add(scrollPane);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				TableModel model = table.getModel();
				textFieldNombre.setText(model.getValueAt(index, 1).toString());
				textFieldPrecio.setText(model.getValueAt(index, 2).toString());
				textFieldId.setText(model.getValueAt(index, 0).toString());
			}
		});
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(12, 208, 114, 19);
		frame.getContentPane().add(textFieldNombre);

		textFieldNombre.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(12, 181, 70, 15);
		frame.getContentPane().add(lblNombre);

		textFieldPrecio = new JTextField();
		textFieldPrecio.setBounds(12, 266, 114, 19);
		frame.getContentPane().add(textFieldPrecio);
		textFieldPrecio.setColumns(10);

		JLabel lblPrecio = new JLabel("Precio");
		lblPrecio.setBounds(12, 239, 70, 15);
		frame.getContentPane().add(lblPrecio);

		textFieldUds = new JTextField();
		textFieldUds.setBounds(425, 221, 114, 19);
		frame.getContentPane().add(textFieldUds);
		textFieldUds.setColumns(10);

		JLabel lblUnidades = new JLabel("Unidades");
		lblUnidades.setBounds(425, 205, 70, 15);
		frame.getContentPane().add(lblUnidades);

		JButton btnInsertar = new JButton("Insertar");
		btnInsertar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String nombre = textFieldNombre.getText();
				String precio = textFieldPrecio.getText();
				//Falta comprobar con expresiones regulares el formato
				if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Por favor introduce un nombre");
					textFieldNombre.requestFocus();
					return;
				}
				if (precio == null || precio.isEmpty() || precio.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Por favor introduce un precio");
					textFieldPrecio.requestFocus();
					return;
				}
				//Falta else para no insertar si hay errores
				try {
					connect();
					PreparedStatement ins_pstmt = con.prepareStatement("INSERT INTO carta (nombre, precio) VALUES (?,?)");
					ins_pstmt.setString(1, nombre);
					ins_pstmt.setString(2, precio);
					ins_pstmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Plato añadido correctamente");
					clear();
					loadData();JComboBox comboPlatos = new JComboBox();
					comboPlatos.setBounds(507, 252, 32, 24);
					frame.getContentPane().add(comboPlatos);
					

					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnInsertar.setBounds(9, 297, 117, 25);
		frame.getContentPane().add(btnInsertar);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setBounds(12, 363, 117, 25);
		frame.getContentPane().add(btnActualizar);
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText();
				double precio = Double.parseDouble(textFieldPrecio.getText());//poner double para precio e int para id en todas partes
				int id = Integer.parseInt(textFieldId.getText());

				if (textFieldNombre.getText() == null) {
					JOptionPane.showMessageDialog(null, "Por favor introduce el nombre");
					textFieldNombre.requestFocus();
					return;
				}
				if (textFieldPrecio.getText() == null) {
					JOptionPane.showMessageDialog(null, "Por Favor introduce el Precio");
					textFieldPrecio.requestFocus();
					return;
				}
				
				try {
					connect();
					String sql = "update carta set nombre=?,precio=? where id_comida=?";//poner nombres igual que en la base de datos
					pst = con.prepareStatement(sql);
					pst.setString(1, nombre);
					pst.setDouble(2, precio);
					pst.setInt(3, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Data Update Success");
					clear();
					loadData();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

		});

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(148, 297, 117, 25);
		frame.getContentPane().add(btnEliminar);
		
		textFieldId = new JTextField();
		textFieldId.setEditable(false);
		textFieldId.setBounds(12, 150, 114, 19);
		frame.getContentPane().add(textFieldId);
		textFieldId.setColumns(10);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(12, 123, 70, 15);
		frame.getContentPane().add(lblId);
		
		
		JButton btnCobrar = new JButton("Cobrar");
		btnCobrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int uds = Integer.parseInt(textFieldUds.getText());
				int idplato = (int) comboPlatos.getSelectedItem();
				try {
					//Controlar que el text field de las unidades este rellenado
					connect();
					String sql = "select precio as precioplato from carta where id_comida = ?";//poner nombres igual que en la base de datos
					pst = con.prepareStatement(sql);
					pst.setInt(1, idplato);
					rs = pst.executeQuery();
					rs.next();
					double precio = rs.getDouble("precioplato");
					double total = precio * uds;
					JOptionPane.showMessageDialog(null, "El precio total es"+total+" .");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnCobrar.setBounds(551, 218, 91, 25);
		frame.getContentPane().add(btnCobrar);
		
		
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int id = Integer.parseInt(textFieldId.getText());
		
					int result = JOptionPane.showConfirmDialog(null, "Estas seguro de querer eliminar", "Eliminar",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.YES_OPTION) {
						try {
							connect();
							String sql = "delete from carta where id_comida = ?";
							pst = con.prepareStatement(sql);
							pst.setInt(1, id);
							pst.executeUpdate();
							JOptionPane.showMessageDialog(null, "Borrado correctamente");
							clear();
							loadData();

						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					
				}

			}
		});
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
				clear();
				loadData();
			}
		});
	}
		}
		

