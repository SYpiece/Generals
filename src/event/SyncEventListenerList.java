package event;

import java.util.List;

public abstract class SyncEventListenerList<L extends EventListener<E>, E extends Event> extends EventListenerList<L, E> {
    protected SyncEventListenerList(Class<L> listenerClass, Class<E> eventClass) {
        super(listenerClass, eventClass);
    }
    @Override
    public synchronized L[] getListeners() {
        return super.getListeners();
    }
    @Override
    public synchronized <T extends L> T[] getListeners(Class<T> listenerClass) {
        return super.getListeners(listenerClass);
    }
    @Override
    public synchronized List<L> getListenersList() {
        return super.getListenersList();
    }
    @Override
    public synchronized <T extends L> List<T> getListenersList(Class<T> listenerClass) {
        return super.getListenersList(listenerClass);
    }
    @Override
    public synchronized void addListener(L listener) {
        super.addListener(listener);
    }
    @Override
    public synchronized int getListenersCount() {
        return super.getListenersCount();
    }
    @Override
    public synchronized <T extends L> int getListenersCount(Class<T> listenerClass) {
        return super.getListenersCount(listenerClass);
    }
    @Override
    public synchronized boolean removeListener(L listener) {
        return super.removeListener(listener);
    }
    @Override
    public abstract void fireListenerEvent(E event);
}
