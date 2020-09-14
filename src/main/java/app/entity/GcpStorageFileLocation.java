package app.entity;

import java.util.Objects;

public class GcpStorageFileLocation {
    private static final String PROTOCOL = "gs://";
    private static final String DELIMITER = "/";
    private final String name;
    private final String bucket;

    public GcpStorageFileLocation(String name, String bucket) {
        this.name = name;
        this.bucket = bucket;
    }

    public String getName() {
        return name;
    }

    public String getBucket() {
        return bucket;
    }

    public String getAbsolutePath() {
        StringBuilder sb = new StringBuilder(PROTOCOL);
        sb.append(bucket);
        sb.append(DELIMITER);
        sb.append(name);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bucket);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GcpStorageFileLocation that = (GcpStorageFileLocation) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(bucket, that.bucket);
    }

    @Override
    public String toString() {
        return "GcpStorageFileLocation{" +
                "name='" + name + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}
