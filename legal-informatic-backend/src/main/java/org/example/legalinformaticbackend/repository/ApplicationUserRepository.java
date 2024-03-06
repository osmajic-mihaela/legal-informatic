package org.example.legalinformaticbackend.repository;

import org.example.legalinformaticbackend.model.ApplicationUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends DbEntityRepository<ApplicationUser> {

    @Query("SELECT u FROM ApplicationUser u WHERE u.email=:email")
    ApplicationUser getByEmail(@Param("email") String email);

}
