package utils;

import app.entity.GcpStorageFileLocation;
import app.utils.GcpStorageMessageParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class GcpStorageMessageParserTest {

    private final static String BASE_PROFILE_PATH = "src/test/resources/";
    private final static String PATH_TO_PUB_SUB_MESSAGE = BASE_PROFILE_PATH + "pub_sub_message.json";
    private final static GcpStorageFileLocation EXPECTED_FILE_LOCATION = new GcpStorageFileLocation("file_name", "bucket");

    @Test
    public void testGetFileAddress() throws IOException {
        String actual = Files.readString(Path.of(PATH_TO_PUB_SUB_MESSAGE));

        Optional<GcpStorageFileLocation> gsFilePath = GcpStorageMessageParser.getFileLocationFromMessage(actual);

        assertThat(gsFilePath).isNotEmpty();
        assertThat(gsFilePath.get()).isEqualTo(EXPECTED_FILE_LOCATION);
    }
}
