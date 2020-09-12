package app.consumer;

import com.google.cloud.pubsub.v1.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.PubSubAdmin;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.AcknowledgeablePubsubMessage;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class WebController {

    Logger log = LoggerFactory.getLogger(WebController.class);

    private final PubSubTemplate pubSubTemplate;

    private final PubSubAdmin pubSubAdmin;

    private final ArrayList<Subscriber> allSubscribers;

    @Autowired
    public WebController(PubSubTemplate pubSubTemplate, PubSubAdmin pubSubAdmin) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubAdmin = pubSubAdmin;
        this.allSubscribers = new ArrayList<>();
    }

    @GetMapping("/pull")
    public RedirectView pull(@RequestParam("subscription1") String subscriptionName) {

        Collection<AcknowledgeablePubsubMessage> messages = this.pubSubTemplate.pull(subscriptionName, 10, true);

        if (messages.isEmpty()) {
            return buildStatusView("No messages available for retrieval.");
        }

        RedirectView returnView;
        try {
            ListenableFuture<Void> ackFuture = this.pubSubTemplate.ack(messages);
            ackFuture.get();
            returnView = buildStatusView(String.format("Pulled and acked %s message(s)", messages.size()));
        }
        catch (Exception ex) {
            log.warn("Acking failed.", ex);
            returnView = buildStatusView("Acking failed");
        }

        return returnView;
    }

    private RedirectView buildStatusView(String statusMessage) {
        RedirectView view = new RedirectView("/");
        view.addStaticAttribute("statusMessage", statusMessage);
        return view;
    }
}
