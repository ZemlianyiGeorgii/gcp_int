package app.config;

import app.repository.ClientBigQueryRepository;
import app.repository.ClientGcpStorageRepository;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ClientGcpStorageRepository clientGcpStorageRepository() {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        return new ClientGcpStorageRepository(storage);
    }

    @Bean
    public ClientBigQueryRepository clientBigQueryRepository(
            @Value("#{systemEnvironment['BQ_DATASET']}") String dataSetName,
            @Value("#{systemEnvironment['BQ_TABLE_FULL']}") String tableNameFull,
            @Value("#{systemEnvironment['BQ_TABLE_PARTIAL']}") String tableNamePartial) {
        BigQuery bigqueryClient = BigQueryOptions.getDefaultInstance().getService();
        return new ClientBigQueryRepository(bigqueryClient, dataSetName, tableNameFull, tableNamePartial);
    }
}
