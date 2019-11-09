package apps.in.live_event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


class EventHandlerList<T> implements Iterable<EventHandlerWrapper<T>> {

    public class ListIterator implements Iterator<EventHandlerWrapper<T>>{

        private Node<T> next = head;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public EventHandlerWrapper<T> next() {
            EventHandlerWrapper value = next.value;
            next = next.nextNode;
            return value;
        }
    }

    private static class Node<T>{

        private final EventHandlerWrapper<T> value;
        private Node<T> nextNode;

        public Node(EventHandlerWrapper<T> value, Node<T> nextNode) {
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    private Node<T> head = null;

    public void add(EventHandlerWrapper<T> e){
        Node<T> node = new Node<>(e, head);
        head = node;
    }

    public Set<LifecycleOwner> remove(Object obj){
        Set<LifecycleOwner> lifecycleOwnerSet = new HashSet<>();
        Node<T> previousNode = null;
        Node<T> currentNode = head;
        while (currentNode != null){
            EventHandlerWrapper<T> eventHandlerWrapper = currentNode.value;
            if (eventHandlerWrapper.equals(obj)){
                LifecycleOwner lifecycleOwner = eventHandlerWrapper.getLifecycleOwner();
                if (lifecycleOwner != null){
                    lifecycleOwnerSet.add(lifecycleOwner);
                }
                if (previousNode != null){
                    previousNode.nextNode = currentNode.nextNode;
                } else {
                    head = currentNode.nextNode;
                }
            } else {
                previousNode = currentNode;
            }
            currentNode = currentNode.nextNode;
        }
        return lifecycleOwnerSet;
    }

    public boolean contains(Object obj){
        for (EventHandlerWrapper<T> eventHandlerWrapper : this){
            if (eventHandlerWrapper.equals(obj)){
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public Iterator<EventHandlerWrapper<T>> iterator() {
        return new ListIterator();
    }
}
