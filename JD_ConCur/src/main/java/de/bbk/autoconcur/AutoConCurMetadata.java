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
package de.bbk.autoconcur;

import ec.nbdemetra.sa.MultiProcessingManager;
import ec.nbdemetra.sa.SaBatchUI;
import ec.nbdemetra.ui.Menus;
import ec.nbdemetra.ws.actions.AbstractViewAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Jan Gerhardt
 */
@ActionID(
        category = "SaProcessing",
        id = "de.bbk.autoconcur.Metadata"
)
@ActionRegistration(
        displayName = "#CTL_AutoConCurMetadata",
        lazy = false
)
@ActionReference(path = MultiProcessingManager.LOCALPATH, position = 1)
@NbBundle.Messages({"CTL_AutoConCurMetadata=CompRes Recommendations"})
public class AutoConCurMetadata extends AbstractViewAction<SaBatchUI> implements Presenter.Popup {

    public static final String PATH = "/AutoConCurMetadata";

    public AutoConCurMetadata() {
        super(SaBatchUI.class);
    }

    @Override
    protected void refreshAction() {
        setEnabled(context() != null && context().getSelectionCount() > 0);
    }

    @Override
    protected void process(SaBatchUI cur) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        refreshAction();
        JMenu menu = new JMenu(Bundle.CTL_AutoConCurMetadata());
        menu.setEnabled(enabled);
        Menus.fillMenu(menu, MultiProcessingManager.LOCALPATH + PATH);
        return menu;
    }

}
