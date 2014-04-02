package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ModelsManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Slider listener for accessories suppliers
 */
final class AccessorySupplierSliderListener implements ChangeListener
{
    private final ModelsManager manager;

    AccessorySupplierSliderListener(ModelsManager manager)
    {
        assert null != manager;

        this.manager = manager;
    }

    public void stateChanged(ChangeEvent event)
    {
        JSlider slider = (JSlider)event.getSource();
        if(slider.getValueIsAdjusting())
        {
            slider.setForeground(Color.RED);
        }
        else
        {

            slider.setForeground(Color.BLUE);
            int time = slider.getValue() ;
            manager.getAccessoriesSuppliers().setWaitingTimeForAll(time);
        }
    }
}
