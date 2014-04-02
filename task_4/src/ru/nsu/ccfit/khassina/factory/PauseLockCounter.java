package ru.nsu.ccfit.khassina.factory;

/**
 * Counter that shows how many models are on waiting on pauseLock object
 */
public final class PauseLockCounter
{
    private int counter;
    private int initialValue;
    private final Object lock = new Object();

    public boolean isNull()
    {
        synchronized(lock)
        {
            return 0 == counter;
        }
    }

    void decreaseCounter()
    {
        synchronized(lock)
        {
            counter--;
            if(isNull()){
                lock.notifyAll();
            }
        }
    }

    public Object getInternalLock()
    {
        return lock;
    }

    public void relieve() 
    {
        counter = initialValue;
    }

    public void setInitialValue(int initialValue)
    {
        this.initialValue = initialValue;
        counter = initialValue;
    }
}
