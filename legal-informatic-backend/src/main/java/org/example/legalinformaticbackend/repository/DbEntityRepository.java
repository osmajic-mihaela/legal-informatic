package org.example.legalinformaticbackend.repository;

import org.example.legalinformaticbackend.model.DbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DbEntityRepository<T extends DbEntity> extends JpaRepository<T, Long> {
    <S extends T> S save(S entity);

    Optional<T> findById(Long id);
    List<T> findAll();

    void deleteById(Long id);
}
