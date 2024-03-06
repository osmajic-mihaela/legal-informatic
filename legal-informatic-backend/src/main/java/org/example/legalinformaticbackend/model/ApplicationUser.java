package org.example.legalinformaticbackend.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.legalinformaticbackend.repository.DbEntityRepository;

@Getter
@Setter
@Entity
public class ApplicationUser extends DbEntity {

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

}
