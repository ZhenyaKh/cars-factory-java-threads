package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ChangeListener;
import ru.nsu.ccfit.khassina.factory.Store;

import javax.swing.*;
import java.awt.*;

/**
 * Listener of cars stores
 */
final class StoreModelListener implements ChangeListener
{
    private final Store store;
    private final JTextField info;
    
    StoreModelListener(Store store, JTextField info)
    {
        assert null != info;
        assert null != store;

        this.info = info;
        this.store = store;
    }    
    
    public void modelChanged()
    {
        Integer currentNumber = store.getCurrentObjectsNumber();
        info.setBackground(Color.GREEN);
        info.setText(currentNumber.toString());
    }    
}
