package com.event.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.dto.ApiResponse;
import com.event.dto.CustomPage;
import com.event.dto.EventDto;
import com.event.service.EventService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/events")
public class EventController {

	@Autowired
	private EventService eventService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

	@PostMapping("/organiser/{orgId}/eventadd")
	public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Integer orgId) {

		EventDto createdEvent = this.eventService.createEvent(eventDto, orgId);
		LOGGER.info("Event created successfully: {}", createdEvent);

		return new ResponseEntity<EventDto>(createdEvent, HttpStatus.CREATED);
	}

	@PutMapping("/{eventId}")
	public ResponseEntity<EventDto> updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Integer eventId) {
		LOGGER.info("Received request to update event with ID: {}", eventId);
		try {
			EventDto updatedEvent = this.eventService.updateEvent(eventDto, eventId);
			LOGGER.info("Event updated successfully: {}", updatedEvent);
			return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
		} catch (Exception ex) {
			LOGGER.error("Failed to update event with ID: {}", eventId, ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{eventId}")
	public ResponseEntity<EventDto> getEventById(@PathVariable Integer eventId) {
		LOGGER.info("Received request to retrieve event with ID: {}", eventId);
		try {
			EventDto eventDto = this.eventService.getEventById(eventId);
			LOGGER.info("Event retrieved successfully: {}", eventDto);
			return new ResponseEntity<>(eventDto, HttpStatus.OK);
		} catch (Exception ex) {
			LOGGER.error("Failed to retrieve event with ID: {}", eventId, ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@GetMapping("/")
//	public ResponseEntity<List<EventDto>> getAllEvents() {
//		List<EventDto> events = this.eventService.getAllEvents();
//		return new ResponseEntity<>(events, HttpStatus.OK);
//	}

	@GetMapping("/")
	public ResponseEntity<CustomPage<EventDto>> getAllEvents(@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    CustomPage<EventDto> eventsPage = eventService.getAllEvents(page, size);
	    return new ResponseEntity<>(eventsPage, HttpStatus.OK);
	}

	@DeleteMapping("/{eventId}")
	public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Integer eventId) {
		LOGGER.info("Received request to delete event with ID: {}", eventId);
		try {
			this.eventService.deleteEvent(eventId);
			LOGGER.info("Event deleted successfully");
			return new ResponseEntity<>(new ApiResponse("Event Deleted Successfully", true), HttpStatus.OK);
		} catch (Exception ex) {
			LOGGER.error("Failed to delete event with ID: {}", eventId, ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/byname")
	public ResponseEntity<List<EventDto>> getEventsByName(@RequestParam String eventName) {
		LOGGER.info("Received request to retrieve events by name: {}", eventName);
		try {
			List<EventDto> events = eventService.findByName(eventName);
			LOGGER.info("Retrieved {} events with name: {}", events.size(), eventName);
			return ResponseEntity.ok(events);
		} catch (Exception ex) {
			LOGGER.error("Failed to retrieve events by name: {}", eventName, ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

