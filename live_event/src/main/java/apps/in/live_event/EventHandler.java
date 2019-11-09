package apps.in.live_event;

public interface EventHandler<T> {

    void onEvent(T eventData);

}
