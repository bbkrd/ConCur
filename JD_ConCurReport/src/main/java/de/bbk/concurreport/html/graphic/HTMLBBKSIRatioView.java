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

import de.bbk.concur.util.SIViewSaved;
import de.bbk.concurreport.html.HTML2Div;
import de.bbk.concurreport.html.HTMLBBKBox;
import de.bbk.concurreport.html.HTMLByteArrayOutputStream;
import ec.tss.html.AbstractHtmlElement;
import ec.tss.html.HtmlStream;
import ec.tss.html.HtmlTag;
import ec.tss.sa.documents.SaDocument;
import ec.tss.sa.documents.X13Document;
import ec.util.chart.swing.Charts;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author Christiane Hofer
 */
public class HTMLBBKSIRatioView extends AbstractHtmlElement {

    private final SaDocument doc;
    private static final int WIDTH = 450, HEIGHT = 250;

    public HTMLBBKSIRatioView(SaDocument doc) {
        this.doc = doc;
    }

    @Override
    public void write(HtmlStream stream) throws IOException {

        SIViewSaved sIViewSaved = new SIViewSaved();
        if (!sIViewSaved.setDoc(doc)) {
            sIViewSaved.dispose();
            return;
        }

        sIViewSaved.setSize(new Dimension(WIDTH, HEIGHT));
        sIViewSaved.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        sIViewSaved.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        sIViewSaved.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JPanel jPanel = new JPanel();
        jPanel.add(sIViewSaved);

        jPanel.setSize(WIDTH, HEIGHT);
        stream.write(HtmlTag.HEADER2, "S-I-Ratio");
        sIViewSaved.doLayout();
        int frequency = doc.getSeries().getDomain().getFrequency().getAsInt();
        HTMLBBKBox leftBox = new HTMLBBKBox();
        HTMLBBKBox rightBox = new HTMLBBKBox();

        for (int i = 0; i < frequency; i++) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Charts.writeChartAsSVG(os, sIViewSaved.getDetailChart(i), WIDTH, HEIGHT);
            HTMLByteArrayOutputStream arrayOutputStream = new HTMLByteArrayOutputStream(os);
            if (i % 2 == 0) {
                leftBox.add(arrayOutputStream);
            } else {
                rightBox.add(arrayOutputStream);
            }

        }
        HTML2Div div = new HTML2Div(leftBox, rightBox);

        div.write(stream);

        String description;
        if (doc instanceof X13Document) {
            description = "Dots - D8, Cross - D9, red - current D10, blue - D10";
        } else {
            description = "Dots - SI, red - SF current, blue - SF new";
        }

        stream.write(
                "<p style='text-align:center; '>")
                .write(description)
                .write("</p>")
                .newLine();
        sIViewSaved.dispose();

        jPanel.removeAll();
    }
}
