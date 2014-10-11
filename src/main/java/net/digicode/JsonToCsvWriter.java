package net.digicode;

import com.jayway.jsonpath.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static net.digicode.Constants.PROPERTIES.*;

public class JsonToCsvWriter {

    // Properties holder
    private Config config;

    public JsonToCsvWriter(Config config) {
        this.config = config;
    }

    /**
     * writes {@code source} to file with name specified in configuration file
     *
     * @param source URL to recourse
     */
    public void writeJsonToCSV(final URL source) {
        try {
            File csvFile = new File(config.getProperty(CSV_FILE_NAME));
            BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile.getAbsoluteFile()));
            ReadContext context = JsonPath.using(contextConfig()).parse(source);

            List<String> paths = getEntityPaths(source);
            for (String path : paths) {
                String[] csvColumns = config.getProperty(WRITE_FIELDS_PROP).split(",");
                StringBuilder csvRow = makeCSVRow(context, csvColumns, path).append('\n');
                bw.write(csvRow.toString());
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't write to file " + config.getProperty(CSV_FILE_NAME));
        }
    }

    // retrieve all needed data from ReadContext
    private StringBuilder makeCSVRow(ReadContext context, String[] csvColumns, String entityPath) {
        StringBuilder csvRow = new StringBuilder();
        for (int j = 0; j < csvColumns.length - 1; j++) {
            csvRow.append(context.read(entityPath + csvColumns[j])).append(getCsvSeparator());
        }
        return csvRow.append(context.read(entityPath + csvColumns[csvColumns.length - 1]));
    }

    // JsonPath returns null for missing leaves
    private com.jayway.jsonpath.Configuration contextConfig() {
        return com.jayway.jsonpath.Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
    }

    // get csv file separator
    private String getCsvSeparator() {
        String separator;
        if (StringUtils.isNotBlank(config.getProperty(SEPARATOR_PROP))) {
            separator = config.getProperty(SEPARATOR_PROP);
        } else {
            separator = DEFAULT_SEPARATOR;
        }
        return separator;
    }

    // get JsonPath to entities of JsonArray
    private List<String> getEntityPaths(URL url) throws IOException {
        Configuration asPathConfig = Configuration.builder().options(Option.AS_PATH_LIST).build();
        List<String> result;
        try {
            result = JsonPath.using(asPathConfig).parse(url)
                    .read(config.getProperty(ENTITY_PATH));
        } catch (PathNotFoundException e) {
            result = new ArrayList<String>();
        }
        return result;
    }
}