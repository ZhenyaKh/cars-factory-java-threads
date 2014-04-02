package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.Supplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Slider listener for suppliers
 */
final class SupplierSliderListener implements ChangeListener
{
    private final Supplier supplier;

    SupplierSliderListener(Supplier supplier)
    {
        assert null != supplier;

        this.supplier = supplier;
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
            supplier.setWaitingTime(time);
        }
    }
}
