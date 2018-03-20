package com.stocks.api.manipulator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.stocks.api.dal.StockRepository;
import com.stocks.api.exceptions.ResourceNotFoundException;
import com.stocks.api.exceptions.StockAlreadyExistsException;
import com.stocks.api.model.Stock;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Manages operations related to stocks.
 */
@Service
public class StockService {
    private static Logger logger = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    private final Integer pageSize;

    @Autowired
    public StockService(StockRepository stockRepository, Integer pageSize) {
        this.stockRepository = stockRepository;
        this.pageSize = pageSize;
    }

    /**
     * Creates a stock if the symbol of the stock does not exist in our database.
     * Making it synchronized so that no two concurrent requests end up creating two stocks of same
     * symbol.
     *
     * Please note, this would not work in a real distributed environment. For real distributed
     * environemnt, this has to be enforced at DAL layer.
     * @param stock to be created.
     * @return Stock created.
     */
    public  Stock create(final Stock stock) {
        Preconditions.checkNotNull(stock.getName(), "Name of company can not be null");
        Preconditions.checkNotNull(stock.getSymbol(), "Symbol of company can not be null");
        Preconditions.checkNotNull(stock.getPrice(), "Price of stock can not be null");

        boolean success = false;
        try {
            return stockRepository.save(stock);
        } catch (DataIntegrityViolationException exception) {
            if(exception.getCause() instanceof ConstraintViolationException) {
                final ConstraintViolationException c = (ConstraintViolationException)exception.getCause();
                final SQLException sqlException = (SQLException) c.getCause();
                int errorCode = sqlException.getErrorCode();

                if(errorCode == 23001) { // Unique index or primary key violation
                    throw new StockAlreadyExistsException(String.format("Stock with symbol:%s already exists.", stock.getSymbol()));
                }
            }
            throw exception;
        }
    }

    /**
     * Gets a {@link Stock} by its id.
     * @param id
     * @return
     */
    public Stock get(final Long id) {
        final Stock stock = stockRepository.findOne(id);
        final String message = String.format("No stock found with id %s", id);
        return Optional
                .of(stock)
                .orElseThrow(()-> new ResourceNotFoundException(message));
    }

    /**
     * Updates the price of a single {@link Stock}.
     * @param id
     * @param price
     */
    public void updatePrice(final Long id, final Double price, final Long version) {
        final Stock stock = stockRepository.findOne(id);

        final String message = String.format("No stock found with id %s", id);
        Optional.ofNullable(stock).orElseThrow(()-> new ResourceNotFoundException(message));

        stockRepository.updatePrice(id, price, version);
    }

    /**
     * Gets a {@link JSONObject} with information for the page.
     * {
     *      "page": 2,
     *      "items": <<get of stocks>>,
     *      "maxPage": 4
     * }
     * @param pageNumber
     * @return
     */
    public JSONObject get(final int pageNumber) {

        Page<Stock> page = stockRepository.findAll(new PageRequest(pageNumber -1, 5));

        final JSONObject obj = new JSONObject();

        obj.put("items", ImmutableList.copyOf(page.iterator()));
        obj.put("page", pageNumber);
        obj.put("maxPage", page.getTotalPages());
        return obj;
    }

    /**
     * Creates stocks in bulk. This has been created for testing purposes only.
     * @param stocks
     */
    public void bulkCreate(List<Stock> stocks) {
        stockRepository.save(stocks);
    }
}