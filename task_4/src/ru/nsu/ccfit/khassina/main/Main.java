package ru.nsu.ccfit.khassina.main;

import ru.nsu.ccfit.khassina.factory.*;
import ru.nsu.ccfit.khassina.gui.GuiLockNotification;
import ru.nsu.ccfit.khassina.gui.GuiRunnable;
import ru.nsu.ccfit.khassina.threadpool.ThreadPool;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;

/**
 * Main class
 */

public final class Main
{
    private static final Object controllerLock = new Object();
    private static final GuiLockNotification guiLockNotification = new GuiLockNotification();
    private static final PauseLockCounter pauseLock = new PauseLockCounter();
    private static final int BODIES_AND_ENGINES_SUPPLIERS = 2;
    private static final int INITIAL_BODIES_SUPPLIER_TIME = 100;
    private static final int INITIAL_ENGINES_SUPPLIER_TIME = 100;
    private static final int INITIAL_ACCESSORIES_SUPPLIER_TIME = 300;
    private static final int INITIAL_DEALERS_TIME = 2000;

    public static void main(String[] arguments)
    {
        try
        {
            ModelsManager manager = new ModelsManager();
            FactorySettings state = new FactorySettings();
            pauseLock.setInitialValue(BODIES_AND_ENGINES_SUPPLIERS + state.getAccessorySuppliersNumber());

            synchronized(guiLockNotification.getGuiLock())
            {
                final Store bodiesStore = new Store(state.getBodiesStoreSize());
                final Store enginesStore = new Store(state.getEnginesStoreSize());
                final Store accessoriesStore = new Store(state.getAccessoriesStoreSize());
                final Store carsStore = new Store(state.getCarsStoreSize());
                manager.setBodyStore(bodiesStore);
                manager.setEnginesStore(enginesStore);
                manager.setAccessoryStore(accessoriesStore);
                manager.setCarsStore(carsStore);

                GuiRunnable guiRunnable = new GuiRunnable(manager, pauseLock, state, guiLockNotification);
                SwingUtilities.invokeLater(guiRunnable);

                final Supplier bodiesSupplier = new Supplier(bodiesStore, INITIAL_BODIES_SUPPLIER_TIME, pauseLock, manager);
                final Supplier enginesSupplier = new Supplier(enginesStore, INITIAL_ENGINES_SUPPLIER_TIME, pauseLock, manager);
                int number = state.getAccessorySuppliersNumber();
                final AccessoriesSuppliers accessoriesSuppliers = new AccessoriesSuppliers(number, accessoriesStore,
                        INITIAL_ACCESSORIES_SUPPLIER_TIME, pauseLock, manager);

                manager.setBodiesSupplier(bodiesSupplier);
                manager.setEnginesSupplier(enginesSupplier);
                manager.setAccessoriesSuppliers(accessoriesSuppliers);

                final ThreadPool pool = new ThreadPool(state.getWorkersNumber());
                manager.setPool(pool);

                final WorkerTaskCreator creator = new WorkerTaskCreator(bodiesStore, enginesStore, accessoriesStore, carsStore);
                manager.setWorkerTaskCreator(creator);

                ProductionStoreController controllerRunnable = new ProductionStoreController(state.getDealersNumber() / 2,
                        carsStore, controllerLock, pool, creator);
                final Thread controller = new Thread(controllerRunnable, "Production store controller");
                manager.setProductionStoreControllerThread(controller);

                final Object dealersLock = new Object();
                int dealersNumber = state.getDealersNumber();
                ArrayList<Dealer> dealers = new ArrayList<Dealer>(dealersNumber);
                FileHandler handler = state.getLoggingFile();
                if(null != handler)
                {
                    Dealer.getLogger().addHandler(handler);
                }
                for(int i = 0; i != dealersNumber; i++)
                {
                    dealers.add(new Dealer(carsStore, INITIAL_DEALERS_TIME, dealersLock, controllerLock, controllerRunnable, state.isLogging()));
                }
                manager.setDealers(dealers);                
                while(!guiLockNotification.isGuiLockNotified())
                {
                    guiLockNotification.getGuiLock().wait();
                }

                guiRunnable.getChartsPanel().beginDrawing(state);
                startThreads(manager, dealersNumber, controllerRunnable);
            }
        }
        catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }
        catch(IOException error)
        {
            JOptionPane.showConfirmDialog(null, "Input-out exception. Info file loading error.", "Application " +
                    "error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void startThreads(ModelsManager manager, int dealersNumber, ProductionStoreController controller) throws InterruptedException
    {
        final Thread bodiesSupplierThread = new Thread(manager.getBodiesSupplier(), "Bodies supplier");
        final Thread enginesSupplierThread = new Thread(manager.getEnginesSupplier(), "Engines supplier");
        manager.getBodiesSupplier().setThread(bodiesSupplierThread);
        manager.getEnginesSupplier().setThread(enginesSupplierThread);
        bodiesSupplierThread.start();
        enginesSupplierThread.start();
        manager.getAccessoriesSuppliers().start();

        manager.getPool().startWorking();
        manager.getProductionStoreControllerThread().start();
        synchronized(controllerLock)
        {
            while(!controller.wereNewTasksLoaded())
            {
                controllerLock.wait();
            }
        }

        final Thread[] dealersThreads = new Thread[dealersNumber];
        for(int i = 0; i != dealersNumber; i++)
        {
            dealersThreads[i] = new Thread(manager.getDealers().get(i), "Dealer{" + i + '}');
            manager.getDealers().get(i).setThread(dealersThreads[i]);
            dealersThreads[i].start();
        }
    }
}
