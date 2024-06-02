package com.event.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.event.dto.CustomPage;
import com.event.dto.EventDto;

public interface EventService {
	EventDto createEvent(EventDto eventDto, Integer orgId);

	EventDto updateEvent(EventDto eventDto, Integer eventId);

	// List<EventDto> getAllEvents();

	CustomPage<EventDto> getAllEvents(int page, int size);

	EventDto getEventById(Integer eventId);

	void deleteEvent(Integer eventId);

	List<EventDto> findByName(String eventName);

}
