package net.digicode;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class SimpleTest {

    @Test
    public void test() {
        String[] args = {"kiev"};
        EntryPoint.main(args);
        File output = new File("csv1-data.csv");
        assertTrue(output.exists());
        output.deleteOnExit();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkWrongArgumentException() {
        String[] args = {};
        EntryPoint.main(args);
    }

    @Test
    public void checkResponse() throws IOException, URISyntaxException {
        Config config = new Config("/app.properties");
        JsonToCsvWriter writer = new JsonToCsvWriter(config);
        File jsonData = new File(getClass().getResource("/json.json").toURI());
        writer.writeJsonToCSV(jsonData.toURI().toURL());

        File actual = new File("./csv1-data.csv");
        Assert.assertTrue(actual.exists());
        File expected = new File(getClass().getResource("/csv-data-kiev.csv").toURI());

        assertEquals(FileUtils.readLines(actual), FileUtils.readLines(expected));
        actual.deleteOnExit();
    }

    @Test
    public void emptyResponse() throws IOException, URISyntaxException {
        Config config = new Config("/app.properties");
        JsonToCsvWriter writer = new JsonToCsvWriter(config);
        File jsonData = new File(getClass().getResource("/empty-response.json").toURI());
        writer.writeJsonToCSV(jsonData.toURI().toURL());

        File actual = new File("./csv1-data.csv");
        Assert.assertTrue(!actual.exists() || actual.length() == 0);
        actual.deleteOnExit();
    }
}
