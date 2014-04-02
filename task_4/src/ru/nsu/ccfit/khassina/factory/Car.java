package ru.nsu.ccfit.khassina.factory;

/**
 * Car class
 */
final class Car implements Identified
{
    private Detail body;
    private Detail engine;
    private Detail accessory;
    private int id;

    Car(Detail body, Detail engine, Detail accessory)
    {
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
        id = setId();
    }

    private int setId()
    {
        return Detail.getNewId();
    }
    
    public int getId()
    {
        return id;
    }
    
    int getBodyId()
    {
        return body.getId();
    }    
    
    int getEngineId()
    {
        return engine.getId();
    }    
    
    int getAccessoryId()
    {
        return accessory.getId();
    }
}
