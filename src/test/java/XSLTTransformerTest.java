import com.labab.XSLTTransformer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XSLTTransformerTest {
    private Logger mockLogger = Mockito.mock(Logger.class);

    private static final String XML_FILE_PATH = "src/main/resources/beer.xml";
    private static final String XSLT_FILE_PATH = "src/main/resources/beer_transform.xslt";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/transformed_beer.xml";



    @AfterEach
    public void tearDown() {
        File outputFile = new File(OUTPUT_FILE_PATH);
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testXSLTTransformation() {
        XSLTTransformer.main(new String[]{});

        File outputFile = new File(OUTPUT_FILE_PATH);
        assertTrue(outputFile.exists(), "Output file should be created after transformation");
    }

    @Test
    public void testOutputFileContent() throws Exception {
        XSLTTransformer.main(new String[]{});

        File outputFile = new File(OUTPUT_FILE_PATH);
        assertTrue(outputFile.exists(), "Output file should be created after transformation");

        String content = new String(Files.readAllBytes(outputFile.toPath()));

        assertTrue(content.contains(" <Manufacturer>Brewery A</Manufacturer>"), "Output should contain expected elements");
    }
}
