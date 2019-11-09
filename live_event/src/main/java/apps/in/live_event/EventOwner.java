package apps.in.live_event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public abstract class EventOwner<T> {

    private PostableLiveEvent<T> event = new PostableLiveEvent<>();

    public void postEvent(T eventData) {
        event.post(eventData);
    }

    public void subscribe(@NonNull EventHandler<T> eventHandler) {
        event.subscribe(eventHandler);
    }

    public void subscribe(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler<T> eventHandler) {
        event.subscribe(lifecycleOwner, eventHandler);
    }

    public void subscribeOnMainThread(@NonNull EventHandler<T> eventHandler) {
        event.subscribeOnMainThread(eventHandler);
    }

    public void subscribeOnMainThread(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler<T> eventHandler) {
        event.subscribeOnMainThread(lifecycleOwner, eventHandler);
    }

    public void unsubscribe(@NonNull EventHandler<T> eventHandler) {
        event.unsubscribe(eventHandler);
    }

}
