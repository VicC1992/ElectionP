package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class UserRepositoryTest {
    @Autowired
    //este varianta simplificata a EntityManager folosita pentru a interactiona cu entiatile din baza de date
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repo;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("ravikumar@gmail.com");
        user.setPassword("ravi2020");
        user.setFirstName("Ravi");
        user.setLastName("Kumar");
        user.setDescription("about");

        User savedUser = repo.save(user);
        User existUser = entityManager.find(User.class, savedUser.getId());
        //metoda de mai jos este folosita pentru a verifica daca emailul utilizatorului creat e acelasi cu al utilizatorului recuperatdin baza de date
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());

    }

}