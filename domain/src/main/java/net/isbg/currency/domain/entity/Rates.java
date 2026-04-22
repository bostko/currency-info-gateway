package net.isbg.currency.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
public class Rates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long timestamp;

    private String base;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "rate_values", joinColumns = @JoinColumn(name = "rates_id"))
    @MapKeyColumn(name = "currency")
    @Column(name = "value")
    private Map<String, Double> rates;
}