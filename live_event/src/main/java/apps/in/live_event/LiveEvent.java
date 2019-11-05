package apps.in.live_event;

import androidx.lifecycle.LifecycleOwner;

public interface LiveEvent {
    void subscribe(EventHandler eventHandler);
    void subscribe(LifecycleOwner lifecycleOwner, EventHandler eventHandler);
    void subscribeOnMainThread(EventHandler eventHandler);
    void subscribeOnMainThread(LifecycleOwner lifecycleOwner, EventHandler eventHandler);
    void unsubscribe(EventHandler eventHandler);
}
