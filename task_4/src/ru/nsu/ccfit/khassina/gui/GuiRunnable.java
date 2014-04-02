package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.Dealer;
import ru.nsu.ccfit.khassina.factory.FactorySettings;
import ru.nsu.ccfit.khassina.factory.ModelsManager;
import ru.nsu.ccfit.khassina.factory.PauseLockCounter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.FileHandler;

/**
 * Class that is run for gui providing
 */
final public class GuiRunnable implements Runnable
{
    private final ModelsManager manager;
    private final PauseLockCounter pauseLock;
    private final FactorySettings state;
    private final GuiLockNotification guiLockNotification;
    private FactoryFrame frame;

    public GuiRunnable(ModelsManager manager, PauseLockCounter pauseLock, FactorySettings state, GuiLockNotification lock)
    {
        assert null != manager;
        assert null != state;
        assert null != lock;
        
        this.manager = manager;
        this.pauseLock = pauseLock;
        this.state = state;
        this.guiLockNotification = lock;
    }

    public void run()
    {
        synchronized(guiLockNotification.getGuiLock())
        {
            frame = new FactoryFrame(manager, pauseLock);
            guiLockNotification.setGuiLockNotified();
            guiLockNotification.getGuiLock().notifyAll();
        }

        FactoryWindowAdapter frameListener = new FactoryWindowAdapter();
        frame.addWindowListener(frameListener);
        frame.setVisible(true);
    }

    public ChartsPanel getChartsPanel()
    {
        return frame.getChartsPanel();
    }

    private final class FactoryWindowAdapter extends WindowAdapter
    {
        private static final int EXIT_SUCCESS = 0;
        public void windowClosing(WindowEvent event)
        {
            manager.setExitTrue();
            manager.getBodiesSupplier().interruptThread();
            manager.getEnginesSupplier().interruptThread();
            manager.getAccessoriesSuppliers().interruptAll();
            for(Dealer dealer: manager.getDealers())
            {
                dealer.interruptThread();
            }
            manager.getPool().interruptAllThreads();
            manager.getProductionStoreControllerThread().interrupt();
            FileHandler handler = state.getLoggingFile();
            if(null != handler)
            {
                handler.close();
            }
            System.exit(EXIT_SUCCESS);
        }
    }
}
