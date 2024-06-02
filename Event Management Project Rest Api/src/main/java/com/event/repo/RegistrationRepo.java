package com.event.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.event.entity.Event;
import com.event.entity.Registration;
import com.event.entity.Student;

public interface RegistrationRepo extends JpaRepository<Registration, Integer> {
	List<Registration> findByEvent(Event event);

	List<Registration> findByStudent(Student student);

	Registration findByEventAndStudent(Event eventId, Student stdId);

	Page<Registration> findByEvent(Event event, Pageable pageable);

	Page<Registration> findByStudent(Student std, Pageable pageable);

	int countByEvent(Event event);
}
