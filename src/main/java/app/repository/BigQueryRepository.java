package app.repository;

import app.exception.ExtendaException;
//import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigQueryRepository {

    private final Logger log = LoggerFactory.getLogger(BigQueryRepository.class);

    String datasetName = "test_avro";
    String tableName = "table_1";

    //TODO: Clean code from comments
/*    public void loadAvroFromGcpStorage(String sourceUri) throws ExtendaException {
        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

            TableId tableId = TableId.of(datasetName, tableName);
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
            throw new ExtendaException("Error: BigQuery was unable to load into the table", e);
        }
    }*/
}
