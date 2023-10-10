package test;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.message.publisher.Subscription;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class AcceptMessageTest {
    private MessagePublisher messagePublisher;
    private MessageConsumer messageConsumer;

    @Before
    public void init() {
        messagePublisher = new MessagePublisherCreator().synchronous().create();
        messageConsumer = new MessageConsumer();
    }

    @Test
    public void runTest() {
//        given that consumer subscribed to message
        var subscription = new Subscription().toMessage(Message.class, messageConsumer::consumeMessage);
        messagePublisher.subscribe(subscription);
//        and consumer did not receive message yet
        assert !messageConsumer.messageReceived();
//        when message is published
        messagePublisher.publishMessage(new Message("hi"));
//        then consumer received the message
        assert messageConsumer.messageReceived();
    }

    private final class MessageConsumer {
        private final AtomicBoolean messageReceived = new AtomicBoolean(false);

        void consumeMessage(Message message) {
            messageReceived.set(true);
        }

        boolean messageReceived() {
            return messageReceived.get();
        }
    }

    @Value
    class Message {
        String value;
    }
}