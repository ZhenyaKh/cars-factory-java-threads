package ru.nsu.ccfit.khassina.factory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;

/**
 * Class that loads and describes factory settings
 */
public final class FactorySettings
{
    private static final int INFO_VALUES_NUMBER = 8;
    private static final int VALUE_FIELD = 1;
    private static final int MAX_STORE_SIZE = 20000;
    private static final int MAX_THREADS_NUMBER = 2000;
    
    private final ArrayList<String> infoValues = new ArrayList<String>(INFO_VALUES_NUMBER);
    private int bodiesStoreSize;
    private int enginesStoreSize;
    private int accessoriesStoreSize;
    private int carsStoreSize;
    private int accessorySuppliersNumber;
    private int workersNumber;
    private int dealersNumber;
    private FileHandler handler = null;
    private boolean isLogging;

    public FactorySettings() throws IOException
    {
        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try
        {
            reader = new FileReader("info.txt");
            bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            String[] value;
            while(null != line)
            {
                value = line.split("=");
                infoValues.add(value[VALUE_FIELD]);
                line = bufferedReader.readLine();        
            }
            setValues();
        }
        finally 
        {
            if(null != reader)
            {
                reader.close();    
            }
            if(null != bufferedReader)
            {
                bufferedReader.close();
            }
        }
    }

    private void setValues() throws IOException
    {
        try
        {
            bodiesStoreSize = Integer.parseInt(infoValues.remove(0));
            enginesStoreSize = Integer.parseInt(infoValues.remove(0));
            accessoriesStoreSize = Integer.parseInt(infoValues.remove(0));
            carsStoreSize = Integer.parseInt(infoValues.remove(0));
            if(0 >= bodiesStoreSize || 0 >= enginesStoreSize || 0 >= accessoriesStoreSize
                    || MAX_STORE_SIZE < bodiesStoreSize || MAX_STORE_SIZE < enginesStoreSize || MAX_STORE_SIZE < accessoriesStoreSize)
            {
                throw new IOException();
            }

            accessorySuppliersNumber = Integer.parseInt(infoValues.remove(0));
            workersNumber = Integer.parseInt(infoValues.remove(0));
            dealersNumber = Integer.parseInt(infoValues.remove(0));
            if(0 >= accessorySuppliersNumber || 0 >= workersNumber || 0 >= dealersNumber
                    || MAX_THREADS_NUMBER < accessorySuppliersNumber || MAX_THREADS_NUMBER < workersNumber || MAX_THREADS_NUMBER < dealersNumber)
            {
                throw new IOException();
            }
            isLogging = Boolean.parseBoolean(infoValues.remove(0));
            if(isLogging)
            {
                handler = new FileHandler("logger_file.txt");
            }
        }
        catch(NumberFormatException exception)
        {
            throw new IOException();
        }
    }

    public int getBodiesStoreSize()
    {
        return bodiesStoreSize;
    }

    public int getEnginesStoreSize()
    {
        return enginesStoreSize;
    }

    public int getAccessoriesStoreSize()
    {
        return accessoriesStoreSize;
    }

    public int getCarsStoreSize()
    {
        return carsStoreSize;
    }

    public int getAccessorySuppliersNumber()
    {
        return accessorySuppliersNumber;
    }

    public int getWorkersNumber()
    {
        return workersNumber;
    }

    public int getDealersNumber()
    {
        return dealersNumber;
    }
    
    public FileHandler getLoggingFile()
    {
        return handler;
    }

    public boolean isLogging()
    {
        return isLogging;
    }
}
