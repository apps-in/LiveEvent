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

public class PostableLiveEvent implements LiveEvent, LifecycleObserver {

    private EventHandlerList handlerList = new EventHandlerList();

    private ExecutorService backgroundThreadPool;
    private Handler mainThreadHandler;

    public void post(){
        notifyListeners();
    }

    @Override
    public void subscribe(@NonNull EventHandler eventHandler) {
        addHandler(new EventHandlerWrapper(eventHandler, null, false));
    }

    @Override
    public void subscribe(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler eventHandler) {
        addHandler(new EventHandlerWrapper(eventHandler, lifecycleOwner, false));
        observeLifecycle(lifecycleOwner);
    }

    @Override
    public void subscribeOnMainThread(@NonNull EventHandler eventHandler) {
        addHandler(new EventHandlerWrapper(eventHandler, null, true));
    }

    @Override
    public void subscribeOnMainThread(@NonNull LifecycleOwner lifecycleOwner, @NonNull EventHandler eventHandler) {
        addHandler(new EventHandlerWrapper(eventHandler, lifecycleOwner, true));
        observeLifecycle(lifecycleOwner);
    }

    @Override
    public void unsubscribe(@NonNull EventHandler eventHandler) {
        removeHandler(eventHandler);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void removeListener(LifecycleOwner lifecycleOwner){
        removeHandler(lifecycleOwner);
    }

    private synchronized void addHandler(EventHandlerWrapper handlerWrapper){
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

    private synchronized void notifyListeners(){
        for (final EventHandlerWrapper handlerWrapper : handlerList){
            LifecycleOwner lifecycleOwner = handlerWrapper.getLifecycleOwner();
            if (lifecycleOwner != null){
                Lifecycle.State currentState = lifecycleOwner.getLifecycle().getCurrentState();
                if (currentState != Lifecycle.State.STARTED && currentState != Lifecycle.State.RESUMED){
                    continue;
                }
            }
            EventHandler eventHandler = handlerWrapper.getEventHandler();
            if (handlerWrapper.isMainThreadNeeded()){
                executeInMainThread(eventHandler);
            } else {
                executeInBackground(eventHandler);
            }
        }
    }

    private void executeInMainThread(EventHandler eventHandler){
        if (mainThreadHandler == null){
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        mainThreadHandler.post(eventHandler::onEvent);
    }

    private void executeInBackground(EventHandler eventHandler){
        if (backgroundThreadPool == null){
            backgroundThreadPool = Executors.newCachedThreadPool();
        }
        backgroundThreadPool.execute(eventHandler::onEvent);
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
