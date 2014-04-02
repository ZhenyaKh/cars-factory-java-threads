package ru.nsu.ccfit.khassina.factory;

/**
 * Car creators class
 */
final class WorkerTask implements Runnable
{
    private Store bodiesStore;
    private Store enginesStore;
    private Store accessoriesStore;
    private Store carsStore;
    private ChangeListener listener;

    WorkerTask(Store bodiesStore, Store enginesStore, Store accessoriesStore, Store carsStore, ChangeListener listener)
    {
        this.accessoriesStore = accessoriesStore;
        this.bodiesStore = bodiesStore;
        this.enginesStore = enginesStore;
        this.carsStore = carsStore;
        this.listener = listener;
    }

    public void run()
    {
        try
        {
            Detail body = (Detail)bodiesStore.getObject();
            Detail engine = (Detail)enginesStore.getObject();
            Detail accessory = (Detail)accessoriesStore.getObject();
            Car car = new Car(body, engine, accessory);
            listener.modelChanged();
            carsStore.storeObject(car);
        }
        catch(InterruptedException exception)
        {
            Thread.currentThread().interrupt();
        }
    }
}
