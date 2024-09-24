import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import javax.swing.JFrame;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;


public class Histogram {

    /**
     * creates a bar chart(histogram)
     * @param chartTitle the Title of the chart
     * @param dataArray The data used in the bar chart
     * @return a JFreeChart object consisting of all the data needed in the histogram.
     */
    public JFreeChart getBarChart(String chartTitle, HashMap<String, Double> dataArray) {
        // Create a dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Loop through the HashMap and add the card names and their costs to the dataset
        for (String cardName : dataArray.keySet()) {
            double cost = dataArray.get(cardName);  // Get the cost value from the HashMap
            dataset.addValue(cost, "Cost", cardName);  // "Cost" is the series label, cardName is the category
        }

        // Create the bar chart with the dataset

        return ChartFactory.createBarChart(
                chartTitle,        // Chart title
                "Card Name",       // X-axis label (card names)
                "Cost",            // Y-axis label (cost values)
                dataset,           // Dataset
                PlotOrientation.VERTICAL,  // Bar orientation
                true,              // Include legend
                true,              // Use tooltips
                false              // Use URLs
        );
    }
}