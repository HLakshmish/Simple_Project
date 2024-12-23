package com.MyProject.SimpleProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MyProject.SimpleProject.Registration.loginpage;

public interface loginRepository extends JpaRepository<loginpage, Long> {
	loginpage findByUsername(String Username);
	
}
