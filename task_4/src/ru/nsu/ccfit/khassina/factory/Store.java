package ru.nsu.ccfit.khassina.factory;

import java.util.ArrayList;
/**
 * details store class
 */

public final class Store
{
    private int maxObjectsNumber;
    private int currentObjectsNumber;
    private final ArrayList<Identified> objects;
    private final Object lock = new Object();
    private final Object storeObjectLock = new Object();
    private final Object getObjectLock = new Object();
    private ChangeListener listener;

    public Store(int maxObjectsNumber)
    {
        this.maxObjectsNumber = maxObjectsNumber;
        objects = new ArrayList<Identified>(maxObjectsNumber);
    }

    void storeObject(Identified object) throws InterruptedException
    {
        synchronized(storeObjectLock)
        {
            synchronized(lock)
            {
                assert null != object;

                while(currentObjectsNumber == maxObjectsNumber)
                {
                    lock.wait();
                }
                currentObjectsNumber++;
                objects.add(object);
                lock.notifyAll();
                listener.modelChanged();
            }
        }
    }

    Identified getObject() throws InterruptedException
    {
        synchronized(getObjectLock)
        {
            synchronized(lock)
            {
                while(0 == currentObjectsNumber)
                {
                    lock.wait();
                }
                Identified object = objects.remove(0);
                currentObjectsNumber--;
                lock.notifyAll();
                listener.modelChanged();

                return object;
            }
        }
    }
    
    public int getCurrentObjectsNumber()
    {
       synchronized(lock)
       {
           return currentObjectsNumber;
       }
    }

    public void setListener(ChangeListener listener)
    {
        assert null != listener;

        this.listener = listener;
    }
}
