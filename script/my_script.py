import sys
from google.cloud import bigquery

# Construct a BigQuery client object.
client = bigquery.Client()

dataset_id = sys.argv[1]

dataset = client.get_dataset(dataset_id)  # Make an API request.

entry = bigquery.AccessEntry(
    role='WRITER',
    entity_type='userByEmail',
    entity_id=' hii-rra-datawarehouse-loader@analytics-staging-e1b8.iam.gserviceaccount.com',
)

entries = list(dataset.access_entries)
entries.append(entry)
dataset.access_entries = entries

dataset = client.update_dataset(dataset, ['access_entries'])