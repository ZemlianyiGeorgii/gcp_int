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

    public void saveAvroToBigQuery(GcpStorageFileLocation fileLocation, boolean saveAllFields) throws AppException, IOException {
        if (saveAllFields) {
            clientBigQueryRepository.saveAvroFromGcpStorage(fileLocation.getAbsolutePath());
        } else {
            savePartFields(fileLocation);
        }
    }

    public void savePartFields(GcpStorageFileLocation fileLocation) throws IOException, AppException {
        DataFileStream<Client> dataFileStream = clientGcpStorageRepository.readFromAvroAsStream(fileLocation.getBucket(), fileLocation.getName());
        while(dataFileStream.hasNext()) {
           Client client = dataFileStream.next();
            clientBigQueryRepository.saveClientObligateFields(client);
        }
        try {
            dataFileStream.close();
        } catch (IOException e) {
            log.error("Error: Can't close Avro data file stream correctly", e);
        }
    }

}
