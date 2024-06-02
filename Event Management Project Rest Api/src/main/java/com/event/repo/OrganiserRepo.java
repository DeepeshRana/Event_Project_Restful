package com.event.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.event.entity.Organiser;
import com.event.entity.Student;

public interface OrganiserRepo extends JpaRepository<Organiser, Integer> {

	Organiser findByEmail(String email);

}
