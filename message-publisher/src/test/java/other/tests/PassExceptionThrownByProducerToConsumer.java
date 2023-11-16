package other.tests;

import com.noscompany.message.publisher.Subscription;
import other.tests.dto.MessageException;
import other.tests.dto.MessageA;
import other.tests.dto.MessageB;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

public class PassExceptionThrownByProducerToConsumer extends AbstractTestClass {
    private final ExceptionProducer producer = new ExceptionProducer();
    private final ExceptionConsumer consumer = new ExceptionConsumer();

    @Test
    public void test() {
//        given the producer subscribed for a MessageA and responds to it with throwing MessageException
        var producerSubscription = new Subscription().toMessage(MessageA.class, producer::throwException);
        messagePublisher.subscribe(producerSubscription);
//        and consumer subscribed for both MessageB and ExceptionMessage
        var consumerSubscription = new Subscription()
                .toMessage(MessageException.class, consumer::consumeException)
                .toMessage(MessageB.class, consumer::consumeMessageB);
        messagePublisher.subscribe(consumerSubscription);
//        and consumer did not receive message yet
        assert consumer.getReceivedMessage() == null;
//        when MessageA is published
        messagePublisher.publishMessage(new MessageA());
//        then consumer receives ExceptionMessage
        Assert.assertEquals(MessageException.class, consumer.getReceivedMessage().getClass());

    }

    private final class ExceptionProducer {

        MessageB throwException(MessageA messageA) {
            throw new MessageException();
        }
    }

    private final class ExceptionConsumer {
        @Getter
        private Object receivedMessage;

        void consumeException(MessageException messageException) {
            receivedMessage = messageException;
        }

        void consumeMessageB(MessageB messageB) {
            receivedMessage = messageB;
        }
    }

}