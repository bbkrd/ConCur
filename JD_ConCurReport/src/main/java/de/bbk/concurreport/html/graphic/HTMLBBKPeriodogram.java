/*
 * Copyright 2017 Deutsche Bundesbank
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package de.bbk.concurreport.html.graphic;

import de.bbk.concurreport.BbkPeriodogramView;
import ec.tss.Ts;
import ec.tss.documents.DocumentManager;
import ec.tss.html.AbstractHtmlElement;
import ec.tss.html.HtmlStream;
import ec.tss.sa.documents.SaDocument;
import ec.tstoolkit.modelling.ModellingDictionary;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.util.chart.swing.Charts;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Christiane Hofer
 */
public class HTMLBBKPeriodogram extends AbstractHtmlElement {

    private final TsData tsData;
    private static final int WIDTH = 450;
    private static final int HEIGHT = 220; //450

    /**
     *
     * @param tsData the time series is differenced in the write
     */
    public HTMLBBKPeriodogram(SaDocument doc) {
        Ts ts = DocumentManager.instance.getTs(doc, ModellingDictionary.SA);
        this.tsData = ts.getTsData();
    }

    @Override
    public void write(HtmlStream stream) throws IOException {

        BbkPeriodogramView pView = new BbkPeriodogramView();
        int freq = this.tsData.getFrequency().intValue();
        pView.setLimitVisible(false);
        pView.setDifferencingOrder(1);
        pView.setData("Periodogram (Seasonally Adjusted stationary)", freq, tsData);
        pView.setSize(WIDTH, HEIGHT);

        pView.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        pView.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        pView.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        pView.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pView.doLayout();

        ByteArrayOutputStream os;
        os = new ByteArrayOutputStream();

        JFreeChart jfc = pView.getChartPanel().getChart();
        Charts.writeChartAsSVG(os, jfc, WIDTH, HEIGHT);
        stream.write(os.toString());
        pView.dispose();
    }
}
