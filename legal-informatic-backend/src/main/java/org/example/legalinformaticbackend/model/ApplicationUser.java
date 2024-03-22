package org.example.legalinformaticbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;

@Entity
@Table(name = "application_user")
@Data
@EqualsAndHashCode(callSuper = true)
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
