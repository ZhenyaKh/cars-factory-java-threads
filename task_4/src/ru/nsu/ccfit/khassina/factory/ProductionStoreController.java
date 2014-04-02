package ru.nsu.ccfit.khassina.factory;

import ru.nsu.ccfit.khassina.threadpool.ThreadPool;

/**
 * Controller that looks after cars number in a car store
 */
public final class ProductionStoreController implements Runnable
{
    private int minCarsNumber;
    private Store carsStore;
    private ThreadPool pool;
    private final Object controllerLock;
    private final WorkerTaskCreator taskCreator;
    private boolean newTasksWereLoaded;
    private static final int TIMES_OF_INCREASE = 4;

    public ProductionStoreController(int minCarsNumber, Store carsStore, Object controllerLock, ThreadPool pool, WorkerTaskCreator creator)
    {
        assert null != carsStore;
        assert null != controllerLock;
        assert null != pool;
        assert null != creator;

        this.minCarsNumber = minCarsNumber;
        this.carsStore = carsStore;
        this.controllerLock = controllerLock;
        this.pool = pool;
        taskCreator = creator;
    }
    
    public void run()
    {
        synchronized(controllerLock)
        {
            while(!Thread.interrupted())
            {
                try
                {
                    int numberOfStoredCars = carsStore.getCurrentObjectsNumber();
                    int tasksNumberInPool = pool.getTasksNumber();
                    if(minCarsNumber > numberOfStoredCars && TIMES_OF_INCREASE * minCarsNumber > tasksNumberInPool)
                    {
                        for(int i = 0; i != TIMES_OF_INCREASE * minCarsNumber - tasksNumberInPool; i++)
                        {
                            pool.addTask(taskCreator.createTask());
                        }
                    }
                    controllerLock.notifyAll();
                    newTasksWereLoaded = true;
                    while(newTasksWereLoaded)
                    {
                        controllerLock.wait();
                    }
                }
                catch (InterruptedException exception)
                {
                    break;
                }
            }
        }
    }

    void setNewTasksDemand()
    {
        newTasksWereLoaded = false;
    }

    public boolean wereNewTasksLoaded()
    {
        return newTasksWereLoaded;
    }
}
