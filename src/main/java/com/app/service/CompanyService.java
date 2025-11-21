package com.app.service;

import com.app.model.Company;
import com.app.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    public Mono<Company> createCompany(Company company) {
        return Mono.just(company)
                .map(c -> {
                    c.setCreatedAt(LocalDateTime.now());
                    c.setUpdatedAt(LocalDateTime.now());
                    return c;
                })
                .flatMap(repository::save);
    }

    public Mono<Company> getCompanyById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Company not found with ID: " + id)));
    }

    public Flux<Company> getAllCompanies() {
        return repository.findAll();
    }

    public Flux<Company> getCompaniesBySector(String sector) {
        return repository.findBySector(sector);
    }

    public Mono<Company> updateCompanyPrice(String id, Double newPrice) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Company not found with ID: " + id)))
                .map(company -> {
                    company.setCurrentPrice(newPrice);
                    company.setUpdatedAt(LocalDateTime.now());
                    return company;
                })
                .flatMap(repository::save);
    }

    public Mono<Void> deleteCompany(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Company not found with ID: " + id)))
                .flatMap(company -> repository.deleteById(id));
    }
}
