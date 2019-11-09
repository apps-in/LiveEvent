package apps.in.live_event;

import androidx.lifecycle.LifecycleOwner;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventHandlerListTest {

    private static EventHandler firstEventHandler, secondEventHandler;
    private static LifecycleOwner firstLifeCycleOwner;


    @BeforeClass
    public static void prepare(){
        firstEventHandler = Mockito.mock(EventHandler.class);
        secondEventHandler = Mockito.mock(EventHandler.class);
        firstLifeCycleOwner = Mockito.mock(LifecycleOwner.class);
    }

    private ArrayList<EventHandlerWrapper> mockList;
    private EventHandlerList<Object> list;

    @Before
    public void populateList(){
        mockList = new ArrayList<>();
        mockList.add(new EventHandlerWrapper(firstEventHandler, firstLifeCycleOwner, false));
        mockList.add(new EventHandlerWrapper(secondEventHandler, firstLifeCycleOwner, false));
        mockList.add(Mockito.mock(EventHandlerWrapper.class));
        list = new EventHandlerList<>();
        for (int i = 2; i >= 0; i--) {
            list.add(mockList.get(i));
        }
    }

    @Test
    public void containsTest(){
        assertTrue(list.contains(firstEventHandler));
        assertTrue(list.contains(firstLifeCycleOwner));
        assertTrue(list.contains(mockList.get(2)));
        assertFalse(list.contains(Mockito.mock(EventHandlerWrapper.class)));
    }

    @Test
    public void removeEventHandlerTest(){
        list.remove(secondEventHandler);
        mockList.remove(1);
    }

    @Test
    public void removeLifecycleOwnerTest(){
        list.remove(firstLifeCycleOwner);
        mockList.remove(1);
        mockList.remove(0);
    }

    @Test
    public void lifecycleOwnerSetTest(){
        EventHandlerList handlerList = new EventHandlerList();
        EventHandler firstEventHandler = Mockito.mock(EventHandler.class);
        EventHandler secondEventHandler = Mockito.mock(EventHandler.class);
        LifecycleOwner firstLifecycleOwner = Mockito.mock(LifecycleOwner.class);
        LifecycleOwner secondLifecycleOwner = Mockito.mock(LifecycleOwner.class);
        LifecycleOwner thirdLifecycleOwner = Mockito.mock(LifecycleOwner.class);
        handlerList.add(new EventHandlerWrapper(firstEventHandler, firstLifecycleOwner, false));
        handlerList.add(new EventHandlerWrapper(secondEventHandler, secondLifecycleOwner, false));
        handlerList.add(new EventHandlerWrapper(firstEventHandler,thirdLifecycleOwner, false));
        Set<LifecycleOwner> lifecycleOwnerSet = handlerList.remove(firstEventHandler);
        assertTrue(lifecycleOwnerSet.contains(firstLifecycleOwner));
        assertFalse(lifecycleOwnerSet.contains(secondLifecycleOwner));
        assertTrue(lifecycleOwnerSet.contains(thirdLifecycleOwner));
    }

    @After
    public void check(){
        int idx = 0;
        for(EventHandlerWrapper eventHandlerWrapper : list){
            assertEquals(eventHandlerWrapper, mockList.get(idx));
            idx++;
        }
    }

}
