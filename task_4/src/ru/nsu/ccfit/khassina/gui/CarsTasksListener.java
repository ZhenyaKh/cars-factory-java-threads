package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.threadpool.ChangeListener;
import ru.nsu.ccfit.khassina.threadpool.ThreadPool;

import javax.swing.*;
import java.awt.*;

/**
 *Listener of tasks of new cars in threadpool
 */
final class CarsTasksListener implements ChangeListener
{
    private final JTextField info;
    private final ThreadPool pool;

    CarsTasksListener(ThreadPool pool, JTextField info)
    {
        assert null != info;
        assert null != pool;

        this.info = info;
        this.pool = pool;
    }

    public void modelChanged()
    {
        Integer currentNumber = pool.getTasksNumber();
        info.setBackground(Color.GREEN);
        info.setText(currentNumber.toString());
    }
}
