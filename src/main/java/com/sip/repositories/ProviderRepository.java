package com.sip.repositories;

import com.sip.entities.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.entities.Provider;

import java.util.List;

@Repository
public interface ProviderRepository extends CrudRepository<Provider,Long> {

    @Query("FROM Provider p WHERE p.name LIKE %?1% OR p.email LIKE %?1% or p.address LIKE %?1% or p.telephone LIKE %?1% ")
    List<Provider> findProviders(String mots);
}
