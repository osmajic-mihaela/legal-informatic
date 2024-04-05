package org.example.legalinformaticbackend.repository;

import org.example.legalinformaticbackend.model.ApplicationUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends DbEntityRepository<ApplicationUser> {
    ApplicationUser getByEmail(String email);
}
