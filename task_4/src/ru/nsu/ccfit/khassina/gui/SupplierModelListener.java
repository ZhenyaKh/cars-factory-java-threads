package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ChangeListener;

import javax.swing.*;

/**
 * Listener of car suppliers
 */
final class SupplierModelListener implements ChangeListener
{
    private final JTextField info;

    SupplierModelListener(JTextField info)
    {
        assert null != info;

        this.info = info;
    }

    public void modelChanged()
    {
        synchronized(info)
        {
            Integer currentNumber = Integer.parseInt(info.getText());
            currentNumber += 1;
            info.setText(currentNumber.toString());
        }
    }
}
