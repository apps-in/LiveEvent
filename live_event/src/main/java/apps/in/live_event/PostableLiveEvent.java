package apps.in.live_event;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostableLiveEvent<T> implements LiveEvent<T>, LifecycleObserver {

    private EventHandlerList<T> handlerList = new EventHandlerList<>();

    private ExecutorService backgroundThreadPool;
    private Handler mainThreadHandler;

    public void post(T eventData){
        notifyListeners(eventData);
    }

    @Override
    public void subscribe(@NonNull EventHandler<T> eventHandler) {
        addHandler(new EventHandlerWrapper<>(eventHandler, null, false));
    }

    @Override
    public void subscribe(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler<T> eventHandler) {
        addHandler(new EventHandlerWrapper<>(eventHandler, lifecycleOwner, false));
        observeLifecycle(lifecycleOwner);
    }

    @Override
    public void subscribeOnMainThread(@NonNull EventHandler<T> eventHandler) {
        addHandler(new EventHandlerWrapper<>(eventHandler, null, true));
    }

    @Override
    public void subscribeOnMainThread(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler<T> eventHandler) {
        addHandler(new EventHandlerWrapper<>(eventHandler, lifecycleOwner, true));
        observeLifecycle(lifecycleOwner);
    }

    @Override
    public void unsubscribe(@NonNull EventHandler<T> eventHandler) {
        removeHandler(eventHandler);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void removeListener(LifecycleOwner lifecycleOwner){
        removeHandler(lifecycleOwner);
    }

    private synchronized void addHandler(EventHandlerWrapper<T> handlerWrapper){
        handlerList.add(handlerWrapper);
    }

    private synchronized void removeHandler(Object handler){
        Set<LifecycleOwner> lifecycleOwnerSet = handlerList.remove(handler);
        for (LifecycleOwner lifecycleOwner : lifecycleOwnerSet){
            if (!handlerList.contains(lifecycleOwner)){
                stopLifecycleObserving(lifecycleOwner);
            }
        }
    }

    private synchronized void notifyListeners(T eventData){
        for (final EventHandlerWrapper<T> handlerWrapper : handlerList){
            LifecycleOwner lifecycleOwner = handlerWrapper.getLifecycleOwner();
            if (lifecycleOwner != null){
                Lifecycle.State currentState = lifecycleOwner.getLifecycle().getCurrentState();
                if (currentState != Lifecycle.State.STARTED && currentState != Lifecycle.State.RESUMED){
                    continue;
                }
            }
            EventHandler<T> eventHandler = handlerWrapper.getEventHandler();
            if (handlerWrapper.isMainThreadNeeded()){
                executeInMainThread(eventHandler, eventData);
            } else {
                executeInBackground(eventHandler, eventData);
            }
        }
    }

    private void executeInMainThread(EventHandler<T> eventHandler, T eventData){
        if (mainThreadHandler == null){
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        mainThreadHandler.post(() -> eventHandler.onEvent(eventData));
    }

    private void executeInBackground(EventHandler eventHandler, T eventData){
        if (backgroundThreadPool == null){
            backgroundThreadPool = Executors.newCachedThreadPool();
        }
        backgroundThreadPool.execute(() -> eventHandler.onEvent(eventData));
    }

    private synchronized void observeLifecycle(LifecycleOwner lifecycleOwner){
        if (lifecycleOwner == null){
            throw new IllegalArgumentException();
        }
        if (!handlerList.contains(lifecycleOwner)) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
    }

    private void stopLifecycleObserving(LifecycleOwner lifecycleOwner){
        if (lifecycleOwner == null){
            throw new IllegalArgumentException();
        }
        lifecycleOwner.getLifecycle().removeObserver(this);
    }
}
