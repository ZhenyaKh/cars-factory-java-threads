package ru.nsu.ccfit.khassina.factory;

import java.util.concurrent.TimeUnit;
/**
 * Details supplier class
 */

public final class Supplier implements Runnable
{
    private Thread thread;
    private Store store;
    private int waitingTime;
    private final Object detailsNumberLock = new Object();
    private ChangeListener listener;
    private final ModelsManager manager;
    private final PauseLockCounter pauseLock;
    
    public Supplier(Store store, int waitingTime, PauseLockCounter pauseLock, ModelsManager manager)
    {
        assert null != store;
        assert null != pauseLock;
        assert null != manager;

        this.store = store;
        this.waitingTime = waitingTime;
        this.pauseLock = pauseLock;
        this.manager = manager;
    }

    public void run()
    {
        while(!manager.isExit())
        {
            while(!Thread.interrupted())
            {
                try
                {
                    store.storeObject(new Detail());
                    increaseProducedDetailsNumber();
                    TimeUnit.MILLISECONDS.sleep(waitingTime);
                }
                catch (InterruptedException exception)
                {
                    break;
                }
            }
            try
            {   if(manager.isExit())
                {
                    return;
                }
                pauseProcessing();
            }
            catch(InterruptedException exception)
            {
                return;
            }
        }
    }

    private void increaseProducedDetailsNumber()
    {
        synchronized(detailsNumberLock)
        {
            listener.modelChanged();
        }
    }

    public void setWaitingTime(int time)
    {
        waitingTime = time;
    }

    public void setListener(ChangeListener listener)
    {
        this.listener = listener;
    }

    public void setThread(Thread thread)
    {
        assert null != thread;

        this.thread = thread;
    }

    public void interruptThread()
    {
        thread.interrupt();
    }

    private void pauseProcessing() throws InterruptedException
    {
        synchronized(pauseLock)
        {
            pauseLock.decreaseCounter();
            while(manager.wereSuppliersStopped())
            {
                pauseLock.wait();
            }
        }
    }
}
