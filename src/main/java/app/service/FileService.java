package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    Logger log = LoggerFactory.getLogger(FileService.class);

    public void processFileOnCreate(String payload) {
        log.info(payload);
    }
}
