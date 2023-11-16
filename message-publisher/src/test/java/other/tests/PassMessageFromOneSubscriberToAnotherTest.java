package other.tests;

import com.noscompany.message.publisher.Subscription;
import other.tests.dto.MessageA;
import other.tests.dto.MessageB;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;

public class PassMessageFromOneSubscriberToAnotherTest extends AbstractTestClass {
    private SubscriberA subscriberA;
    private SubscriberB subscriberB;

    @Before
    public void init() {
        subscriberA = new SubscriberA();
        subscriberB = new SubscriberB();
    }

    @Test
    public void test() {
//        given that subscriber A subscribed to MessageA and responds to it with 'MessageB'
        messagePublisher.subscribe(new Subscription().toMessage(MessageA.class, subscriberA::processMessageA).subscriberName("subscriber A"));
//        and subscriber B subscribed to 'MessageB'
        messagePublisher.subscribe(new Subscription().toMessage(MessageB.class, subscriberB::processMsgA).subscriberName("subscriber B"));
//        and subscriber B didn't receive any message yet
        assert subscriberB.getReceivedMessage() == null;
//        when MessageA is published
        messagePublisher.publishMessage(new MessageA());
//        then subscriber B receives 'MessageB'
        assert subscriberB.getReceivedMessage().equals(new MessageB());
    }

    private final class SubscriberA {

        MessageB processMessageA(MessageA messageA) {
            return new MessageB();
        }
    }

    private final class SubscriberB {
        @Getter
        private Object receivedMessage = null;

        void processMsgA(MessageB messageB) {
            this.receivedMessage = messageB;
        }
    }
}