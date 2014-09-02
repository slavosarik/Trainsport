package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/***
 * Trieda pre vykreslovanie loading animacie
 * 
 * @author Slavomir Sarik
 * 
 */
public class LoadingAnimation {

	private JFrame frame;
	private JLabel lblText;
	private JLabel lblImage;

	/**
	 * Konstruktor - inicializovanie okna pre animaciu
	 */
	public LoadingAnimation() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Inicializuje obsah okna
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 469, 272);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 475, 0 };
		gridBagLayout.rowHeights = new int[] { 190, 10, 0, 10, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		lblImage = new JLabel();
		GridBagConstraints gbc_lblImage = new GridBagConstraints();
		gbc_lblImage.insets = new Insets(0, 0, 5, 0);
		gbc_lblImage.gridx = 0;
		gbc_lblImage.gridy = 0;
		frame.getContentPane().add(lblImage, gbc_lblImage);

		lblText = new JLabel();
		GridBagConstraints gbc_lblText_1 = new GridBagConstraints();
		gbc_lblText_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblText_1.gridx = 0;
		gbc_lblText_1.gridy = 2;
		frame.getContentPane().add(lblText, gbc_lblText_1);

		// Create icons
		Icon icon = new ImageIcon(this.getClass().getResource("/loading.gif"));
		lblImage.setIcon(icon);

		lblText.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 20));
	}

	public JLabel getLblImage() {
		return lblImage;
	}

	public JLabel getLblText() {
		return lblText;
	}

	public JFrame getFrame() {
		return frame;
	}
}