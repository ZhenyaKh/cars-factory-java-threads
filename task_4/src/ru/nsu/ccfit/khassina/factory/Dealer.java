package ru.nsu.ccfit.khassina.factory;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *Car dealer class
 */

public final class Dealer implements Runnable
{
    private static final Logger logger = Logger.getLogger(Dealer.class.getName());
    private Store carsStore;
    private volatile int waitingTime;
    private Thread thread;
    private ProductionStoreController controller;
    private final Object dealersLock;
    private final Object controllerLock;
    private boolean isLogging;

    public Dealer(Store carsStore, int waitingTime, Object dealersLock, Object controllerLock,
           ProductionStoreController controller, boolean isLogging)
    {
        assert null != carsStore;
        assert null != dealersLock;
        assert null != controllerLock;
        assert null != controller;

        this.carsStore = carsStore;
        this.waitingTime = waitingTime;
        this.dealersLock = dealersLock;
        this.controllerLock = controllerLock;
        this.controller = controller;
        this.isLogging = isLogging;
    }

    public void run()
    {
        try
        {
            GregorianCalendar calendar = new GregorianCalendar();
            while(!Thread.interrupted())
            {
                synchronized(dealersLock)
                {
                    synchronized(controllerLock)
                    {
                        Car car = (Car) carsStore.getObject();
                        if(isLogging)
                        {
                            logger.info(String.format("[%s]: %s: Auto[%d]: (Body: [%d], Engine: [%d], Accessory: [%d])",
                                    calendar.getTime().toString(), Thread.currentThread().getName(), car.getId(),
                                    car.getBodyId(), car.getEngineId(), car.getAccessoryId()));
                        }
                        controllerLock.notifyAll();
                        controller.setNewTasksDemand();
                        while(!controller.wereNewTasksLoaded())
                        {
                            controllerLock.wait();
                        }
                    }
                }
                TimeUnit.MILLISECONDS.sleep(waitingTime);
            }
        }
        catch (InterruptedException exception)
        {
            carsStore = null;
        }
    }

    public void setWaitingTime(int time)
    {
        waitingTime = time;
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

    public static Logger getLogger()
    {
        return logger;
    }
}
