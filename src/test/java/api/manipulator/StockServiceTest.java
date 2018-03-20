package api.manipulator;

import com.stocks.api.dal.StockRepository;
import com.stocks.api.exceptions.StockAlreadyExistsException;
import com.stocks.api.manipulator.StockService;
import com.stocks.api.model.Stock;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class StockServiceTest {

    @MockBean StockRepository stockRepository;
    @Captor ArgumentCaptor<Stock> stockArgCaptor;

    private StockService stockService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        stockService = new StockService(stockRepository, 5);
    }

    @Test
    public void testCreate() {
        final Long id = 123L;
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol);

        final Stock expected = new Stock()
                .withName(name)
                .withCreationDate(new Date())
                .withId(id)
                .withPrice(price);

        given(stockRepository.save(any(Stock.class))).willReturn(expected);

        final Stock actual = stockService.create(stock);

        assertEquals(expected, actual);
        verify(stockRepository, times(1)).save(stockArgCaptor.capture());
        assertEquals(stock, stockArgCaptor.getValue());
    }

    @Test(expected = StockAlreadyExistsException.class)
    public void testCreateAlreadyPresent() {
        final Long id = 123L;
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);
        final SQLException exception = new SQLException("", "", 23001);
        final ConstraintViolationException constraintViolationException = new ConstraintViolationException("Primary key voilation.", exception , "Primary key voilation");
        given(stockRepository.save(any(Stock.class)))
                .willThrow(new DataIntegrityViolationException("Some problem with data integrity", constraintViolationException));
        final Stock createdStock = stockService.create(stock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNoName() {
        final Long id = 123L;
        final String name = null;
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        Stock createdStock = stockService.create(stock);
        createdStock.equals(stock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNoSymbol() {
        final Long id = 123L;
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = null;
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withLastUpdateDate(lastUpdate);

        Stock createdStock = stockService.create(stock);
        createdStock.equals(stock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNoPrice() {
        final Long id = 123L;
        final String name = "a stock name";

        final String symbol = "a stock symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        Stock createdStock = stockService.create(stock);
        createdStock.equals(stock);
    }

    @Test
    public void testUpdate() {
        final Long id = 123L;
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock stock = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);

        final Double newPrice = 22.03;
        when(stockRepository.findOne(id)).thenReturn(stock);

        stockService.updatePrice(id, newPrice, 1L);
        verify(stockRepository, times(1)).findOne(id);
        verify(stockRepository, times(1)).updatePrice(id, newPrice, 1L);
    }

    @Test
    public void testGet() {
        final Long id = 123L;
        final String name = "a stock name";
        final Double price = 20.34;
        final String symbol = "a symbol";
        final Date lastUpdate = new Date();
        final Stock expected = new Stock()
                .withId(id)
                .withName(name)
                .withPrice(price)
                .withSymbol(symbol)
                .withLastUpdateDate(lastUpdate);
        given(stockRepository.findOne(id)).willReturn(expected);

        final Stock actual = stockService.get(id);

        assertEquals(expected, actual);
        verify(stockRepository, times(1)).findOne(id);
    }
}
