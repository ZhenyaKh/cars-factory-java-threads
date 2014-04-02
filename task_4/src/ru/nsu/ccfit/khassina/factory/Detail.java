package ru.nsu.ccfit.khassina.factory;
/**
 * Detail class
 */

class Detail implements Identified
{
    private int id;
    private static volatile int number = 0;

    public Detail()
    {
        id = number++;
    }

    public static int getNewId()
    {
        return number++;
    }

    public int getId()
    {
        return id;
    }
}
