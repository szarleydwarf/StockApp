package app;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import utillity.FinalVariables;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;

public class WyswietlRachunki {

	private JFrame frame;
	private FinalVariables fv;
	private JTextField tfSearch;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WyswietlRachunki window = new WyswietlRachunki();
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
	public WyswietlRachunki() {
		this.fv = new FinalVariables();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(240, 230, 140));
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Lista wystawionych rachunkow??");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 11, 664, 55);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Powr\u00F3t");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnNewButton.setBounds(459, 533, 89, 23);
		frame.getContentPane().add(btnNewButton);

		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "LISTA WYSTAWIONYCH RACHUNKÓW");
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label.setBorder(border);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBounds(10, 62, 460, 461);
		frame.getContentPane().add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 114, 421, 400);
		frame.getContentPane().add(scrollPane);
		
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		scrollPane.setViewportView(list);
		
		JButton btnRefresh = new JButton("Odśwież");
		btnRefresh.setForeground(new Color(0, 153, 255));
		btnRefresh.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnRefresh.setBackground(new Color(255, 255, 153));
		btnRefresh.setBounds(23, 80, 89, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				populateList();
			}
		});
		frame.getContentPane().add(btnRefresh);
		
		tfSearch = new JTextField();
		tfSearch.setText("wpisz szukaną nazwę");
		tfSearch.setHorizontalAlignment(SwingConstants.CENTER);
		tfSearch.setColumns(10);
		tfSearch.setBounds(121, 79, 195, 24);
		frame.getContentPane().add(tfSearch);
		
		JButton btnSearch = new JButton("Szukaj");
		btnSearch.setForeground(new Color(255, 255, 204));
		btnSearch.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSearch.setBackground(new Color(0, 153, 255));
		btnSearch.setBounds(315, 79, 89, 24);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchInDatabase();
			}
		});
		frame.getContentPane().add(btnSearch);
		
		JButton btnDelete = new JButton("Usuń");
		btnDelete.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btnDelete.setBounds(480, 81, 71, 20);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteRocordFromDatabase();
			}
		});
		frame.getContentPane().add(btnDelete);
		
		
		frame.setBackground(new Color(135, 206, 235));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 574, 606);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		        		fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        	MainView.main(null);
		        }
		    }
		});

	}

	protected void populateList() {
		// TODO Auto-generated method stub
		
	}

	protected void searchInDatabase() {
		// TODO Auto-generated method stub
		
	}

	protected void deleteRocordFromDatabase() {
		// TODO Auto-generated method stub
		
	}
}
