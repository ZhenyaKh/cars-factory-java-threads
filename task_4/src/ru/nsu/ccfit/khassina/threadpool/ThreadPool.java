package ru.nsu.ccfit.khassina.threadpool;

import java.util.ArrayList;

/**
 * Threads pool class
 */
public final class ThreadPool
{
    private ArrayList<Thread> threads;
    private final ArrayList<Runnable> tasks = new ArrayList<Runnable>();
    private final  Object lock = new Object();
    private volatile ChangeListener listener;

    public ThreadPool(int threadsNumber)
    {
        threads = new ArrayList<Thread>(threadsNumber);    
        for(int i = 0; i != threadsNumber; ++i)
        {
            Thread thread = new Thread(new ThreadPoolRunnable(this), "worker # " + i);
            threads.add(thread);
        }        
    }

    public void startWorking()
    {
        for(Thread thread: threads)
        {
            thread.start();
        }
    }
    
    public void addTask(Runnable newTask)
    {
        assert null != newTask;

        synchronized(lock)
        {
            tasks.add(newTask);
            listener.modelChanged();
            lock.notifyAll();
        }
    }
    
    public Runnable getTask() throws InterruptedException
    {
        synchronized(lock)
        {
            while(0 == tasks.size())
            {
                lock.wait();
            }
            Runnable task = tasks.remove(0);
            listener.modelChanged();
            return task;
        }

    }  
    
    public int getThreadsNumber()
    {
        return threads.size();
    }
    
    public int getTasksNumber()
    {
        synchronized(lock)
        {
            return tasks.size();
        }
    }
    
    public void setListener(ChangeListener listener)
    {
        assert null != listener;
        this.listener = listener;
    }

    public void interruptAllThreads()
    {
        for(Thread thread: threads)
        {
            thread.interrupt();
        }
    }
}
