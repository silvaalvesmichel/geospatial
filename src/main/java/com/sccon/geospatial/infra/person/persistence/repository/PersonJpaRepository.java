package com.sccon.geospatial.infra.person.persistence.repository;

import com.sccon.geospatial.infra.person.persistence.entity.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonJpaRepository extends JpaRepository<PessoaEntity, Long> {

    @Query("SELECT MAX(p.id) FROM PessoaEntity p")
    Long findMaxId();
}
