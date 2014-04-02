package ru.nsu.ccfit.khassina.factory;

import java.util.ArrayList;
/**
 * Car accessories suppliers
 */

public final class AccessoriesSuppliers
{
    private ArrayList<Supplier> suppliers;
    private final ArrayList<Thread> threads = new ArrayList<Thread>();

    public AccessoriesSuppliers(int quantity, Store store, int time, PauseLockCounter pauseLock, ModelsManager manager)
    {
        suppliers = new ArrayList<Supplier>(quantity);
        while(0 < quantity--)
        {
            Supplier supplier = new Supplier(store, time, pauseLock, manager);
            suppliers.add(supplier);
        }
    }

    public void start()
    {
        int i = 0;
        for(Supplier supplier: suppliers)
        {
            Thread thread = new Thread(supplier, "accessory supplier # " + i);
            threads.add(thread);
            thread.start();
            i++;
        }
    }
    
    public int getSize()
    {
        return suppliers.size();
    }

    public Supplier getSupplier(int number)
    {
        return suppliers.get(number);
    }

    public void setWaitingTimeForAll(int time)
    {
        for(Supplier supplier: suppliers)
        {
            supplier.setWaitingTime(time);
        }
    }

    public void interruptAll()
    {
        for(Thread thread: threads)
        {
            thread.interrupt();
        }
    }
}
