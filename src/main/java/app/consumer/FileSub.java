package app.consumer;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FileSub {

    private final Logger log = LoggerFactory.getLogger(FileSub.class);

    @Value("${PROJ_ID}")
    private String projectId;
    @Value("${SUB_ID}")
    private String subscriptionId;

    public void subscribeAsync() {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
            log.info("Id: {}", message.getMessageId());
            log.info("Data: {}", message.getData().toStringUtf8());
            consumer.ack();
        };

        Subscriber subscriber = null;

        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();

            subscriber.startAsync().awaitRunning();
            log.info("Listening for messages on {}", subscriptionName.toString());
            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 30s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }
}