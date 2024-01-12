package net.greeta.stock.testdata;

import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class TestDataService {

    protected abstract JdbcTemplate getJdbcTemplate();

    public abstract void resetDatabase();

    public void executeScript(File file) throws IOException {
        String sqlScript = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        getJdbcTemplate().execute(sqlScript);
    }

    public void executeString(String sql) {
        getJdbcTemplate().execute(sql);
    }

}
