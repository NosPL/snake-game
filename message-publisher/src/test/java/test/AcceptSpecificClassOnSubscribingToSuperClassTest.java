package test;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.message.publisher.Subscription;
import lombok.Value;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

public class AcceptSpecificClassOnSubscribingToSuperClassTest {
    private MessagePublisher messagePublisher;
    private MessageConsumer messageConsumer;

    @Before
    public void init() {
        messagePublisher = new MessagePublisherCreator().synchronous().create();
         messageConsumer = new MessageConsumer();
    }

    @Test
    public void runTest() {
//        Given that abstract message is super class of specific message
        assert AbstractMessage.class.isAssignableFrom(SpecificMessage.class);
//        and consumer subscribed to abstract message
        var subscription = new Subscription().toMessage(AbstractMessage.class, messageConsumer::consumeMessage);
        messagePublisher.subscribe(subscription);
//        and consumer did not receive message yet
        assert !messageConsumer.messageReceived();
//        when specific message is sent
        messagePublisher.publishMessage(new SpecificMessage("hi"));
//        then consumer received the specific message
        assertTrue(messageConsumer.messageReceived());
    }

    private final class MessageConsumer {
        private final AtomicBoolean messageReceived = new AtomicBoolean(false);

        void consumeMessage(AbstractMessage abstractMessage) {
            messageReceived.set(true);
        }

        boolean messageReceived() {
            return messageReceived.get();
        }
    }

    interface AbstractMessage {
    }

    @Value
    class SpecificMessage implements AbstractMessage {
        String value;
    }
}