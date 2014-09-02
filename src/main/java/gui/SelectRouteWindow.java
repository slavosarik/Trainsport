package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

/**
 * Graficke rozhranie pre sekundarne okno pre vyber trasy
 * 
 * @author Slavomir Sarik
 * 
 */
public class SelectRouteWindow {

	private JFrame frame;
	private JTable table;
	private JButton btnShowDetails;
	private Logger logger = Logger.getLogger(SelectRouteWindow.class);

	/**
	 * Vytvorenie okna
	 */
	public SelectRouteWindow() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Vykreslenie okna
	 */
	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException: ", e);
		} catch (InstantiationException e) {
			logger.error("Instantiation: ", e);
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccess: ", e);
		} catch (UnsupportedLookAndFeelException e) {
			logger.error("UnsupportedLookAndFeel: ", e);
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 727, 407);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 0, 0, 10, 0 };
		gridBagLayout.rowHeights = new int[] { 10, 10, 10, 10, 10, 0, 10, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblVberSpojenia = new JLabel("V\u00FDber spojenia");
		lblVberSpojenia.setFont(new Font("Franklin Gothic Medium", Font.PLAIN,
				24));
		GridBagConstraints gbc_lblVberSpojenia = new GridBagConstraints();
		gbc_lblVberSpojenia.fill = GridBagConstraints.VERTICAL;
		gbc_lblVberSpojenia.anchor = GridBagConstraints.WEST;
		gbc_lblVberSpojenia.insets = new Insets(0, 0, 5, 5);
		gbc_lblVberSpojenia.gridx = 1;
		gbc_lblVberSpojenia.gridy = 1;
		frame.getContentPane().add(lblVberSpojenia, gbc_lblVberSpojenia);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setBorder(border);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);

		btnShowDetails = new JButton("Zobrazi\u0165 detaily");
		GridBagConstraints gbc_btnVybra = new GridBagConstraints();
		gbc_btnVybra.insets = new Insets(0, 0, 5, 5);
		gbc_btnVybra.gridx = 2;
		gbc_btnVybra.gridy = 5;
		frame.getContentPane().add(btnShowDetails, gbc_btnVybra);
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnShowDetails() {
		return btnShowDetails;
	}
}