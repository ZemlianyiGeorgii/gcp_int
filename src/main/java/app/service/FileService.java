package app.service;

import app.entity.Client;
import app.entity.GcpStorageFileLocation;
import app.exception.AppException;
import app.repository.ClientBigQueryRepository;
import app.repository.ClientGcpStorageRepository;
import org.apache.avro.file.DataFileStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private ClientGcpStorageRepository clientGcpStorageRepository;
    private ClientBigQueryRepository clientBigQueryRepository;

    public FileService(ClientGcpStorageRepository clientGcpStorageRepository, ClientBigQueryRepository clientBigQueryRepository) {
        this.clientGcpStorageRepository = clientGcpStorageRepository;
        this.clientBigQueryRepository = clientBigQueryRepository;
    }

    //TODO: CLean code
    public void saveAvroToBigQuery(GcpStorageFileLocation fileLocation) throws AppException, IOException {
        //TODO: remove logger + remove getAbsolute
        log.info("Inserting BigQuery from {}", fileLocation.getAbsolutePath());
        DataFileStream<Client> dataFileStream = clientGcpStorageRepository.readFromAvroAsStream(fileLocation.getBucket(), fileLocation.getName());

        while (dataFileStream.hasNext()) {
            Client client = dataFileStream.next();
            log.info("==============================================");
            log.info("Id: " + client.getId());
            log.info("Name: " + client.getName());
            log.info("Address: " + client.getAddress());
            log.info("Phone: " + client.getPhone());
        }
        clientBigQueryRepository.loadAvroFromGcpStorage(fileLocation.getAbsolutePath());
        clientBigQueryRepository.loadAvroFromGcpStoragePart(fileLocation.getAbsolutePath());
        try {
            dataFileStream.close();
        } catch (IOException e) {
            log.error("Error: Can't close input stream of AVRO file");
        }
    }
}
