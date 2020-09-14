package app.repository;

import app.entity.Client;
import app.exception.AppException;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;

public class ClientGcpStorageRepository {

    private Storage storage;

    public ClientGcpStorageRepository(Storage storage) {
        this.storage = storage;
    }

    public DataFileStream<Client> readFromAvroAsStream(String bucket, String name) throws IOException, AppException {
        InputStream inputStream = Channels.newInputStream(storage.reader(bucket, name));
        DatumReader<Client> clientDatumReader = new SpecificDatumReader<>(Client.class);
        DataFileStream<Client> dataFileStream = new DataFileStream<>(inputStream, clientDatumReader);
        Schema realSchema = dataFileStream.getSchema();

        if (!Client.getClassSchema().equals(realSchema)) {
          throw new AppException("Error: Inappropriate AVRO schema " + realSchema.toString());
        }

        return dataFileStream;
    }

}
