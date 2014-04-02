package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.FactorySettings;
import ru.nsu.ccfit.khassina.factory.ModelsManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ChartsPanel extends JPanel
{
    private static final float MY_SCREEN_HEIGHT = 768f;
    private static final int Y_AXIS_UP_SHIFT = 50;
    private static final int X_AXIS_SHIFT = 40;
    private static final float SIDE_SIZE = 510;
    private static final int Y_UP_INDENT = 10;
    private static final int TIMER_DELAY = 500;
    private static final int MILLS_PER_SECONDS = 1000;
    static final int BODIES_CHECK_BOX = 0;
    static final int ENGINES_CHECK_BOX = 1;
    static final int ACCESSORIES_CHECK_BOX = 2;
    static final int CARS_CHECK_BOX = 3;    

    private final ArrayList<Integer> storedBodiesList = new ArrayList<Integer>();
    private final ArrayList<Integer> storedEnginesList = new ArrayList<Integer>();
    private final ArrayList<Integer> storedAccessoriesList = new ArrayList<Integer>();
    private final ArrayList<Integer> storedCarsList = new ArrayList<Integer>();
    private final AtomicBoolean areChartsStarted = new AtomicBoolean();
    private volatile Boolean[] checkBoxesSemaphore;
    private int allStoresMaxCapacity;
    private final ModelsManager manager;
    private final float xPixelsDelay;
    private FactorySettings settings;
    private final float coefficient;
    private Point2D.Float bodiesBeginning;
    private Point2D.Float enginesBeginning;
    private Point2D.Float accessoriesBeginning;
    private Point2D.Float carsBeginning;
    private float pixelsNumberForDetail;
    private final float sideSize;
    private final float yUpIndent;
    private final float yAxis;
    private final float xAxis;

    ChartsPanel(ModelsManager modelsManager)
    {
        this.manager = modelsManager;
        coefficient = (float)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / MY_SCREEN_HEIGHT;
        sideSize = SIDE_SIZE * coefficient;
        setPreferredSize(new Dimension((int)sideSize, (int)sideSize));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "details/seconds",
                TitledBorder.RIGHT, TitledBorder.BOTTOM, new Font("Times New Roman", Font.BOLD, (int)(14 * coefficient))));
        setBackground(Color.WHITE);
        yAxis = sideSize - Y_AXIS_UP_SHIFT * coefficient;
        xAxis = X_AXIS_SHIFT * coefficient;
        yUpIndent = Y_UP_INDENT * coefficient;
        xPixelsDelay = (sideSize - 2 * xAxis) / (TIME_LABELS_SECONDS_DELAY * TIME_TICKS_NUMBER * MILLS_PER_SECONDS) * TIMER_DELAY;
        bodiesBeginning = new Point2D.Float(xAxis, yAxis);
        enginesBeginning = new Point2D.Float(xAxis, yAxis);
        accessoriesBeginning = new Point2D.Float(xAxis, yAxis);
        carsBeginning = new Point2D.Float(xAxis, yAxis);
    }
    
    public void paintComponent(Graphics graphics)
    {        
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        drawAxises(graphics2D);        
        
        if(areChartsStarted.get())
        {           
            synchronized(storedBodiesList)
            {
                if(checkBoxesSemaphore[BODIES_CHECK_BOX])
                {
                    drawStoreCurve(storedBodiesList, graphics2D, Color.GREEN, bodiesBeginning);
                }
                if(checkBoxesSemaphore[ENGINES_CHECK_BOX])
                {
                    drawStoreCurve(storedEnginesList, graphics2D, Color.MAGENTA, enginesBeginning);
                }
                if(checkBoxesSemaphore[ACCESSORIES_CHECK_BOX])
                {
                    drawStoreCurve(storedAccessoriesList, graphics2D, Color.ORANGE, accessoriesBeginning);
                }
                if(checkBoxesSemaphore[CARS_CHECK_BOX])
                {
                    drawStoreCurve(storedCarsList, graphics2D, Color.CYAN, carsBeginning);
                }
            }
        }
    }

    public void beginDrawing(FactorySettings settings)
    {
        assert null != settings;
        
        this.settings = settings;
        setAllStoresMaxCapacity();
        pixelsNumberForDetail = (sideSize - yUpIndent - Y_AXIS_UP_SHIFT * coefficient) / allStoresMaxCapacity;
        ChartsInfoRetriever retriever = new ChartsInfoRetriever();
        Timer timer = new Timer(TIMER_DELAY, retriever);
        timer.start();
    }
    
    private void drawStoreCurve(ArrayList<Integer> storedDetailsList, Graphics2D graphics2D, Color color, Point2D.Float startPoint)
    {
        graphics2D.setColor(color);
        Point2D.Float beginning = startPoint;
        for(Integer storedDetails: storedDetailsList)
        {
            float yCoordinate = yAxis - storedDetails * pixelsNumberForDetail;
            Point2D.Float end = new Point2D.Float(beginning.x + xPixelsDelay, yCoordinate);
            Line2D line = new Line2D.Float(beginning, end);
            graphics2D.draw(line);
            beginning = end;
        }
    }

    private static final int STORES_NUMBER = 4;
    private void setAllStoresMaxCapacity() 
    {
        ArrayList<Integer> allMaximums = new ArrayList<Integer>(STORES_NUMBER);
        allMaximums.add(settings.getBodiesStoreSize());
        allMaximums.add(settings.getEnginesStoreSize());
        allMaximums.add(settings.getAccessoriesStoreSize());
        allMaximums.add(settings.getCarsStoreSize());
        allStoresMaxCapacity = Collections.max(allMaximums);
    }

    private static final int AXIS_STROKE = 3;
    private static final int TICK_LENGTH = 5;
    private static final int LABELS_INCREASING = 3;
    private static final int Y_TICKS_COUNT = 10;  
    private static final int TIME_LABELS_SECONDS_DELAY = 5;
    private static final int TIME_TICKS_NUMBER = 8;

    private void drawAxises(Graphics2D graphics2D)
    {
        graphics2D.setFont(new Font("Times New Roman", Font.BOLD, (int)(14 * coefficient)));
        Point2D.Float axisBeginning = new Point2D.Float(xAxis, yAxis);
        Stroke usualStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(AXIS_STROKE * coefficient));
        Line2D.Float xAxisLine = new Line2D.Float(axisBeginning, new Point2D.Float(sideSize - xAxis, yAxis));
        graphics2D.draw(xAxisLine);
        float xPixelsDelay = (sideSize - 2 * xAxis) / TIME_TICKS_NUMBER;
        float tickLength = TICK_LENGTH * coefficient;
        float xTick = xAxis;
        Integer seconds = 0;
        while(xTick <= sideSize - xAxis)
        {
            graphics2D.draw(new Line2D.Float(xTick, yAxis - tickLength / 2, xTick, yAxis + tickLength / 2));
            float stringWidth = (float)graphics2D.getFont().getStringBounds(seconds.toString(), graphics2D.getFontRenderContext()).getWidth();
            graphics2D.drawString(seconds.toString(), xTick - stringWidth / 2, yAxis + LABELS_INCREASING * tickLength);
            xTick += xPixelsDelay;
            seconds += TIME_LABELS_SECONDS_DELAY;
        }
        Line2D.Float yAxisLine = new Line2D.Float(axisBeginning, new Point2D.Float(xAxis, yUpIndent));
        graphics2D.draw(yAxisLine);
        float yTicksDelay = (sideSize - yUpIndent - Y_AXIS_UP_SHIFT * coefficient) / Y_TICKS_COUNT;
        float yTick = yAxis;
        Integer details = 0;
        while(yTick >= yUpIndent)
        {
            graphics2D.draw(new Line2D.Float(xAxis - tickLength / 2, yTick, xAxis + tickLength / 2, yTick));
            float stringHeight = graphics2D.getFont().getLineMetrics(details.toString(), graphics2D.getFontRenderContext()).getAscent();
            float stringWidth = (float)graphics2D.getFont().getStringBounds(details.toString(), graphics2D.getFontRenderContext()).getWidth();
            graphics2D.drawString(details.toString(), xAxis - stringWidth - TICK_LENGTH, yTick + stringHeight / 2);
            yTick -= yTicksDelay;
            details += allStoresMaxCapacity / Y_TICKS_COUNT;
        }
        drawExplanations(graphics2D);
        graphics2D.setStroke(usualStroke);
    }
    
    private static final int EXPLANATIONS_TEXT_Y_SHIFT = 490;
    private static final int EXPLANATIONS_X_SHIFT = 30;
    private void drawExplanations(Graphics2D graphics2D)
    {
        graphics2D.setColor(Color.GREEN);
        int shift = 0;
        graphics2D.drawString("Storied Bodies", EXPLANATIONS_X_SHIFT * coefficient + (sideSize / STORES_NUMBER) * (shift++),
                EXPLANATIONS_TEXT_Y_SHIFT * coefficient);
        graphics2D.setColor(Color.MAGENTA);
        graphics2D.drawString("Storied Engines", EXPLANATIONS_X_SHIFT * coefficient + (sideSize / STORES_NUMBER) * (shift++),
                EXPLANATIONS_TEXT_Y_SHIFT * coefficient);
        graphics2D.setColor(Color.ORANGE);
        graphics2D.drawString("Storied Accessories", EXPLANATIONS_X_SHIFT * coefficient + (sideSize / STORES_NUMBER) * (shift++),
                EXPLANATIONS_TEXT_Y_SHIFT * coefficient);
        graphics2D.setColor(Color.CYAN);
        graphics2D.drawString("Storied Cars", EXPLANATIONS_X_SHIFT * coefficient + (sideSize / STORES_NUMBER) * (shift),
                EXPLANATIONS_TEXT_Y_SHIFT * coefficient);
    }    
    
    void setCheckBoxesSemaphore(Boolean[] semaphore)
    {
        checkBoxesSemaphore = semaphore;
        repaint();
    }

    private final class ChartsInfoRetriever implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            synchronized(storedBodiesList)
            {
                storedBodiesList.add(manager.getBodiesStore().getCurrentObjectsNumber());
                storedEnginesList.add(manager.getEnginesStore().getCurrentObjectsNumber());
                storedAccessoriesList.add(manager.getAccessoryStore().getCurrentObjectsNumber());
                storedCarsList.add(manager.getCarsStore().getCurrentObjectsNumber());
                if((sideSize - 2 * xAxis) / xPixelsDelay == storedBodiesList.size())
                {
                    bodiesBeginning = new Point2D.Float(xAxis, yAxis - storedBodiesList.remove(0) * pixelsNumberForDetail);
                    enginesBeginning = new Point2D.Float(xAxis, yAxis - storedEnginesList.remove(0) * pixelsNumberForDetail);
                    accessoriesBeginning = new Point2D.Float(xAxis, yAxis - storedAccessoriesList.remove(0) * pixelsNumberForDetail);
                    carsBeginning = new Point2D.Float(xAxis, yAxis - storedCarsList.remove(0) * pixelsNumberForDetail);
                }
                areChartsStarted.set(true);
                repaint();
            }
        }
    }
}
