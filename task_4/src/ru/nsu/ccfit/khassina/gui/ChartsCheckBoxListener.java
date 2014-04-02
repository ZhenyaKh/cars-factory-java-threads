package ru.nsu.ccfit.khassina.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class that reacts on stores check boxes states
 */
final class ChartsCheckBoxListener implements ActionListener
{
    private final ChartsPanel chartsPanel;
    private final JCheckBox bodies;
    private final JCheckBox engines;
    private final JCheckBox accessories;
    private final JCheckBox cars;
    
    private static final int STORES_NUMBER = 4;
    private static final int BODIES_CHECK_BOX_NUMBER = 0;
    private static final int ENGINES_CHECK_BOX_NUMBER = 1;
    private static final int ACCESSORIES_CHECK_BOX_NUMBER = 2;
    private static final int CARS_CHECK_BOX_NUMBER = 12;
    
    ChartsCheckBoxListener(ChartsPanel chartsPanel, InfoPanel panel)
    {
        assert null != chartsPanel;

        this.chartsPanel = chartsPanel;
        bodies = (JCheckBox) panel.getComponent(BODIES_CHECK_BOX_NUMBER);
        engines = (JCheckBox) panel.getComponent(ENGINES_CHECK_BOX_NUMBER);
        accessories = (JCheckBox) panel.getComponent(ACCESSORIES_CHECK_BOX_NUMBER);
        cars = (JCheckBox) panel.getComponent(CARS_CHECK_BOX_NUMBER);

        bodies.addActionListener(this);
        engines.addActionListener(this);
        accessories.addActionListener(this);
        cars.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        Boolean[] checkBoxesSemaphore = new Boolean[STORES_NUMBER];

        checkBoxesSemaphore[ChartsPanel.BODIES_CHECK_BOX] = bodies.isSelected();
        checkBoxesSemaphore[ChartsPanel.ENGINES_CHECK_BOX] = engines.isSelected();
        checkBoxesSemaphore[ChartsPanel.ACCESSORIES_CHECK_BOX] = accessories.isSelected();
        checkBoxesSemaphore[ChartsPanel.CARS_CHECK_BOX] = cars.isSelected();

        chartsPanel.setCheckBoxesSemaphore(checkBoxesSemaphore);
    }    
}
