/***************************************************************************
 * Copyright (c) 2016 the WESSBAS project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
***************************************************************************/

package net.voorn.markov4jmeter.control.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.voorn.markov4jmeter.control.BehaviorMix;
import net.voorn.markov4jmeter.control.BehaviorMixEntry;
import net.voorn.markov4jmeter.control.MarkovController;
import net.voorn.markov4jmeter.util.Markov4JMeterVersion;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * GUI element to create and modify a MarkovController.
 *
 * @author Andr&eacute; van Hoorn
 */
public class MarkovControllerGui extends org.apache.jmeter.control.gui.AbstractControllerGui implements Clearable, ItemListener, ActionListener, GuiLogger {

    /** Command for removing a row from the table. */
    private static final String CHECK = "check"; // $NON-NLS-1$

    /** A panel allowing the user to define the user behavior mix. */
    private BehaviorMixPanel behaviorMixPanel;

    /** A panel to contain comments on the application state. */
    private JTextArea commentPanel;

    /** The check box to enable/disable the arrival controller. */
    private JCheckBox enableArrivalCtrl;

    /** The panel to contain the arrival controller properties. */
    private VerticalPanel arrivalCtrlPropsPanel;

    /** The check box for the arrival formula. */
    private JTextField arrivalFormula;

    /* The check box to enable/disable logging. * */
    private JCheckBox enableLogging;

    /** The panel to conatain session arrival controller logging properties. */
    private JPanel arrivalCtrlLoggingPanel;

    /** The text field to contain the session arrival controller log filename. */
    private JTextField logFilename;

    private JTextArea logTextArea;

    /** Creates a new instance of ApplicationControllerGUI */
    public MarkovControllerGui() {
        init();
    }

    /** @see org.apache.jmeter.control.gui.AbstractControllerGui#getStaticLabel */
    @Override
    public String getStaticLabel(){
        return "Markov Session Controller";
    }

    /** @see org.apache.jmeter.control.gui.AbstractControllerGui#getLabelResource */
    @Override
    public String getLabelResource() {
        return "markov_session_controller";
    }

    /** @see org.apache.jmeter.control.gui.AbstractControllerGui#createTestElement */
    @Override
    public MarkovController createTestElement() {
        MarkovController ctrl = new MarkovController();
        super.configureTestElement(ctrl);
        MarkovControllerTreeListener.registerListener();
        MarkovControllerTreeListener.getInstance().registerController(ctrl);
        return ctrl;
    }

    /** @see org.apache.jmeter.control.gui.AbstractControllerGui#configure */
    @Override
    public void configure(TestElement elt) {
        super.configure(elt);
        if (elt instanceof MarkovController) {
            MarkovController ctrl = (MarkovController) elt;
            commentPanel.setText(ctrl.getPropertyAsString(MarkovController.COMMENTS));
            behaviorMixPanel.setBehaviorMix(ctrl.getBehaviorMix());
            behaviorMixPanel.setStateNames(ctrl.getStateNames());
            enableArrivalCtrl.setSelected(ctrl.isArrivalCtrlEnabled());
            arrivalCtrlPropsPanel.setVisible(enableArrivalCtrl.isSelected());
            arrivalFormula.setText(ctrl.getArrivalCtrlNumSessions());
            enableLogging.setSelected(ctrl.isArrivalCtrlLoggingEnabled());
            arrivalCtrlLoggingPanel.setVisible(enableLogging.isSelected());
            logFilename.setText(ctrl.getArrivalCtrlLogfile());
        }
    }

    /** @see org.apache.jmeter.control.gui.AbstractControllerGui#modifyTestElement */
    @Override
    public void modifyTestElement(TestElement elt) {
        configureTestElement(elt);
        if (elt instanceof MarkovController) {
            MarkovController ctrl = (MarkovController) elt;
            ctrl.setProperty(MarkovController.COMMENTS, commentPanel.getText());
            ctrl.setBehaviorMix(behaviorMixPanel.getBehaviorMix());
            ctrl.setArrivalCtrlEnabled(enableArrivalCtrl.isSelected());
            ctrl.setArrivalCtrlNumSessions(arrivalFormula.getText());
            ctrl.setArrivalCtrlLoggingEnabled(enableLogging.isSelected());
            ctrl.setArrivalCtrlLogfile(logFilename.getText());
        }
    }

    /**
     * Called when states of the two check boxes change.
     *
     * @param ie the change event.
     */
    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getItem().equals(enableArrivalCtrl)) {
            if (enableArrivalCtrl.isSelected()) {
                arrivalCtrlPropsPanel.setVisible(true);
            } else {
                arrivalCtrlPropsPanel.setVisible(false);
            }
        }
        if (ie.getItem().equals(enableLogging)) {
            if (enableLogging.isSelected()) {
                arrivalCtrlLoggingPanel.setVisible(true);
            } else {
                arrivalCtrlLoggingPanel.setVisible(false);
            }
        }
    }

    /**
     * Called when the button is clicked.
     *
     * @param e the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals(CHECK)) {
            GuiPackage.showErrorMessage("Not yet implemented","Error");
        }
    }

    /**
     * Creates the comment panel.
     *
     * @return the comment panel.
     */
    private Container createCommentPanel() {

        Container panel = makeTitlePanel();
        commentPanel = new JTextArea();
        JLabel label = new JLabel(JMeterUtils.getResString("testplan_comments"));
        label.setLabelFor(commentPanel);
        // JMeter GUI components already have a text input field by default;
        // leave the lines above as they are, otherwise the comment panel will
        // be packed to minimum size -> components will be hidden;
        //        panel.add(label);
        //        panel.add(commentPanel);
        return panel;
    }

    /**
     * Create a panel allowing the user to define a user behavior mix.
     *
     * @return the panel.
     */
    private JPanel createBehaviorMixPanel() {
        behaviorMixPanel = new BehaviorMixPanel("Behavior Mix");

        return behaviorMixPanel;
    }

    /**
     * Create a session arrival controller panel.
     *
     * @return the panel.
     */
    private JPanel createArrivalCtrlPanel(){
        VerticalPanel arrivalCtrlPanel = new VerticalPanel();

        enableArrivalCtrl = new JCheckBox("Session Arrival Controller");
        enableArrivalCtrl.addItemListener(this);
        arrivalCtrlPanel.add(enableArrivalCtrl);

        arrivalCtrlPropsPanel = new VerticalPanel();
        arrivalCtrlPropsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Active Sessions Properties"));

        JPanel arrivalCtrlFormularPanel = new JPanel(new BorderLayout(5, 0));
        JLabel arrivalFormulaLabel = new JLabel("Maximum Number");
        arrivalCtrlFormularPanel.add(arrivalFormulaLabel, BorderLayout.WEST);
        arrivalFormula = new JTextField("sin(t)", 5);
        arrivalFormulaLabel.setLabelFor(arrivalFormula);
        arrivalCtrlFormularPanel.add(arrivalFormula, BorderLayout.CENTER);
        JButton checkFormular = new JButton("Check");
        checkFormular.setActionCommand(CHECK);
        checkFormular.addActionListener(this);
        //arrivalCtrlFormularPanel.add(checkFormular, BorderLayout.EAST);
        arrivalCtrlPropsPanel.add(arrivalCtrlFormularPanel);

        enableLogging = new JCheckBox("Logging");
        enableLogging.addItemListener(this);
        arrivalCtrlPropsPanel.add(enableLogging);

        arrivalCtrlLoggingPanel = new JPanel(new BorderLayout(5, 0));
        JLabel arrivalLoggingLabel = new JLabel("Filename");
        arrivalCtrlLoggingPanel.add(arrivalLoggingLabel, BorderLayout.WEST);
        logFilename = new JTextField("", 5);
        arrivalLoggingLabel.setLabelFor(logFilename);
        arrivalCtrlLoggingPanel.add(logFilename, BorderLayout.CENTER);
        arrivalCtrlPropsPanel.add(arrivalCtrlLoggingPanel);

        arrivalCtrlPanel.add(arrivalCtrlPropsPanel);
        arrivalCtrlPropsPanel.setVisible(false);
        arrivalCtrlLoggingPanel.setVisible(false);

        return arrivalCtrlPanel;
    }

    private JPanel createLogPanel () {

        final JPanel panel = new JPanel();
        final JPanel buttonPanel = new JPanel();
        final JTextArea textArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(textArea);
        final JButton clearButton = new JButton("Clear");

        panel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(clearButton);

        textArea.setEditable(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Behavior Mix Log"));
        scrollPane.setPreferredSize(scrollPane.getMinimumSize());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (final ActionEvent e) {

                textArea.setText("");
            }
        });

        this.logTextArea = textArea;
        return panel;
    }

    /**
     * Initialize the components and layout of this component.
     */
    private void init() {
        setLayout(new BorderLayout(10, 10));
        setBorder(makeBorder());

        add(createCommentPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));  // 2 rows, 1 column;
        centerPanel.add(createBehaviorMixPanel());
        centerPanel.add(createLogPanel());
        add(centerPanel, BorderLayout.CENTER);

        VerticalPanel footer = new VerticalPanel(); // just to add the version info
        JLabel versionInfo = new JLabel("Markov4JMeter version: " + Markov4JMeterVersion.getVERSION());
        footer.add(createArrivalCtrlPanel());
        footer.add(versionInfo);
        add(footer, BorderLayout.SOUTH);

        MarkovControllerTreeListener.registerListener();
        BehaviorMixEntry.setGuiLogger(this);
        BehaviorMix.setGuiLogger(this);
    }

    @Override
    public void info (final String message) {

        if (this.logTextArea != null) {

            this.logTextArea.append("INFO: " + message + "\r\n");
        }
    }

    @Override
    public void warn (final String message) {

        if (this.logTextArea != null) {

            this.logTextArea.append("WARNING: " + message + "\r\n");
        }
    }

    @Override
    public void error (final String message) {

        if (this.logTextArea != null) {

            this.logTextArea.append("ERROR: " + message + "\r\n");
        }
    }

    @Override
    // TODO: toolbar button actions: only "clear" action will invoke this
    // method; "clear all" action has no effect yet;
    public void clearData() {

        this.logTextArea.setText("");
    }
}
