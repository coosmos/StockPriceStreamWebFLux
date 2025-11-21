package com.app.repository;

import com.app.model.Company;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompanyRepository extends ReactiveMongoRepository<Company, String> {
    Mono<Company> findBySymbol(String symbol);
    Flux<Company> findBySector(String sector);
}
