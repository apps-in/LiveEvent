package apps.in.live_event;

import androidx.lifecycle.LifecycleOwner;

public interface LiveEvent<T> {
    void subscribe(EventHandler<T> eventHandler);
    void subscribe(LifecycleOwner lifecycleOwner, EventHandler<T> eventHandler);
    void subscribeOnMainThread(EventHandler<T> eventHandler);
    void subscribeOnMainThread(LifecycleOwner lifecycleOwner, EventHandler<T> eventHandler);
    void unsubscribe(EventHandler<T> eventHandler);
}
