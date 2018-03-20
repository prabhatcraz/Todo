package com.stocks.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;


/**
 * A model to represent a Stock object.
 */
@Wither
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@Table(name = "stocks")
@DynamicUpdate
public class Stock {
    /**
     * Uniquely Identifies id of a stock
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "symbol", unique = true, nullable = false)
    private String symbol;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name = "lastUpdated", nullable = false)
    private Date lastUpdateDate;

    @CreatedDate
    @Column(name="creationDate", nullable = false, updatable=false)
    @Temporal(TIMESTAMP)
    private Date creationDate;

    @Column(name = "version", nullable = false)
    private Long version;

}