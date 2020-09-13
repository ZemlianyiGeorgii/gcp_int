package app.controller;

import app.entity.Body;
import app.entity.GcpStorageFileLocation;
import app.service.FileService;
import app.utils.GcpStorageMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
public class PubSubController {

    private final Logger log = LoggerFactory.getLogger(PubSubController.class);

    private final FileService fileService;

    @Autowired
    public PubSubController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/")
    public ResponseEntity receiveMessage(@RequestBody Body body) {
        Body.Message message = body.getMessage();
        if (message == null) {
            String msg = "Bad Request: invalid Pub/Sub message format";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }

        String pubSubMessage = message.getData();
        Optional<GcpStorageFileLocation> fileLocationOptional;
        try {
            String decodedMessage = new String(Base64.getDecoder().decode(pubSubMessage));
            fileLocationOptional = GcpStorageMessageParser.getFileLocationFromMessage(decodedMessage);
        } catch (Exception e) {
            String msg = "Error: Invalid Pub/Sub message JSON";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        if (fileLocationOptional.isEmpty()) {
            String msg = "Error: Invalid Cloud Storage notification: expected name and bucket properties";
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }

        try {
            fileService.handleFile(fileLocationOptional.get());
        } catch (Exception e) {
            String msg = String.format("Error: Handling file: %s", e.getMessage());
            log.error(msg);
            return new ResponseEntity(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
