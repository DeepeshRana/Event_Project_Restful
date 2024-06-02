package com.event.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.event.dto.StudentDto;
import com.event.entity.Student;
import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Integer> {

	Student findByEmail(String email);
	
}
