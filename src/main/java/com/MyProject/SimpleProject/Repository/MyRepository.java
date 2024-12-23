package com.MyProject.SimpleProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MyProject.SimpleProject.Registration.Registration;

public interface MyRepository extends JpaRepository<Registration, Integer>{
	Registration findByEmail(String email);

}
