package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.ModelsManager;
import ru.nsu.ccfit.khassina.factory.PauseLockCounter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener of button "continue"
 */
final class ContinueButtonListener implements ActionListener
{
    private final PauseLockCounter pauseLock;
    private final ModelsManager manager;

    ContinueButtonListener(PauseLockCounter pauseLock, ModelsManager manager)
    {
        assert null != pauseLock;
        assert null != manager;

        this.pauseLock = pauseLock;
        this.manager = manager;
    }

    public void actionPerformed(ActionEvent event)
    {
        if(!manager.wereSuppliersStopped())
        {
            return;
        }
        while(!pauseLock.isNull())
        {
            synchronized(pauseLock.getInternalLock())
            {
                try
                {
                    pauseLock.getInternalLock().wait();
                }
                catch(InterruptedException exception)
                {
                    break;
                }
            }
        }
        synchronized(pauseLock)
        {
            pauseLock.relieve();
            manager.turnOnSuppliers();
            pauseLock.notifyAll();
        }
    }
}
