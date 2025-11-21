package com.app.controller;
import com.app.model.Company;
import com.app.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @PostMapping
    public Mono<ResponseEntity<String>> createCompany(@RequestBody Company company) {
        return service.createCompany(company)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created.getId()))
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Company>> getCompanyById(@PathVariable String id) {
        return service.getCompanyById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                );
    }

    @GetMapping
    public Flux<Company> getAllCompanies() {
        return service.getAllCompanies();
    }

    @GetMapping("/sector/{sector}")
    public Flux<Company> getCompaniesBySector(@PathVariable String sector) {
        return service.getCompaniesBySector(sector);
    }

    @PatchMapping("/{id}/price")
    public Mono<ResponseEntity<Company>> updatePrice(
            @PathVariable String id,
            @RequestParam Double price
    ) {
        return service.updateCompanyPrice(id, price)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCompany(@PathVariable String id) {
        return service.deleteCompany(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
                );
    }
}
