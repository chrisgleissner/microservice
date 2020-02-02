package com.github.chrisgleissner.sandbox.springbootmicroservice.repository;

import com.github.chrisgleissner.sandbox.springbootmicroservice.entity.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Exposes entity via REST without the need for a controller
 */
@RepositoryRestResource(collectionResourceRel = "company", path = "company")
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
    @Query("select c from Company c where lower(c.sector) = lower(?1)")
    List<Company> findBySector(Company.Sector sector);
}
