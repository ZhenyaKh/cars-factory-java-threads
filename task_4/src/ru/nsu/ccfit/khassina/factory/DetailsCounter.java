package ru.nsu.ccfit.khassina.factory;

/**
 * details counter
 */

final class DetailsCounter
{
    private int number = 0;
    private static final DetailsCounter counter = new DetailsCounter();

    static DetailsCounter getInstance()
    {
        return counter;
    }

    int getNewId()
    {
        synchronized(counter)
        {
            return number++;
        }
    }

    private DetailsCounter()
    {

    }
}
