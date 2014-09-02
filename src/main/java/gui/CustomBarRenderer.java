package gui;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 * Trieda pre upravu vizualnej stranky stlpcov v grafe
 * 
 * @author Slavomir Sarik
 * 
 */
public class CustomBarRenderer extends BarRenderer {

	private static final long serialVersionUID = 7871879393999195752L;

	/**
	 * Metoda na zmenu farby stlpca podla hodnoty
	 * 
	 * @param row
	 *            cislo riadku
	 * @param column
	 *            cislo stlpca
	 */
	@Override
	public Paint getItemPaint(int row, int column) {
		final CategoryDataset cd = getPlot().getDataset();
		final String l_rowKey = (String) cd.getRowKey(row);
		final String l_colKey = (String) cd.getColumnKey(column);
		final double l_value = cd.getValue(l_rowKey, l_colKey).doubleValue();

		if (l_value < 5) {
			return Color.GREEN;
		} else {
			if (l_value < 15) {
				return Color.YELLOW;
			} else {
				if (l_value < 30) {
					return Color.ORANGE;
				} else {
					return Color.RED;
				}

			}
		}
	}
}