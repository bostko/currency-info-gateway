package net.isbg.currency.domain.repository;

import net.isbg.currency.domain.entity.Rates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RatesRepository extends JpaRepository<Rates, Long> {

    Optional<Rates> findTopByOrderByTimestampDesc();
}
