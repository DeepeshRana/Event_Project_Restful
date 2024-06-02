package com.event.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.event.entity.Event;

public interface EventRepo extends JpaRepository<Event, Integer> {

	List<Event> findByTitle(String eventName);

}
