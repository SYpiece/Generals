package event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class EventListenerList<L extends EventListener<E>, E extends Event> {
    protected final List<L> _listenerList = new LinkedList<>();
    protected final Class<L> _listenerClass;
    protected final Class<E> _eventClass;
    protected EventListenerList(Class<L> listenerClass, Class<E> eventClass) {
        _listenerClass = listenerClass;
        _eventClass = eventClass;
    }
    @SuppressWarnings("unchecked")
    public L[] getListeners() {
        return _listenerList.toArray((L[]) Array.newInstance(_listenerClass, _listenerList.size()));
    }
    @SuppressWarnings("unchecked")
    public <T extends L> T[] getListeners(Class<T> listenerClass) {
        if (listenerClass == null) {
            return null;
        }
        LinkedList<T> listeners = new LinkedList<>();
        for (L listener : _listenerList) {
            if (listenerClass.isInstance(listener)) {
                listeners.add((T) listener);
            }
        }
        return listeners.toArray((T[]) Array.newInstance(listenerClass, listeners.size()));
    }
    public List<L> getListenersList() {
        return Collections.unmodifiableList(_listenerList);
    }
    @SuppressWarnings("unchecked")
    public <T extends L> List<T> getListenersList(Class<T> listenerClass) {
        if (listenerClass == null) {
            return Collections.emptyList();
        }
        LinkedList<T> listeners = new LinkedList<>();
        for (L listener : _listenerList) {
            if (listenerClass.isInstance(listener)) {
                listeners.add((T) listener);
            }
        }
        return Collections.unmodifiableList(listeners);
    }
    public void addListener(L listener) {
        if (listener == null) {
            return;
        }
        _listenerList.add(listener);
    }
    public int getListenersCount() {
        return _listenerList.size();
    }
    public <T extends L> int getListenersCount(Class<T> listenerClass) {
        if (listenerClass == null) {
            return 0;
        }
        int count = 0;
        for (L listener : _listenerList) {
            if (listenerClass.isInstance(listener)) {
                count++;
            }
        }
        return count;
    }
    public boolean removeListener(L listener) {
        return _listenerList.remove(listener);
    }
    public abstract void fireListenerEvent(E event);
}
