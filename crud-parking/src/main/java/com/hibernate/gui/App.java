package com.hibernate.gui;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.hibernate.dao.PlantaDAO;
import com.hibernate.model.Planta;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class App {

	private JFrame frmPlantas;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;
	private JTable table;
	private JTextField txtId;
	private JTextField txtNumPlanta;
	private JTextField txtPlazas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frmPlantas.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void refrescarHib(PlantaDAO pd, DefaultTableModel model) {
		int s = 0;
		List<Planta> lp = pd.selectAllPlantas();

		model.setRowCount(0);
		for (Planta p : lp) {
			Object[] row = new Object[3];
			row[0] = p.getId();
			row[1] = p.getNumplanta();
			row[2] = p.getPlazas();
			model.addRow(row);
		}
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		PlantaDAO plantaDAO = new PlantaDAO();
		List<Planta> listPlantas = null;

		frmPlantas = new JFrame();
		frmPlantas.setTitle("Parking");
		frmPlantas.setBounds(100, 100, 622, 429);
		frmPlantas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPlantas.getContentPane().setLayout(null);

		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.addColumn("ID");
		model.addColumn("Planta");
		model.addColumn("Plazas");

		listPlantas = plantaDAO.selectAllPlantas();

		for (int i = 0; i < listPlantas.size(); i++) {
			Object[] row = new Object[3];
			row[0] = listPlantas.get(i).getId();
			row[1] = listPlantas.get(i).getNumplanta();
			row[2] = listPlantas.get(i).getPlazas();
			model.addRow(row);
		}
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 12, 513, 108);
		frmPlantas.getContentPane().add(scrollPane);

		table = new JTable(model);
		scrollPane.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				TableModel model = table.getModel();

				txtId.setText(model.getValueAt(index, 0).toString());
				txtNumPlanta.setText(model.getValueAt(index, 1).toString());
				txtPlazas.setText(model.getValueAt(index, 2).toString());
			}
		});

		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setBounds(22, 149, 114, 19);
		frmPlantas.getContentPane().add(txtId);
		txtId.setColumns(10);

		JLabel lblPlazasRestantes = new JLabel("");
		lblPlazasRestantes.setForeground(Color.RED);
		lblPlazasRestantes.setFont(new Font("Dialog", Font.BOLD, 23));
		lblPlazasRestantes.setBounds(58, 320, 477, 44);
		frmPlantas.getContentPane().add(lblPlazasRestantes);

		
		txtNumPlanta = new JTextField();
		txtNumPlanta.setBounds(22, 207, 114, 19);
		frmPlantas.getContentPane().add(txtNumPlanta);
		txtNumPlanta.setColumns(10);

		txtPlazas = new JTextField();
		txtPlazas.setBounds(22, 265, 114, 19);
		frmPlantas.getContentPane().add(txtPlazas);
		txtPlazas.setColumns(10);

		JLabel lblId = new JLabel("Id");
		lblId.setBounds(22, 135, 70, 15);
		frmPlantas.getContentPane().add(lblId);

		JLabel lblNPlanta = new JLabel("Nº Planta");
		lblNPlanta.setBounds(22, 193, 70, 15);
		frmPlantas.getContentPane().add(lblNPlanta);

		JLabel lblPlazas = new JLabel("Plazas");
		lblPlazas.setBounds(22, 251, 70, 15);
		frmPlantas.getContentPane().add(lblPlazas);

		JButton btnInsertar = new JButton("Insertar");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numPlanta;
				int plazas;

				if (txtNumPlanta.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nº de planta vacío", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (txtPlazas.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Plazas vacías", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!txtNumPlanta.getText().matches("^\\d{1,2}$")) {
					JOptionPane.showMessageDialog(null, "El nº de planta ha de ser un número (0-99)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!txtPlazas.getText().matches("^\\d{1,3}$")) {
					JOptionPane.showMessageDialog(null, "Las plazas disponibles han de ser un número (0-999)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					numPlanta = Integer.parseInt(txtNumPlanta.getText());
					plazas = Integer.parseInt(txtPlazas.getText());

					Planta p = new Planta(numPlanta, plazas);
					plantaDAO.insertPlanta(p);
					refrescarHib(plantaDAO, model);
				}
			}
		});
		btnInsertar.setBounds(204, 146, 114, 25);
		frmPlantas.getContentPane().add(btnInsertar);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numPlanta;
				int plazas;
				int id;
				
				if (txtNumPlanta.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nº de planta vacío", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (txtPlazas.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Plazas vacías", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!txtNumPlanta.getText().matches("^\\d{1,2}$")) {
					JOptionPane.showMessageDialog(null, "El nº de planta ha de ser un número (0-99)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!txtPlazas.getText().matches("^\\d{1,3}$")) {
					JOptionPane.showMessageDialog(null, "Las plazas disponibles han de ser un número (0-999)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					id = Integer.parseInt(txtId.getText());
					numPlanta = Integer.parseInt(txtNumPlanta.getText());
					plazas = Integer.parseInt(txtPlazas.getText());
					
					Planta p = plantaDAO.selectPlantaById(id);
					p.setNumplanta(numPlanta);
					p.setPlazas(plazas);
					plantaDAO.updatePlanta(p);
					refrescarHib(plantaDAO, model);
				}
			}
		});
		btnActualizar.setBounds(204, 204, 114, 25);
		frmPlantas.getContentPane().add(btnActualizar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(txtId.getText());
				plantaDAO.deletePlanta(id);
				refrescarHib(plantaDAO, model);
			}
			
		});
		btnEliminar.setBounds(204, 262, 117, 25);
		frmPlantas.getContentPane().add(btnEliminar);

	
			JButton btnAparcar = new JButton("Aparcar");
			btnAparcar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int numPlanta, plazas, id;
					
					if (txtNumPlanta.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Nº de planta vacío", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (txtPlazas.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Plazas vacías", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (!txtNumPlanta.getText().matches("^\\d{1,2}$")) {
						JOptionPane.showMessageDialog(null, "El nº de planta ha de ser un número (0-99)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (Integer.parseInt(txtPlazas.getText()) == 0 ) {
						JOptionPane.showMessageDialog(null, "No quedan plazas disponibles", "Error",
								JOptionPane.ERROR_MESSAGE);
					
					}else {
					 if (Integer.parseInt(txtPlazas.getText()) <= 10 ) {
						lblPlazasRestantes.setText("Quedan pocas plazas disponibles");
					 }else {
						 lblPlazasRestantes.setText("");
					 }
						id = Integer.parseInt(txtId.getText());
						numPlanta = Integer.parseInt(txtNumPlanta.getText());
						plazas = Integer.parseInt(txtPlazas.getText());
						id = Integer.parseInt(txtId.getText());
						numPlanta = Integer.parseInt(txtNumPlanta.getText());
						plazas = Integer.parseInt(txtPlazas.getText());
						
						Planta p = plantaDAO.selectPlantaById(id);
						p.setPlazas(p.getPlazas()- 1);
						plantaDAO.updatePlanta(p);
						refrescarHib(plantaDAO, model);

				}
				}
			});
			btnAparcar.setBounds(369, 175, 117, 25);
			frmPlantas.getContentPane().add(btnAparcar);

			JButton btnLiberar = new JButton("Liberar");
			btnLiberar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int numPlanta, plazas, id;
					
					if (txtNumPlanta.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Nº de planta vacío", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (txtPlazas.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Plazas vacías", "Error", JOptionPane.ERROR_MESSAGE);
					}else {
						if (Integer.parseInt(txtPlazas.getText()) <= 10 ) {
							lblPlazasRestantes.setText("Quedan pocas plazas disponibles");
						 }else {
							 lblPlazasRestantes.setText("");
						 }
						id = Integer.parseInt(txtId.getText());
						numPlanta = Integer.parseInt(txtNumPlanta.getText());
						plazas = Integer.parseInt(txtPlazas.getText());
						
						Planta p = plantaDAO.selectPlantaById(id);
						p.setPlazas(p.getPlazas() + 1);
						plantaDAO.updatePlanta(p);
						refrescarHib(plantaDAO, model);
					}
				}
			});
			btnLiberar.setBounds(369, 233, 117, 25);
			frmPlantas.getContentPane().add(btnLiberar);
			
			
		}
	}

