package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.ServiceActivator;

@SpringBootApplication
public class ApplicationMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void messageReceiver(String payload) {
        LOGGER.info("Message arrived! Payload: " + payload);
    }
}
