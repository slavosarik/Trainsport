package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

/**
 * Graficke rozhranie pre hlavne okno
 * 
 * @author Slavomir Sarik
 * 
 */

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 7963043735196726453L;
	private JFrame frame;
	private JComboBox<String> comboBoxStationFrom;
	private JComboBox<String> comboBoxStationTo;
	private JSpinner spinnerArrivalTime;
	private JButton btnSearch;
	private JButton btnReset;
	private JMenuItem mntmUpdateDatabase;
	private List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
	private Logger logger = Logger.getLogger(MainWindow.class);

	/**
	 * Konstruktor pre incializaciu a vykreslenie okna
	 */
	public MainWindow() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * vrati zoznam checkboxov
	 * 
	 * @return zoznam checkboxov
	 */
	public List<JCheckBox> getCheckboxes() {
		return checkboxes;
	}

	/**
	 * Inicializuje obsah okna
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
		frame.setBounds(100, 100, 460, 520);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 100, 0, 140, 142, 10, 0 };
		gridBagLayout.rowHeights = new int[] { 10, 45, 31, 0, 20, 10, 10, 20,
				10, 10, 0, 0, 37, 0, 0, 10, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 1.0,
				0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblSpojenie = new JLabel("Spojenie");
		lblSpojenie.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 24));
		GridBagConstraints gbc_lblSpojenie = new GridBagConstraints();
		gbc_lblSpojenie.gridwidth = 3;
		gbc_lblSpojenie.fill = GridBagConstraints.BOTH;
		gbc_lblSpojenie.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpojenie.gridx = 1;
		gbc_lblSpojenie.gridy = 1;
		frame.getContentPane().add(lblSpojenie, gbc_lblSpojenie);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		separator.setPreferredSize(new Dimension(5, 1));
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.weighty = 1;
		gbc_separator.gridwidth = 4;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 2;
		frame.getContentPane().add(separator, gbc_separator);

		JLabel lblVchodziaStanica = new JLabel("V\u00FDchodzia stanica");
		GridBagConstraints gbc_lblVchodziaStanica = new GridBagConstraints();
		gbc_lblVchodziaStanica.fill = GridBagConstraints.BOTH;
		gbc_lblVchodziaStanica.insets = new Insets(0, 0, 5, 5);
		gbc_lblVchodziaStanica.gridx = 1;
		gbc_lblVchodziaStanica.gridy = 3;
		frame.getContentPane().add(lblVchodziaStanica, gbc_lblVchodziaStanica);

		comboBoxStationFrom = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.BOTH;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 3;
		frame.getContentPane().add(comboBoxStationFrom, gbc_comboBox);

		JLabel lblCieovStanica = new JLabel("Cie\u013Eov\u00E1 stanica");
		GridBagConstraints gbc_lblCieovStanica = new GridBagConstraints();
		gbc_lblCieovStanica.fill = GridBagConstraints.BOTH;
		gbc_lblCieovStanica.insets = new Insets(0, 0, 5, 5);
		gbc_lblCieovStanica.gridx = 1;
		gbc_lblCieovStanica.gridy = 4;
		frame.getContentPane().add(lblCieovStanica, gbc_lblCieovStanica);

		comboBoxStationTo = new JComboBox<String>();
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.gridwidth = 2;
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.BOTH;
		gbc_comboBox_1.gridx = 3;
		gbc_comboBox_1.gridy = 4;
		frame.getContentPane().add(comboBoxStationTo, gbc_comboBox_1);

		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(5, 1));
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.BOTH;
		gbc_separator_1.weighty = 1;
		gbc_separator_1.gridwidth = 4;
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 1;
		gbc_separator_1.gridy = 6;
		frame.getContentPane().add(separator_1, gbc_separator_1);

		JLabel lblasOdchodu = new JLabel("\u010Cas odchodu");
		GridBagConstraints gbc_lblasOdchodu = new GridBagConstraints();
		gbc_lblasOdchodu.fill = GridBagConstraints.BOTH;
		gbc_lblasOdchodu.insets = new Insets(0, 0, 5, 5);
		gbc_lblasOdchodu.gridx = 1;
		gbc_lblasOdchodu.gridy = 7;
		frame.getContentPane().add(lblasOdchodu, gbc_lblasOdchodu);

		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);
		spinnerArrivalTime = new JSpinner();
		spinnerArrivalTime.setModel(model);
		spinnerArrivalTime.setEditor(new JSpinner.DateEditor(
				spinnerArrivalTime, "HH:mm"));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.BOTH;
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.gridx = 3;
		gbc_spinner.gridy = 7;
		frame.getContentPane().add(spinnerArrivalTime, gbc_spinner);

		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(5, 1));
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.fill = GridBagConstraints.BOTH;
		gbc_separator_2.weighty = 1;
		gbc_separator_2.gridwidth = 4;
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 9;
		frame.getContentPane().add(separator_2, gbc_separator_2);

		JLabel lblKategieVlakovvoliten = new JLabel("Kateg\u00F3rie vlakov: ");
		GridBagConstraints gbc_lblKategieVlakovvoliten = new GridBagConstraints();
		gbc_lblKategieVlakovvoliten.fill = GridBagConstraints.BOTH;
		gbc_lblKategieVlakovvoliten.insets = new Insets(0, 0, 5, 5);
		gbc_lblKategieVlakovvoliten.gridx = 1;
		gbc_lblKategieVlakovvoliten.gridy = 10;
		frame.getContentPane().add(lblKategieVlakovvoliten,
				gbc_lblKategieVlakovvoliten);

		JCheckBox chckbxEurocity = new JCheckBox("Eurocity");
		chckbxEurocity.setName("EC");
		checkboxes.add(chckbxEurocity);
		GridBagConstraints gbc_chckbxEurocity = new GridBagConstraints();
		gbc_chckbxEurocity.fill = GridBagConstraints.BOTH;
		gbc_chckbxEurocity.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxEurocity.gridx = 3;
		gbc_chckbxEurocity.gridy = 10;
		frame.getContentPane().add(chckbxEurocity, gbc_chckbxEurocity);

		JCheckBox chckbxEuronight = new JCheckBox("EuroNight");
		chckbxEuronight.setName("EN");
		checkboxes.add(chckbxEuronight);
		GridBagConstraints gbc_chckbxEuronight = new GridBagConstraints();
		gbc_chckbxEuronight.fill = GridBagConstraints.BOTH;
		gbc_chckbxEuronight.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxEuronight.gridx = 4;
		gbc_chckbxEuronight.gridy = 10;
		frame.getContentPane().add(chckbxEuronight, gbc_chckbxEuronight);

		JCheckBox chckbxIntercity = new JCheckBox("InterCity");
		chckbxIntercity.setName("IC");
		checkboxes.add(chckbxIntercity);
		GridBagConstraints gbc_chckbxIntercity = new GridBagConstraints();
		gbc_chckbxIntercity.fill = GridBagConstraints.BOTH;
		gbc_chckbxIntercity.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxIntercity.gridx = 3;
		gbc_chckbxIntercity.gridy = 11;
		frame.getContentPane().add(chckbxIntercity, gbc_chckbxIntercity);

		JCheckBox chckbxExpres = new JCheckBox("Expres");
		chckbxExpres.setName("Ex");
		checkboxes.add(chckbxExpres);
		GridBagConstraints gbc_chckbxExpres = new GridBagConstraints();
		gbc_chckbxExpres.fill = GridBagConstraints.BOTH;
		gbc_chckbxExpres.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxExpres.gridx = 4;
		gbc_chckbxExpres.gridy = 11;
		frame.getContentPane().add(chckbxExpres, gbc_chckbxExpres);

		JCheckBox chckbxRchlik = new JCheckBox("R\u00FDchlik");
		chckbxRchlik.setName("R");
		checkboxes.add(chckbxRchlik);
		GridBagConstraints gbc_chckbxRchlik = new GridBagConstraints();
		gbc_chckbxRchlik.fill = GridBagConstraints.BOTH;
		gbc_chckbxRchlik.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRchlik.gridx = 3;
		gbc_chckbxRchlik.gridy = 12;
		frame.getContentPane().add(chckbxRchlik, gbc_chckbxRchlik);

		JCheckBox chckbxZrchlen = new JCheckBox("Zr\u00FDchlen\u00FD");
		chckbxZrchlen.setName("Zr");
		checkboxes.add(chckbxZrchlen);
		GridBagConstraints gbc_chckbxZrchlen = new GridBagConstraints();
		gbc_chckbxZrchlen.fill = GridBagConstraints.BOTH;
		gbc_chckbxZrchlen.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxZrchlen.gridx = 4;
		gbc_chckbxZrchlen.gridy = 12;
		frame.getContentPane().add(chckbxZrchlen, gbc_chckbxZrchlen);

		JCheckBox chckbxRex = new JCheckBox("ReX");
		chckbxRex.setName("REx");
		checkboxes.add(chckbxRex);
		GridBagConstraints gbc_chckbxRex = new GridBagConstraints();
		gbc_chckbxRex.fill = GridBagConstraints.BOTH;
		gbc_chckbxRex.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRex.gridx = 3;
		gbc_chckbxRex.gridy = 13;
		frame.getContentPane().add(chckbxRex, gbc_chckbxRex);

		JCheckBox chckbxOsobn = new JCheckBox("Osobn\u00FD");
		chckbxOsobn.setName("Os");
		checkboxes.add(chckbxOsobn);
		GridBagConstraints gbc_chckbxOsobn = new GridBagConstraints();
		gbc_chckbxOsobn.fill = GridBagConstraints.BOTH;
		gbc_chckbxOsobn.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOsobn.gridx = 4;
		gbc_chckbxOsobn.gridy = 13;
		frame.getContentPane().add(chckbxOsobn, gbc_chckbxOsobn);

		btnSearch = new JButton("Vyh\u013Eada\u0165");
		GridBagConstraints gbc_btnVyhada = new GridBagConstraints();
		gbc_btnVyhada.fill = GridBagConstraints.BOTH;
		gbc_btnVyhada.insets = new Insets(0, 0, 5, 5);
		gbc_btnVyhada.gridx = 3;
		gbc_btnVyhada.gridy = 14;
		frame.getContentPane().add(btnSearch, gbc_btnVyhada);

		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 5, 5);
		gbc_btnReset.fill = GridBagConstraints.BOTH;
		gbc_btnReset.gridx = 4;
		gbc_btnReset.gridy = 14;
		frame.getContentPane().add(btnReset, gbc_btnReset);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		mntmUpdateDatabase = new JMenuItem("Update Database");
		mnNewMenu.add(mntmUpdateDatabase);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mnNewMenu.add(mntmNewMenuItem);

		JMenu mnNewMenu_1 = new JMenu("Options");
		menuBar.add(mnNewMenu_1);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mnAbout.add(mntmHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnAbout.add(mntmAbout);
	}

	public JComboBox<String> getCmbBoxStationFrom() {
		return comboBoxStationFrom;
	}

	public JComboBox<String> getCmbBoxStationTo() {
		return comboBoxStationTo;
	}

	public JSpinner getSpinnerArrivalTime() {
		return spinnerArrivalTime;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public JButton getBtnReset() {
		return btnReset;
	}

	public JMenuItem getMenuItemUpdateDatabase() {
		return mntmUpdateDatabase;
	}

	public JFrame getFrame() {
		return frame;
	}
}