package avalanche.analytics;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.HashMap;
import java.util.Map;

import avalanche.analytics.ingestion.models.EndSessionLog;
import avalanche.analytics.ingestion.models.EventLog;
import avalanche.analytics.ingestion.models.PageLog;
import avalanche.analytics.ingestion.models.json.EndSessionLogFactory;
import avalanche.analytics.ingestion.models.json.EventLogFactory;
import avalanche.analytics.ingestion.models.json.PageLogFactory;
import avalanche.base.channel.AvalancheChannel;
import avalanche.base.ingestion.models.Log;
import avalanche.base.ingestion.models.json.LogFactory;

import static avalanche.base.channel.DefaultAvalancheChannel.ANALYTICS_GROUP;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnalyticsTest {

    @Before
    public void setUp() {
        Analytics.unsetInstance();
    }

    @Test
    public void singleton() {
        Assert.assertSame(Analytics.getInstance(), Analytics.getInstance());
    }

    @Test
    public void checkFactories() {
        Map<String, LogFactory> factories = Analytics.getInstance().getLogFactories();
        assertTrue(factories.remove(PageLog.TYPE) instanceof PageLogFactory);
        assertTrue(factories.remove(EventLog.TYPE) instanceof EventLogFactory);
        assertTrue(factories.remove(EndSessionLog.TYPE) instanceof EndSessionLogFactory);
        assertTrue(factories.isEmpty());
    }

    @Test
    public void notInit() {

        /* Just check log is discarded without throwing any exception. */
        Analytics.sendPage("test", new HashMap<String, String>());
    }

    private void activityResumed(final String expectedName, android.app.Activity activity) {
        Analytics analytics = Analytics.getInstance();
        AvalancheChannel channel = mock(AvalancheChannel.class);
        analytics.onChannelReady(channel);
        analytics.onActivityResumed(activity);
        //noinspection WrongConstant (well its not a wrong constant but something is odd with compiler here)
        verify(channel).enqueue(argThat(new ArgumentMatcher<Log>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof PageLog) {
                    PageLog pageLog = (PageLog) item;
                    return expectedName.equals(pageLog.getName());
                }
                return false;
            }
        }), eq(ANALYTICS_GROUP));
    }

    @Test
    public void activityResumedWithSuffix() {
        activityResumed("My", new MyActivity());
    }

    @Test
    public void activityResumedNoSuffix() {
        activityResumed("SomeScreen", new SomeScreen());
    }

    @Test
    public void activityResumedNamedActivity() {
        activityResumed("Activity", new Activity());
    }

    @Test
    public void sendEvent() {
        Analytics analytics = Analytics.getInstance();
        AvalancheChannel channel = mock(AvalancheChannel.class);
        analytics.onChannelReady(channel);
        final String name = "testEvent";
        final HashMap<String, String> properties = new HashMap<>();
        properties.put("a", "b");
        Analytics.sendEvent(name, properties);
        //noinspection WrongConstant (well its not a wrong constant but something is odd with compiler here)
        verify(channel).enqueue(argThat(new ArgumentMatcher<Log>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof EventLog) {
                    EventLog eventLog = (EventLog) item;
                    return name.equals(eventLog.getName()) && properties.equals(eventLog.getProperties()) && eventLog.getId() != null;
                }
                return false;
            }
        }), eq(ANALYTICS_GROUP));
    }

    /**
     * Activity with page name automatically resolving to "My" (no "Activity" suffix).
     */
    private static class MyActivity extends android.app.Activity {
    }

    /**
     * Activity with page name automatically resolving to "SomeScreen".
     */
    private static class SomeScreen extends android.app.Activity {
    }

    /**
     * Activity with page name automatically resolving to "Activity", because name == suffix.
     */
    private static class Activity extends android.app.Activity {
    }
}
