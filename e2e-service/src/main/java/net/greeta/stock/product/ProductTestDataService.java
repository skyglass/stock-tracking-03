package net.greeta.stock.product;

import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.testdata.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductTestDataService extends TestDataService {

    @Autowired
    @Qualifier("productJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void resetDatabase() {
        executeString("DELETE FROM product");
        executeString("DELETE FROM order_event_result");
    }

}
