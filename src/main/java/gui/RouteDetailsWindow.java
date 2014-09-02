package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Graficke rozhranie pre vykreslenie okna pre detaily o trase
 * 
 * @author Slavomir Sarik
 * 
 */

public class RouteDetailsWindow {

	private JFrame frame;
	private JTable table;
	private JLabel lblGooglemap;
	private JLabel lblRouteDetails;
	private JLabel lblDelay;
	private JScrollPane scrollPane_2;
	private Logger logger = Logger.getLogger(RouteDetailsWindow.class);

	/**
	 * Vytvorenie okna
	 */
	public RouteDetailsWindow() {
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
		frame.setBounds(100, 100, 993, 976);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 650, 10, 550, 10, 0 };
		gridBagLayout.rowHeights = new int[] { 10, 10, 10, 500, 5, 10, 5, 300,
				10, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
				0.0, 1.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		lblRouteDetails = new JLabel("Detaily spojenia:");
		lblRouteDetails.setFont(new Font("Franklin Gothic Medium", Font.PLAIN,
				24));
		GridBagConstraints gbc_lblDetailySpojenia = new GridBagConstraints();
		gbc_lblDetailySpojenia.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDetailySpojenia.insets = new Insets(0, 0, 5, 5);
		gbc_lblDetailySpojenia.gridx = 1;
		gbc_lblDetailySpojenia.gridy = 1;
		frame.getContentPane().add(lblRouteDetails, gbc_lblDetailySpojenia);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		scrollPane.setBorder(null);

		lblGooglemap = new JLabel("");
		scrollPane.setViewportView(lblGooglemap);

		JScrollPane scrollPane_1 = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 3;
		gbc_scrollPane_1.gridy = 3;
		frame.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane_1.setBorder(border);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		lblDelay = new JLabel("Priemern\u00E9 me\u0161kanie:");
		lblDelay.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 20));
		GridBagConstraints gbc_lblMekanie = new GridBagConstraints();
		gbc_lblMekanie.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMekanie.insets = new Insets(0, 0, 5, 5);
		gbc_lblMekanie.gridx = 1;
		gbc_lblMekanie.gridy = 5;
		frame.getContentPane().add(lblDelay, gbc_lblMekanie);

		scrollPane_2 = new JScrollPane();
		scrollPane_2
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_2.setBorder(null);
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.gridwidth = 3;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 1;
		gbc_scrollPane_2.gridy = 7;
		frame.getContentPane().add(scrollPane_2, gbc_scrollPane_2);

	}

	public void createChartBar(DefaultCategoryDataset dataset) {
		DelayChart dc = new DelayChart(null, dataset);
		ChartPanel chPanel = new ChartPanel(dc.getChart());
		chPanel.setPreferredSize(new java.awt.Dimension(600, 250));
		scrollPane_2.setViewportView(chPanel);
	}

	public JTable getTable() {
		return table;
	}

	public JLabel getLblGooglemap() {
		return lblGooglemap;
	}

	public JLabel getLblRouteDetails() {
		return lblRouteDetails;
	}

	public static void main(String[] args) {
		RouteDetailsWindow rw = new RouteDetailsWindow();
		rw.getLblGooglemap()
				.setIcon(new ImageIcon("resources/no_internet.jpg"));
	}
}