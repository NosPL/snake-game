package other.tests;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.message.publisher.Subscription;
import io.vavr.control.Try;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import other.tests.dto.MessageA;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CorrectThreadSetupTest {
    private ThreadTrackingComponent threadTrackingComponent;

    @Before
    public void init() {
        threadTrackingComponent = new ThreadTrackingComponent();
        assert threadTrackingComponent.getMessageProcessingThreadName() == null;
    }

    @Test
    public void subscriberThreadFromSynchronousMessagePublisher() {
//        given that message publisher is set to synchronous mode
        var messagePublisher = new MessagePublisherCreator().synchronous().create();
//        and thread tracking component subscribed to the MessageA
        var subscription = new Subscription().toMessage(MessageA.class, threadTrackingComponent::saveCurrentThreadName);
        messagePublisher.subscribe(subscription);
//        when MessageA is published
        messagePublisher.publishMessage(new MessageA());
//        then thread name from the component is same as client thread name
        var clientThreadName = Thread.currentThread().getName();
        Assert.assertEquals(clientThreadName, threadTrackingComponent.getMessageProcessingThreadName());
    }

    @Test
    public void subscriberThreadFromMessagePublisherWithDefaultSetupTest() {
//        given that message publisher is built with default settings
        var messagePublisher = new MessagePublisherCreator().create();
//        and thread tracking component subscribed to the MessageA
        var subscription = new Subscription().toMessage(MessageA.class, threadTrackingComponent::saveCurrentThreadName);
        messagePublisher.subscribe(subscription);
//        when MessageA is published
        messagePublisher.publishMessage(new MessageA());
//        then thread name from the component is eventually set to default subscriber name
        String componentThreadName = awaitForThreadName();
        Assert.assertEquals("subscriber-0 thread", componentThreadName);
    }

    @Test
    public void subscriberThreadWithCustomExecutorTest() {
//        given that message publisher is built with default settings
        var messagePublisher = new MessagePublisherCreator().create();
//        and thread tracking component subscribed
        var subscription = new Subscription().toMessage(MessageA.class, threadTrackingComponent::saveCurrentThreadName);
//        and component set its own executor
        subscription.executorService(customExecutorWithName("custom executor thread"));
        messagePublisher.subscribe(subscription);
//        when MessageA is published
        messagePublisher.publishMessage(new MessageA());
//        then thread name from the component is eventually set to the name of custom executor name
        String componentThreadName = awaitForThreadName();
        Assert.assertEquals("custom executor thread", componentThreadName);
    }

    private final class ThreadTrackingComponent {
        private volatile String messageProcessingThreadName;

        private void saveCurrentThreadName(MessageA msg) {
            messageProcessingThreadName = Thread.currentThread().getName();
        }

        private String getMessageProcessingThreadName() {
            return messageProcessingThreadName;
        }
    }

    private ExecutorService customExecutorWithName(String threadName) {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName(threadName);
                    return thread;
                });
    }

    private String awaitForThreadName() {
        int i = 0;
        while (threadTrackingComponent.getMessageProcessingThreadName() == null) {
            i += 100;
            Try.run(() -> Thread.sleep(10));
            if (i > 3_000)
                throw new RuntimeException("waiting for thread tracking component result was too long");
        }
        return threadTrackingComponent.getMessageProcessingThreadName();
    }
}