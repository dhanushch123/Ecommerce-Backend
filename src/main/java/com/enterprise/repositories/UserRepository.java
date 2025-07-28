package com.enterprise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.models.Users;

public interface UserRepository extends JpaRepository<Users,Integer>{

	Users findByUsername(String username);

	Users findByEmail(String email);

}
