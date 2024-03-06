package org.example.legalinformaticbackend.repository;


import org.example.legalinformaticbackend.model.DbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface DbEntityRepository<T extends DbEntity> extends JpaRepository<T, Long> {

    default T get(Long id) {
        Optional<T> optionalEntity = findById(id);
        return optionalEntity.orElse(null);
    }

    default boolean exists(Long id) {
        return get(id) != null;
    }

}
