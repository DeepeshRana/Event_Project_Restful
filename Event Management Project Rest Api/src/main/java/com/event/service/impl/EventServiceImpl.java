package com.event.service.impl;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.event.dto.CustomPage;
import com.event.dto.EventDto;
import com.event.entity.Event;
import com.event.entity.Organiser;
import com.event.exception.ResourceNotFoundException;
import com.event.repo.EventRepo;
import com.event.repo.OrganiserRepo;
import com.event.service.EventService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepo eventRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OrganiserRepo orgRepo;

	public EventDto createEvent(EventDto eventDto, Integer orgId) {
		
		
		// Check if Organiser exists
		Organiser org = this.orgRepo.findById(orgId)
				.orElseThrow(() -> new ResourceNotFoundException("Organiser", "orgId", orgId));

		try {
			// Create a new Event entity and populate its fields
			Event event = new Event();
			event.setTitle(eventDto.getTitle());
			event.setContent(eventDto.getContent());
			event.setLocalDateTime(eventDto.getLocalDateTime());
			event.setMaxAttendees(eventDto.getMaxAttendes());
			event.setOrganiser(org);

			// Save the new Event entity to the database
			Event newEvent = this.eventRepo.save(event);
			LOGGER.info("Event created successfully: {}", newEvent);

			// Map the saved Event entity back to EventDto and return it
			return this.modelMapper.map(newEvent, EventDto.class);

		} catch (Exception ex) {
			// Log the exception or handle it appropriately
			ex.printStackTrace();
			LOGGER.error("Failed to create event: {}", ex.getMessage(), ex);

			throw new RuntimeException("Failed to create event. Please try again.");
		}
	}

	@Override
	public EventDto updateEvent(EventDto eventDto, Integer eventId) {
		LOGGER.info("Updating event with ID: {}", eventId);

		Event event = this.eventRepo.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
		event.setTitle(eventDto.getTitle());
		event.setContent(eventDto.getContent());
		event.setLocalDateTime(eventDto.getLocalDateTime());
		event.setMaxAttendees(eventDto.getMaxAttendes());
		Event updatedEvent = this.eventRepo.save(event);
		LOGGER.info("Event updated successfully: {}", updatedEvent);

		return this.modelMapper.map(updatedEvent, EventDto.class);
	}

//	@Override
//	public List<EventDto> getAllEvents() {
//	
//		List<Event> events = this.eventRepo.findAll();
//		List<EventDto> eventDtos = events.stream().map((event) -> this.modelMapper.map(event, EventDto.class))
//				.collect(Collectors.toList());
//		return eventDtos;
//	}

	public CustomPage<EventDto> getAllEvents(int page, int size) {
	    LOGGER.info("Retrieving all events (page={}, size={})", page, size);

	    PageRequest pageable = PageRequest.of(page, size);
	    Page<Event> eventsPage = eventRepo.findAll(pageable);
	    LOGGER.info("Retrieved {} events", eventsPage.getNumberOfElements());

	    List<EventDto> eventDtos = eventsPage.getContent().stream()
	            .map(event -> modelMapper.map(event, EventDto.class))
	            .collect(Collectors.toList());

	    return new CustomPage<>(
	            eventDtos,
	            eventsPage.getTotalPages(),
	            eventsPage.getTotalElements(),
	            eventsPage.getNumber(),
	            eventsPage.getSize(),
	            eventsPage.hasNext(),
	            eventsPage.hasPrevious()
	    );
	}

	@Override
	public EventDto getEventById(Integer eventId) {
		LOGGER.info("Retrieving event with ID: {}", eventId);

		Event event = this.eventRepo.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
		return this.modelMapper.map(event, EventDto.class);
	}

	@Override
	public void deleteEvent(Integer eventId) {
		this.eventRepo.delete(this.eventRepo.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId)));
	}

	// ____________________

	public List<EventDto> findByName(String eventName) {
		LOGGER.info("Retrieving events by name: {}", eventName);

		List<Event> events = eventRepo.findByTitle(eventName);
		LOGGER.info("Retrieved {} events with name: {}", events.size(), eventName);

		return events.stream().map(event -> modelMapper.map(event, EventDto.class)).collect(Collectors.toList());
	}

}
