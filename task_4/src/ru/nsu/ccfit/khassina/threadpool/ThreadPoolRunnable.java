package ru.nsu.ccfit.khassina.threadpool;

/**
 * Runnable class that are given to constructors of threads of the pool
 */
final class ThreadPoolRunnable implements Runnable
{
    private final ThreadPool pool;

    ThreadPoolRunnable(ThreadPool pool)
    {
        this.pool = pool;
    }

    public void run()
    {
        while(!Thread.interrupted())
        {   
            try
            {
                Runnable task = pool.getTask();
                task.run();
            }
            catch(InterruptedException exception)
            {
                return;
            }
        }
    }
}
