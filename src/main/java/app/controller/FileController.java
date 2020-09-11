package app.controller;

import app.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/avro")
    public ResponseEntity processFile(@RequestBody String payload) {
        fileService.processFileOnCreate(payload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
