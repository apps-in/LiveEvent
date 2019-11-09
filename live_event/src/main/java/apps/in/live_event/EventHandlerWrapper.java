package apps.in.live_event;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

class EventHandlerWrapper<T> {

    private final EventHandler<T> eventHandler;
    private final LifecycleOwner lifecycleOwner;
    private final boolean mainThreadNeeded;

    public EventHandlerWrapper(EventHandler<T> eventHandler, LifecycleOwner lifecycleOwner, boolean mainThreadNeeded) {
        this.eventHandler = eventHandler;
        this.lifecycleOwner = lifecycleOwner;
        this.mainThreadNeeded = mainThreadNeeded;
    }

    public EventHandler<T> getEventHandler() {
        return eventHandler;
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public boolean isMainThreadNeeded() {
        return mainThreadNeeded;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null){
            return false;
        }
        if (obj instanceof EventHandler){
            return eventHandler.equals(obj);
        }
        if (obj instanceof LifecycleOwner){
            return lifecycleOwner.equals(obj);
        }
        if (obj instanceof EventHandlerWrapper){
            return this == obj;
        }
        return false;
    }
}
