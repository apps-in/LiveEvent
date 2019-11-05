package apps.in.live_event;

import androidx.lifecycle.LifecycleOwner;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class EventHandlerWrapperTest {

    private static LifecycleOwner firstLifeCycleOwner;
    private static LifecycleOwner secondLifeCycleOwner;
    private static EventHandler firstEventHandler;
    private static EventHandler secondEventHandler;
    private static EventHandlerWrapper first;
    private static EventHandlerWrapper second;
    private static EventHandlerWrapper linkCopy;

    @BeforeClass
    public static void prepare(){
        firstLifeCycleOwner = Mockito.mock(LifecycleOwner.class);
        secondLifeCycleOwner = Mockito.mock(LifecycleOwner.class);
        firstEventHandler = Mockito.mock(EventHandler.class);
        secondEventHandler = Mockito.mock(EventHandler.class);
        first = new EventHandlerWrapper(firstEventHandler, firstLifeCycleOwner,false);
        second = new EventHandlerWrapper(secondEventHandler, secondLifeCycleOwner, true);
        linkCopy = first;
    }

    @Test
    public void equalsTest(){
        assertEquals(first, linkCopy);
        assertEquals(first, firstEventHandler);
        assertEquals(first, firstLifeCycleOwner);

    }

    @Test
    public void notEqualsTest(){
        assertNotEquals(first, null);
        assertNotEquals(first, new Object());
        assertNotEquals(first, second);
        assertNotEquals(first, secondEventHandler);
        assertNotEquals(first, secondLifeCycleOwner);
    }

    @Test
    public void getEventHandlerTest(){
        assertEquals(first.getEventHandler(), firstEventHandler);
        assertEquals(second.getEventHandler(), secondEventHandler);
    }

    @Test
    public void getLifecycleOwnerTest(){
        assertEquals(first.getLifecycleOwner(), firstLifeCycleOwner);
        assertEquals(second.getLifecycleOwner(), secondLifeCycleOwner);
    }

    @Test
    public void getMainThreadNeededTest(){
        assertFalse(first.isMainThreadNeeded());
        assertTrue(second.isMainThreadNeeded());
    }

}
