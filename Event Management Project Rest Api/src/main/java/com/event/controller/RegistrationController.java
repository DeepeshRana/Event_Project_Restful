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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.dto.ApiResponse;
import com.event.dto.CustomPage;
import com.event.dto.EventDto;
import com.event.dto.RegistrationDto;
import com.event.dto.StudentDto;
import com.event.entity.Student;
import com.event.service.RegistrationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	private RegistrationService regService;

	@GetMapping("/event/{eventId}/student/{stdId}")
	public ResponseEntity<RegistrationDto> getRegistrationByEventAndStudent(@PathVariable Integer eventId,
			@PathVariable Integer stdId) {
		try {
			LOGGER.info("Retrieving registration for event ID {} and student ID {}", eventId, stdId);

			RegistrationDto getReg = this.regService.getRegistrationByEventAndStudent(eventId, stdId);
			LOGGER.info("Retrieved registration: {}", getReg);

			System.out.println(getReg);
			return new ResponseEntity<RegistrationDto>(getReg, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error retrieving registration: {}", e.getMessage());

			System.out.println(e);
			return new ResponseEntity<RegistrationDto>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PostMapping("/event/{eventId}/student/{stdId}")
	public ResponseEntity<RegistrationDto> createRegistration(@Valid @RequestBody RegistrationDto regDto,
			@PathVariable Integer eventId, @PathVariable Integer stdId) {
		LOGGER.info("Creating registration for event ID {} and student ID {}", eventId, stdId);

		RegistrationDto createdReg = this.regService.createRegistration(regDto, eventId, stdId);
		LOGGER.info("Registration created successfully: {}", createdReg);

		return new ResponseEntity<RegistrationDto>(createdReg, HttpStatus.CREATED);
	}

//	@GetMapping("/")
//	public ResponseEntity<List<RegistrationDto>> getAllRegistrations() {
//		List<RegistrationDto> registrations = this.regService.getAllRegistrations();
//		return new ResponseEntity<>(registrations, HttpStatus.OK);
//
//	}

	@GetMapping("/{regId}")
	public ResponseEntity<RegistrationDto> getRegById(@PathVariable Integer regId) {
		RegistrationDto regDto = this.regService.getRegById(regId);
		return new ResponseEntity<>(regDto, HttpStatus.OK);
	}

//	@GetMapping("/event/{eventId}")
//	public ResponseEntity<List<RegistrationDto>> getRegByEvent(@PathVariable Integer eventId) {
//		List<RegistrationDto> regByEvent = this.regService.getRegistrationByEvent(eventId);
//		return new ResponseEntity<>(regByEvent, HttpStatus.OK);
//	}

//	@GetMapping("/student/{stdId}")
//	public ResponseEntity<List<RegistrationDto>> getRegByStudent(@PathVariable Integer stdId) {
//		List<RegistrationDto> regByStudent = this.regService.getRegistrationByStudent(stdId);
//		return new ResponseEntity<>(regByStudent, HttpStatus.OK);
//	}

	@DeleteMapping("/{regId}")
	public ResponseEntity<ApiResponse> deleteRegistration(@PathVariable Integer regId) {
		LOGGER.info("Deleting registration with ID: {}", regId);

		this.regService.deleteRegistration(regId);
		LOGGER.info("Registration deleted successfully");

		return new ResponseEntity<ApiResponse>(new ApiResponse("Registration Deleted Successfully", true),
				HttpStatus.OK);
	}

	@DeleteMapping("/event/{eventId}/student/{stdId}")
	public ResponseEntity<ApiResponse> deleteRegistrationByEventAndStudent(@PathVariable Integer eventId,
			@PathVariable Integer stdId) {
		LOGGER.info("Deleting registration with event ID {} and student ID {}", eventId, stdId);

		this.regService.deleteRegistrationByEventAndStudentId(eventId, stdId);
		LOGGER.info("Registration deleted successfully with Event ID {} and student ID {}", eventId, stdId);

		return new ResponseEntity<ApiResponse>(new ApiResponse(
				"Registration Delete Successfully with Event id " + eventId + " and studentId " + stdId + "", true),
				HttpStatus.OK);
	}

//	@GetMapping("/")
//	public ResponseEntity<Page<RegistrationDto>> getAllRegistrations(@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "10") int size) {
//
//		Page<RegistrationDto> registrationsPage = regService.getAllRegistrations(page, size);
//		return new ResponseEntity<>(registrationsPage, HttpStatus.OK);
//	}
	@GetMapping("/")
	public ResponseEntity<CustomPage<RegistrationDto>> getAllRegistrations(@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    CustomPage<RegistrationDto> registrationsPage = regService.getAllRegistrations(page, size);
	    return new ResponseEntity<>(registrationsPage, HttpStatus.OK);
	}

//	@GetMapping("/event/{eventId}")
//	public ResponseEntity<Page<RegistrationDto>> getRegByEvent(@PathVariable Integer eventId,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
//		LOGGER.info("Retrieving registrations for event with ID {} (page={}, size={})", eventId, page, size);
//
//		Page<RegistrationDto> regByEventPage = regService.getRegistrationByEvent(eventId, page, size);
//		LOGGER.info("Retrieved {} registrations for event with ID {}", regByEventPage.getNumberOfElements(), eventId);
//
//		return new ResponseEntity<>(regByEventPage, HttpStatus.OK);
//	}

	@GetMapping("/student/{stdId}")
	public ResponseEntity<CustomPage<RegistrationDto>> getRegByStudent(@PathVariable Integer stdId,
	        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
	    LOGGER.info("Retrieving registrations for student with ID {} (page={}, size={})", stdId, page, size);

	    CustomPage<RegistrationDto> regByStudentPage = regService.getRegistrationByStudent(stdId, page, size);
	    LOGGER.info("Retrieved {} registrations for student with ID {}", regByStudentPage.getContent().size(), stdId);

	    return new ResponseEntity<>(regByStudentPage, HttpStatus.OK);
	}
}
