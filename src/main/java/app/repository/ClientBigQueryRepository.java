package app.repository;

import app.entity.Client;
import app.exception.AppException;
import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBigQueryRepository {

    private final Logger log = LoggerFactory.getLogger(ClientBigQueryRepository.class);
    private static final String ID = "id";
    private static final String NAME = "name";

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
    public void saveAvroFromGcpStorage(String sourceUri) throws AppException {
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
                log.info("Table {} is successfully appended by AVRO file loaded from GCS", tableNameFull);
            } else {
                log.error("Error: BigQuery was unable to load into the table due to an error: {}", job.getStatus().getError().getMessage());
            }
        } catch (BigQueryException | InterruptedException e) {
            log.error("Row not added during load append ", e);
            throw new AppException("Error: BigQuery was unable to load into the table", e);
        }
    }

    public void saveClientObligateFields(Client client) throws AppException {
            try {
                String tableName = "`" + datasetName + "." + tableNamePartial + "`";
                String query = "INSERT " + tableName + " (`id`, `name`) VALUES (@id, @name)";

                // Note: Standard SQL is required to use query parameters.
                QueryJobConfiguration queryConfig =
                        QueryJobConfiguration.newBuilder(query)
                                .addNamedParameter(ID, QueryParameterValue.int64(client.getId()))
                                .addNamedParameter(NAME, QueryParameterValue.string(client.getName().toString()))
                                .build();
                Job job = bigquery.create(JobInfo.of(queryConfig));

                job = job.waitFor();
                if (job.isDone()) {
                    log.info("Table {} is successfully appended by AVRO file loaded from GCS", tableNamePartial);
                } else {
                    log.error("Error: BigQuery was unable to load into the table due to an error: {}", job.getStatus().getError().getMessage());
                }
            } catch (BigQueryException | InterruptedException e) {
                log.error("Row not added during load append ", e);
                throw new AppException("Error: BigQuery was unable to load into the table", e);
            }
    }
}
