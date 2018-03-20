package com.stocks.api.dal;

import com.stocks.api.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    /**
     * Updates price of a stock based on version provided by the client. Version of price is maintained by client.
     * Update will succeed only if version is greater than current version in database.
     *
     * This is useful in scenario when client is handling requests in asynchronous fashion and does not guarantee arrival
     * of requests in ordered manner. Even if requests arrive in ordered manner, in a distributed system, two
     * order of execution of request is not guaranteed.
     *
     * Please note that, this is not going to return an error if version happens to be lower. As there would be no row to
     * update, so SQL will not produce an error. This is not bad, since we know there is another version updated and
     * data is more fresh than ongoing request.
     *
     * @param id @{@link Long} id of the stock
     * @param price {@link Double} price of stock
     * @param version {@link Long} version of the price.
     */
    @Transactional
    @Modifying
    @Query("UPDATE Stock s SET s.price= :price, s.version= :version, s.lastUpdateDate= CURRENT_TIMESTAMP WHERE s.id= :id and s.version < :version")
    void updatePrice(@Param("id") Long id, @Param("price") Double price, @Param("version") Long version);
}
