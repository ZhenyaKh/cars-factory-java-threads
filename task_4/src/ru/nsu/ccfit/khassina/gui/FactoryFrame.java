package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ModelsManager;
import ru.nsu.ccfit.khassina.factory.PauseLockCounter;

import javax.swing.*;
import java.awt.*;

/**
 * Frame of factory
 */
public final class FactoryFrame extends JFrame
{
    private final static int TWO_ROWS = 2;

    private final ChartsPanel chartsPanel;

    public FactoryFrame(ModelsManager manager, PauseLockCounter pauseLockCounter)
    {
        setResizable(false);
        setTitle("Cars factory");

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = TWO_ROWS;
        chartsPanel = new ChartsPanel(manager);
        layout.setConstraints(chartsPanel, constraints);
        getContentPane().add(chartsPanel);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        InfoPanel infoPanel = new InfoPanel(manager, chartsPanel);
        layout.setConstraints(infoPanel, constraints);
        getContentPane().add(infoPanel);

        constraints.gridx = 0;
        constraints.gridy = 1;
        ControlPanel controlPanel = new ControlPanel(manager, pauseLockCounter);
        layout.setConstraints(controlPanel, constraints);
        getContentPane().add(controlPanel);

        pack();
    }

    ChartsPanel getChartsPanel()
    {
        return chartsPanel;
    }
}
