package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ModelsManager;
import ru.nsu.ccfit.khassina.factory.PauseLockCounter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Control panel class
 */
final class ControlPanel extends JPanel
{
    private static final int COLUMNS = 3;
    private static final int HORIZONTAL_SPACE = 20;
    private static final int VERTICAL_SPACE = 30;
    private static final int LABEL_HORIZONTAL_SHIFT = 50;
    private static final int BODIES_INITIAL_TIME = 100;
    private static final int ENGINES_INITIAL_TIME = 100;
    private static final int ACCESSORIES_INITIAL_TIME = 300;
    private static final int DEALERS_INITIAL_TIME = 2000;
    
    ControlPanel(ModelsManager manager, PauseLockCounter pauseLock)
    {
        setLayout(new GridLayout(0, COLUMNS, HORIZONTAL_SPACE, VERTICAL_SPACE));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Control (milliseconds)"));

        Border border = BorderFactory.createEmptyBorder(0, LABEL_HORIZONTAL_SHIFT, 0, LABEL_HORIZONTAL_SHIFT);
        String[] texts =  new String[]{"bodies delay", "engines delay", "accessories delay"};
        for(String text: texts)
        {
            JLabel label = new JLabel(text);
            label.setBorder(border);
            add(label);
        }

        JSlider slider = getNewSlider(BODIES_INITIAL_TIME);
        SupplierSliderListener listener = new SupplierSliderListener(manager.getBodiesSupplier());
        slider.addChangeListener(listener);
        add(slider);

        slider = getNewSlider(ENGINES_INITIAL_TIME);
        listener = new SupplierSliderListener(manager.getEnginesSupplier());
        slider.addChangeListener(listener);
        add(slider);

        slider = getNewSlider(ACCESSORIES_INITIAL_TIME);
        AccessorySupplierSliderListener accessoryListener = new AccessorySupplierSliderListener(manager);
        slider.addChangeListener(accessoryListener);
        add(slider);

        JLabel label = new JLabel("dealers delay");
        label.setBorder(border);
        add(label);
        add(new JLabel(""));
        add(new JLabel(""));

        slider = getNewSlider(DEALERS_INITIAL_TIME);
        DealersSliderListener dealersListener = new DealersSliderListener(manager);
        slider.addChangeListener(dealersListener);
        add(slider);

        JButton stopButton = new JButton("Stop producing details");
        StopButtonListener stopButtonListener = new StopButtonListener(manager);
        stopButton.addActionListener(stopButtonListener);
        add(stopButton);

        JButton continueButton = new JButton("Continue producing details");
        ContinueButtonListener continueButtonListener = new ContinueButtonListener(pauseLock, manager);
        continueButton.addActionListener(continueButtonListener);
        add(continueButton);
    }

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 2000;
    private static final int MAJOR_TICK_SPACING = 1000;
    private static final int MINOR_TICK_SPACING = 100;

    private JSlider getNewSlider(int initialTime)
    {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, initialTime);
        slider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        slider.setMinorTickSpacing(MINOR_TICK_SPACING);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setForeground(Color.BLUE);

        return slider;
    }
}
