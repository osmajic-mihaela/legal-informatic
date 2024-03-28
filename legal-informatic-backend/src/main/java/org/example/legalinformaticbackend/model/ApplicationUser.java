package org.example.legalinformaticbackend.model;


import lombok.*;

import jakarta.persistence.*;

@Entity(name="ApplicationUser")
@Table(name = "application_user")
@Getter
@Setter
public class ApplicationUser extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
