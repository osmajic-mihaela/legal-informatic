package org.example.legalinformaticbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.dto.ApplicationUserDTO;
import org.example.legalinformaticbackend.dto.LoginDTO;
import org.example.legalinformaticbackend.model.ApplicationUser;
import org.example.legalinformaticbackend.repository.ApplicationUserRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository userRepository;

    public ApplicationUser register(ApplicationUserDTO newUser) {
        if ((this.getByEmail(newUser.email) == null)) {
            ApplicationUser user = createUser(newUser);
            userRepository.save(user);
            return user;
        } else return null;
    }

    public ApplicationUser getByEmail(String email) {
        if (email.isEmpty())
            return null;
        return userRepository.getByEmail(email.toLowerCase().replaceAll("\\s+", ""));
    }

    private ApplicationUser createUser(ApplicationUserDTO newUser){
        ApplicationUser user = new ApplicationUser();
        user.setFirstName(newUser.firstName);
        user.setLastName(newUser.lastName);
        user.setEmail(newUser.email.toLowerCase());
        user.setPassword(newUser.password);

        return user;

    }


    public ApplicationUser login(LoginDTO user) {
        ApplicationUser loggedUser = this.getByEmail(user.email);
        if (loggedUser != null && loggedUser.getPassword() == user.getPassword()) {
            return loggedUser;
        } else return null;
    }


}
