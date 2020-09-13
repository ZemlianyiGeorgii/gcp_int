package app.service;

import app.entity.GcpStorageFileLocation;
import app.exception.ExtendaException;
import app.repository.BigQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    //@Autowired
    //private BigQueryRepository bigQueryRepository;

/*    public FileService(BigQueryRepository bigQueryRepository) {
        this.bigQueryRepository = bigQueryRepository;
    }*/

    //TODO: CLean code
    public void handleFile(GcpStorageFileLocation fileLocation) throws ExtendaException {
        log.info("Inserting BigQuery from {}", fileLocation.getAbsolutePath());
        //bigQueryRepository.loadAvroFromGcpStorage(fileLocation.getAbsolutePath());
    }
}
