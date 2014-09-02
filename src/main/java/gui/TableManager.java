package gui;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Trieda pre nastavenie tabulky
 * 
 * @author Slavomir Sarik
 * 
 */
public class TableManager {

	/**
	 * nastavenie rozmiestnenia atributov v tabulke
	 * 
	 * @param table
	 *            tabulka
	 */
	public void setTableAlignment(JTable table) {
		JTableHeader header = table.getTableHeader();
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table
				.getTableHeader().getDefaultRenderer();
		header.setDefaultRenderer(renderer);
		renderer.setHorizontalAlignment(JLabel.CENTER);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		int rowNumber = table.getColumnCount();
		for (int i = 0; i < rowNumber; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
	}

}