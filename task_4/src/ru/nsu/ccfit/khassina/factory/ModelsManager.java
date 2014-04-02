package ru.nsu.ccfit.khassina.factory;

import ru.nsu.ccfit.khassina.factory.*;
import ru.nsu.ccfit.khassina.threadpool.ThreadPool;

import java.util.ArrayList;

/**
 * Models manager class
 */
public final class ModelsManager 
{
    private Store bodiesStore;
    private Store enginesStore;
    private Store accessoryStore;
    private Store carsStore;
    
    private Supplier bodiesSupplier;
    private Supplier enginesSupplier;
    private AccessoriesSuppliers accessoriesSuppliers;

    private ThreadPool pool;
    private WorkerTaskCreator creator;
    private ArrayList<Dealer> dealers;
    private Thread controllerThread;
    private boolean wereSuppliersStopped = false;
    private boolean isExit = false;

    public void setBodyStore(Store bodiesStore)
    {
        this.bodiesStore = bodiesStore;
    }

    public void setEnginesStore(Store enginesStore)
    {
        this.enginesStore = enginesStore;
    }

    public void setAccessoryStore(Store accessoryStore)
    {
        this.accessoryStore = accessoryStore;
    }
    
    public void setBodiesSupplier(Supplier bodiesSupplier)
    {
        this.bodiesSupplier = bodiesSupplier;
    }    
    
    public void setEnginesSupplier(Supplier enginesSupplier)
    {
        this.enginesSupplier = enginesSupplier;
    }    
    
    public void setAccessoriesSuppliers(AccessoriesSuppliers accessoriesSuppliers)
    {
        this.accessoriesSuppliers = accessoriesSuppliers;
    }            

    public void setCarsStore(Store carsStore)
    {
        this.carsStore = carsStore;
    }

    public void setPool(ThreadPool pool)
    {
        this.pool = pool;
    }

    public void setWorkerTaskCreator(WorkerTaskCreator creator)
    {
        this.creator = creator;
    }

    public void setDealers(ArrayList<Dealer> dealers)
    {
        this.dealers = dealers;
    }

    public void setProductionStoreControllerThread(Thread controllerThread)
    {
        assert null != controllerThread;

        this.controllerThread = controllerThread;
    }

    public Store getBodiesStore()
    {
        return bodiesStore;
    }

    public Store getEnginesStore()
    {
        return enginesStore;
    }

    public Store getAccessoryStore()
    {
        return accessoryStore;
    }
    
    public Supplier getBodiesSupplier()
    {
        return bodiesSupplier;
    }    
    
    public Supplier getEnginesSupplier()
    {
        return enginesSupplier;
    }    
    
    public AccessoriesSuppliers getAccessoriesSuppliers()
    {
        return accessoriesSuppliers;
    }

    public Store getCarsStore()
    {
        return carsStore;
    }

    public ThreadPool getPool()
    {
        return pool;
    }

    public WorkerTaskCreator getCreator()
    {
        return creator;
    }

    public ArrayList<Dealer> getDealers()
    {
        return dealers;
    }

    public Thread getProductionStoreControllerThread()
    {
        return controllerThread;
    }

    public void stopSuppliers()
    {
        wereSuppliersStopped = true;
    }

    public boolean wereSuppliersStopped()
    {
        return wereSuppliersStopped;
    }

    public void turnOnSuppliers()
    {
        wereSuppliersStopped = false;
    }

    public boolean isExit()
    {
        return isExit;
    }

    public void setExitTrue()
    {
        isExit = true;
    }
}
