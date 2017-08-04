/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bbk.outputpdf.html;

import de.bbk.outputpdf.BbkPeriodogramView;
import de.bbk.outputpdf.SVGJComponent;
import ec.tss.html.AbstractHtmlElement;
import ec.tss.html.HtmlStream;
import ec.tss.html.IHtmlElement;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.ui.view.PeriodogramView;
import ec.util.chart.swing.Charts;
import ec.util.chart.swing.JTimeSeriesChart;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

/**
 *
 * @author Christiane Hofer
 */
public class HTMLBBKPeriodogram extends AbstractHtmlElement implements IHtmlElement {

    private TsData tsData;
    private SVGJComponent svgJComponent;
    private final int width = 900;
    private final int height = 450;

    public HTMLBBKPeriodogram(TsData tsData) {
        this.tsData = tsData;

    }

    @Override
    public void write(HtmlStream stream) throws IOException {

        BbkPeriodogramView pView = new BbkPeriodogramView();
        int freq = this.tsData.getFrequency().intValue();
        pView.setLimitVisible(false);
        pView.setDifferencingOrder(0);
        pView.setData("Periodogram", freq, tsData);
        pView.setSize(width, height);

        pView.setMaximumSize(new Dimension(width, height));
        pView.setMinimumSize(new Dimension(width, height));
        pView.setMaximumSize(new Dimension(width, height));
        pView.setPreferredSize(new Dimension(width, height));
        pView.doLayout();

        ByteArrayOutputStream os;
        os = new ByteArrayOutputStream();

        JFreeChart jfc = pView.getChartPanel().getChart();
        Charts.writeChartAsSVG(os, jfc, width, height);
        stream.write(os.toString());
//        JPanel jPanel = new JPanel();
//        jPanel.add(m_pView);
//
//        jPanel.setSize(width, height);
//        jPanel.setMaximumSize(new Dimension(width, height));
//        jPanel.setMinimumSize(new Dimension(width, height));
//        jPanel.setPreferredSize(new Dimension(width, height));
//        jPanel.doLayout();
//
//        svgJComponent = new SVGJComponent(jPanel);
//        svgJComponent.write(stream);

    }

    public void dispose() {
        this.svgJComponent.dispose();
        this.tsData = null;
    }

}
