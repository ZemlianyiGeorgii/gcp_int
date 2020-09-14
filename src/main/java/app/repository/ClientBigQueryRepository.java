package app.repository;

import app.exception.AppException;
import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBigQueryRepository {

    private final Logger log = LoggerFactory.getLogger(ClientBigQueryRepository.class);

    private BigQuery bigquery;
    private String datasetName;
    private String tableNameFull;
    private String tableNamePartial;

    public ClientBigQueryRepository(BigQuery bigquery, String datasetName, String tableNameFull, String tableNamePartial) {
        this.bigquery = bigquery;
        this.datasetName = datasetName;
        this.tableNameFull = tableNameFull;
        this.tableNamePartial = tableNamePartial;
    }

    //TODO: Clean code from comments
    public void loadAvroFromGcpStorage(String sourceUri) throws AppException {
        log.info("Yhis is loadAvroFromGcpStorage + {}", tableNameFull);
        try {
            TableId tableId = TableId.of(datasetName, tableNameFull);
            LoadJobConfiguration loadConfig =
                    LoadJobConfiguration.newBuilder(tableId, sourceUri)
                            .setFormatOptions(FormatOptions.avro())
                            .setWriteDisposition(JobInfo.WriteDisposition.WRITE_APPEND)
                            .build();

            Job job = bigquery.create(JobInfo.of(loadConfig));

            job = job.waitFor();
            if (job.isDone()) {
                log.info("Table is successfully appended by AVRO file loaded from GCS");
            } else {
                log.error("Error: BigQuery was unable to load into the table due to an error: {}", job.getStatus().getError().getMessage());
            }
        } catch (BigQueryException | InterruptedException e) {
            log.error("Row not added during load append ", e);
            throw new AppException("Error: BigQuery was unable to load into the table", e);
        }
    }


    public void loadAvroFromGcpStoragePart(String sourceUri) throws AppException {
        log.info("Yhis is loadAvroFromGcpStoragePart + {}", tableNamePartial);
        try {
            TableId tableId = TableId.of(datasetName, tableNamePartial);
            LoadJobConfiguration loadConfig =
                    LoadJobConfiguration.newBuilder(tableId, sourceUri)
                            .setFormatOptions(FormatOptions.avro())
                            .setWriteDisposition(JobInfo.WriteDisposition.WRITE_APPEND)
                            .build();

            Job job = bigquery.create(JobInfo.of(loadConfig));

            job = job.waitFor();
            if (job.isDone()) {
                log.info("Table is successfully appended by AVRO file loaded from GCS");
            } else {
                log.error("Error: BigQuery was unable to load into the table due to an error: {}", job.getStatus().getError().getMessage());
            }
        } catch (BigQueryException | InterruptedException e) {
            log.error("Row not added during load append ", e);
            throw new AppException("Error: BigQuery was unable to load into the table", e);
        }
    }
}
