# gcp based back-end service

## General description

### File upload generates two events into two pub/sub topics -> subscribers PUSH messages to separate endpoints -> each endpoint handle own table in different manner