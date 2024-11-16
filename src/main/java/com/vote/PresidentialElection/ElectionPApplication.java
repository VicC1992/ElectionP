package com.vote.PresidentialElection;

import com.vote.PresidentialElection.entity.Role;
import com.vote.PresidentialElection.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class ElectionPApplication {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ElectionPApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeRoles() {
		return args -> {
			initializeRole("ADMIN");
			initializeRole("USER");
		};
	}

	private void initializeRole(String roleName) {
		Optional<Role> roleOpt = roleRepository.findByName(roleName);
		if (roleOpt.isEmpty()) {
			Role role = new Role();
			role.setName(roleName);
			roleRepository.save(role);
		}
	}

}
