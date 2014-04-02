package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.Dealer;
import ru.nsu.ccfit.khassina.factory.ModelsManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Slider listener for dealers
 */
public class DealersSliderListener implements ChangeListener
{
    private ModelsManager manager;

    DealersSliderListener(ModelsManager manager)
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
            ArrayList<Dealer> dealers = manager.getDealers();
            for(Dealer dealer: dealers)
            {
                dealer.setWaitingTime(time);
            }
        }
    }
}
