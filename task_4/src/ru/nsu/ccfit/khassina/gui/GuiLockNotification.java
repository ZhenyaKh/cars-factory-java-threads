package ru.nsu.ccfit.khassina.gui;

/**
 * Class that provides synchronization: threads of models awaits till gui is constructed
 */
public final class GuiLockNotification
{
    private boolean guiLockNotified;
    private final Object guiLock = new Object();

    public Object getGuiLock()
    {
        return guiLock;
    }

    public void setGuiLockNotified()
    {
        guiLockNotified = true;
    }

    public boolean isGuiLockNotified()
    {
        return guiLockNotified;
    }
}
