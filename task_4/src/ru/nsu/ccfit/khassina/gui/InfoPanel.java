package ru.nsu.ccfit.khassina.gui;

import ru.nsu.ccfit.khassina.factory.*;
import ru.nsu.ccfit.khassina.threadpool.ThreadPool;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Panel with cars factory information
 */
final class InfoPanel extends JPanel
{
    private final ModelsManager manager;

    private static final int COLUMNS_NUMBER = 3;
    private static final int HORIZONTAL_GAP = 30;
    private static final int VERTICAL_GAP = 5;
    private static final int TOP_SHIFT = 15;
    private static final int TEXT_FIELD_LENGTH = 7;

    InfoPanel(ModelsManager manager, ChartsPanel chartsPanel)
    {
        this.manager = manager;
        setLayout(new GridLayout(0, COLUMNS_NUMBER, HORIZONTAL_GAP, VERTICAL_GAP));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Info"));

        Border border = BorderFactory.createEmptyBorder(TOP_SHIFT, 0, 0, 0);
        setCheckBoxes(border, "stored bodies", "stored engines", "stored accessories");
        setTextFields(TEXT_FIELD_LENGTH);

        setLabels(border, "produced bodies", "produced engines", "produced accessories");
        setTextFields(TEXT_FIELD_LENGTH);

        JCheckBox storedCarsBox= new JCheckBox("stored cars", true);
        storedCarsBox.setBorder(border);
        add(storedCarsBox);
        setLabels(border, "produced cars", "tasks of future cars", null);
        setTextFields(TEXT_FIELD_LENGTH);

        ChartsCheckBoxListener checkBoxListener = new ChartsCheckBoxListener(chartsPanel, this);
        checkBoxListener.actionPerformed(null);
        setListeners();
    }

    private static final int BODIES_STORE_NUMBER = 3;
    private static final int ENGINES_STORE_NUMBER = 4;
    private static final int ACCESSORIES_STORE_NUMBER = 5;
    private static final int BODIES_SUPPLIER_NUMBER = 9;
    private static final int ENGINES_SUPPLIER_NUMBER = 10;
    private static final int ACCESSORIES_SUPPLIERS_NUMBER = 11;
    private static final int CARS_STORE_NUMBER = 15;
    private static final int CARS_PRODUCED_VALUE_NUMBER = 16;
    private static final int CURRENT_TASKS_VALUE_NUMBER = 17;
    
    private void setListeners()
    {
        Store bodiesStore = manager.getBodiesStore();
        bodiesStore.setListener(new StoreModelListener(bodiesStore, (JTextField)getComponent(BODIES_STORE_NUMBER)));

        Store enginesStore = manager.getEnginesStore();
        enginesStore.setListener(new StoreModelListener(enginesStore, (JTextField)getComponent(ENGINES_STORE_NUMBER)));

        Store accessoryStore = manager.getAccessoryStore();
        accessoryStore.setListener(new StoreModelListener(accessoryStore, (JTextField)getComponent(ACCESSORIES_STORE_NUMBER)));

        Supplier bodiesSupplier = manager.getBodiesSupplier();
        bodiesSupplier.setListener(new SupplierModelListener((JTextField)getComponent(BODIES_SUPPLIER_NUMBER)));

        Supplier enginesSupplier = manager.getEnginesSupplier();
        enginesSupplier.setListener(new SupplierModelListener((JTextField)getComponent(ENGINES_SUPPLIER_NUMBER)));

        AccessoriesSuppliers accessoriesSuppliers = manager.getAccessoriesSuppliers();
        int suppliersNumber = accessoriesSuppliers.getSize();
        for(int i = 0; i != suppliersNumber; i++)
        {
            accessoriesSuppliers.getSupplier(i).setListener(new SupplierModelListener((JTextField) getComponent(ACCESSORIES_SUPPLIERS_NUMBER)));
        }

        Store carsStore = manager.getCarsStore();
        carsStore.setListener(new StoreModelListener(carsStore, (JTextField)getComponent(CARS_STORE_NUMBER)));

        WorkerTaskCreator creator = manager.getCreator();
        creator.setListener(new SupplierModelListener((JTextField)getComponent(CARS_PRODUCED_VALUE_NUMBER)));

        ThreadPool pool = manager.getPool();
        pool.setListener(new CarsTasksListener(pool, (JTextField)getComponent(CURRENT_TASKS_VALUE_NUMBER)));
    }

    private void setLabels(Border border, String text1, String text2, String text3)
    {
        JLabel label = new JLabel(text1);
        label.setBorder(border);
        add(label);

        label = new JLabel(text2);
        label.setBorder(border);
        add(label);

        if(null != text3)
        {
            label = new JLabel(text3);
            label.setBorder(border);
            add(label);
        }
    }

    private void setTextFields(int length)
    {
        JTextField storedBodiesDetails = new JTextField("0", length);
        storedBodiesDetails.setEditable(false);
        add(storedBodiesDetails);

        JTextField storedEnginesDetails = new JTextField("0", length);
        storedEnginesDetails.setEditable(false);
        add(storedEnginesDetails);

        JTextField storedAccessoryDetails = new JTextField("0", length);
        storedAccessoryDetails.setEditable(false);
        add(storedAccessoryDetails);
    }

    private void setCheckBoxes(Border border, String text1, String text2, String text3)
    {
        JCheckBox checkBox = new JCheckBox(text1, true);
        checkBox.setBorder(border);
        add(checkBox);

        checkBox = new JCheckBox(text2, true);
        checkBox.setBorder(border);
        add(checkBox);

        checkBox = new JCheckBox(text3, true);
        checkBox.setBorder(border);
        add(checkBox);
    }
}
