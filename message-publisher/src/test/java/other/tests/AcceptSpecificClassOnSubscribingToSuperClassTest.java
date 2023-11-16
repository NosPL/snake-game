package other.tests;

import com.noscompany.message.publisher.Subscription;
import other.tests.dto.AbstractMessage;
import other.tests.dto.SpecificMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

public class AcceptSpecificClassOnSubscribingToSuperClassTest extends AbstractTestClass {
    private MessageConsumer messageConsumer;

    @Before
    public void init() {
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
        messagePublisher.publishMessage(new SpecificMessage());
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

}