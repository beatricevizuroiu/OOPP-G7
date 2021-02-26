package nl.tudelft.oopp.g7.server.repositories;

import nl.tudelft.oopp.g7.server.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {}
