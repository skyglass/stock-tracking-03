package net.greeta.stock.order;

import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.testdata.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderTestDataService extends TestDataService {

    @Autowired
    @Qualifier("orderJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void resetDatabase() {
        executeString("DELETE FROM orders");
        executeString("DELETE FROM token_entry");
        executeString("DELETE FROM association_value_entry");
        executeString("DELETE FROM dead_letter_entry");
        executeString("DELETE FROM saga_entry");
    }
}
