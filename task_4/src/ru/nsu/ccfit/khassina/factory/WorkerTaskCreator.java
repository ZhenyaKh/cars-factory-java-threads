package ru.nsu.ccfit.khassina.factory;

/**
 * Creators of new tasks for thread pool
 */
public final class WorkerTaskCreator
{
    private Store bodiesStore;
    private Store enginesStore;
    private Store accessoriesStore;
    private Store carsStore;
    private ChangeListener tasksListener;

    public WorkerTaskCreator(Store bodiesStore, Store enginesStore, Store accessoriesStore, Store carsStore)
    {
        this.accessoriesStore = accessoriesStore;
        this.bodiesStore = bodiesStore;
        this.enginesStore = enginesStore;
        this.carsStore = carsStore;
    }

    WorkerTask createTask()
    {
         return new WorkerTask(bodiesStore, enginesStore, accessoriesStore, carsStore, tasksListener);
    }

    public void setListener(ChangeListener listener)
    {
        assert null != listener;

        this.tasksListener = listener;
    }
}
