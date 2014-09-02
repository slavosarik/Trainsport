package gui;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;

/**
 * Trieda pre vytvorenie grafu
 * 
 * @author Slavomir Sarik
 * 
 */
public class DelayChart extends ApplicationFrame {

	private static final long serialVersionUID = -7874111087076691219L;
	private JFreeChart chart;

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            nazov
	 * @param dataset
	 *            data vo forme datasetu
	 */
	public DelayChart(String title, DefaultCategoryDataset dataset) {
		super(title);
		createChart(dataset);
	}

	/**
	 * Vracia graf
	 * 
	 * @return graf
	 */
	public JFreeChart getChart() {
		return chart;
	}

	/**
	 * Vytvorenie grafu
	 * 
	 * @param dataset
	 *            data vo forme datasetu
	 */
	private void createChart(CategoryDataset dataset) {

		chart = ChartFactory.createBarChart(null, null, null, dataset,
				PlotOrientation.VERTICAL, false, true, true);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(3,
				Color.blue);
		CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
		xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		CustomBarRenderer renderer = new CustomBarRenderer();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE6, TextAnchor.BOTTOM_CENTER));
		renderer.setBaseItemLabelFont(new Font("Tahoma", Font.BOLD, 16));
		chart.getCategoryPlot().setRenderer(renderer);

	}
}