package other.tests;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import org.junit.Before;

public class AbstractTestClass {
    protected MessagePublisher messagePublisher;

    @Before
    public void initializeMessagePublisher() {
        messagePublisher = new MessagePublisherCreator().synchronous().create();
    }
}