package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ModelsManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *Listener of button "stop"
 */
final class StopButtonListener implements ActionListener
{
    private final ModelsManager manager;

    StopButtonListener(ModelsManager manager)
    {
        assert null != manager;

        this.manager = manager;
    }

    public void actionPerformed(ActionEvent event)
    {
        if(!manager.wereSuppliersStopped())
        {
            manager.getBodiesSupplier().interruptThread();
            manager.getEnginesSupplier().interruptThread();
            manager.getAccessoriesSuppliers().interruptAll();
            manager.stopSuppliers();
        }
    }    
}
