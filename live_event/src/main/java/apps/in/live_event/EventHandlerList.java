package apps.in.live_event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


class EventHandlerList implements Iterable<EventHandlerWrapper> {

    public class ListIterator implements Iterator<EventHandlerWrapper>{

        private Node next = head;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public EventHandlerWrapper next() {
            EventHandlerWrapper value = next.value;
            next = next.nextNode;
            return value;
        }
    }

    private static class Node{

        private final EventHandlerWrapper value;
        private Node nextNode;

        public Node(EventHandlerWrapper value, Node nextNode) {
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    private Node head = null;

    public void add(EventHandlerWrapper e){
        Node node = new Node(e, head);
        head = node;
    }

    public Set<LifecycleOwner> remove(Object obj){
        Set<LifecycleOwner> lifecycleOwnerSet = new HashSet<>();
        Node previousNode = null;
        Node currentNode = head;
        while (currentNode != null){
            EventHandlerWrapper eventHandlerWrapper = currentNode.value;
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
        for (EventHandlerWrapper eventHandlerWrapper : this){
            if (eventHandlerWrapper.equals(obj)){
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public Iterator<EventHandlerWrapper> iterator() {
        return new ListIterator();
    }
}
