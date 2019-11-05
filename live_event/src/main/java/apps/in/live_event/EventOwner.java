package apps.in.live_event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public abstract class EventOwner {

    private PostableLiveEvent event = new PostableLiveEvent();

    public void post() {
        event.post();
    }

    public void subscribe(@NonNull EventHandler eventHandler) {
        event.subscribe(eventHandler);
    }

    public void subscribe(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler eventHandler) {
        event.subscribe(lifecycleOwner, eventHandler);
    }

    public void subscribeOnMainThread(@NonNull EventHandler eventHandler) {
        event.subscribeOnMainThread(eventHandler);
    }

    public void subscribeOnMainThread(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler eventHandler) {
        event.subscribeOnMainThread(lifecycleOwner, eventHandler);
    }

    public void unsubscribe(@NonNull EventHandler eventHandler) {
        event.unsubscribe(eventHandler);
    }

}
