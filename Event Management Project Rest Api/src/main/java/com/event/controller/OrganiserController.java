package com.event.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.dto.ApiResponse;
import com.event.dto.CustomPage;
import com.event.dto.OrganiserDto;
import com.event.dto.StudentDto;
import com.event.service.OrganiserService;
import com.event.validater.EmailUniqueValidator;
import com.event.validater.EmailValidaterForOrganizer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/organisers")
public class OrganiserController {

	@Autowired
	public OrganiserService orgService;

	@Autowired
	public EmailValidaterForOrganizer emailValidaterForOrganizer;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganiserController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		if (dataBinder.getTarget() != null && OrganiserDto.class.equals(dataBinder.getTarget().getClass())) {
			dataBinder.addValidators(emailValidaterForOrganizer);
		}
	}

	@PostMapping("/")
	public ResponseEntity<OrganiserDto> createOrganiser(@Valid @RequestBody OrganiserDto organiserDto) {
		LOGGER.info("Creating organiser: {}", organiserDto);

		OrganiserDto createOrganiser = this.orgService.createOrganiser(organiserDto);

		LOGGER.info("Organiser created successfully: {}", createOrganiser);

		return new ResponseEntity<>(createOrganiser, HttpStatus.CREATED);
	}

	@PutMapping("/{orgId}")
	public ResponseEntity<OrganiserDto> updateOrganiser(@Valid @RequestBody OrganiserDto organiserDto,
			@PathVariable Integer orgId) {
		LOGGER.info("Updating organiser with ID {}: {}", orgId, organiserDto);

		OrganiserDto updateOrganiser = this.orgService.updateOrganiser(organiserDto, orgId);
		LOGGER.info("Organiser updated successfully: {}", updateOrganiser);

		return new ResponseEntity<>(updateOrganiser, HttpStatus.OK);
	}

	@GetMapping("/{orgId}")
	public ResponseEntity<OrganiserDto> getOrganiser(@PathVariable Integer orgId) {
		LOGGER.info("Retrieving organiser with ID: {}", orgId);

		return ResponseEntity.ok(this.orgService.getOrganiserById(orgId));
	}

//	@GetMapping("/")
//	public ResponseEntity<List<OrganiserDto>> getAllOrganisers() {
//		return ResponseEntity.ok(this.orgService.getAllOrganisers());
//	}
	@GetMapping("/")
	public ResponseEntity<CustomPage<OrganiserDto>> getAllOrganisers(@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    CustomPage<OrganiserDto> organisersPage = orgService.getAllOrganisers(page, size);
	    return new ResponseEntity<>(organisersPage, HttpStatus.OK);
	}

	@DeleteMapping("/{orgId}")
	public ResponseEntity<ApiResponse> deleteOrganiser(@PathVariable Integer orgId) {
		LOGGER.info("Deleting organiser with ID: {}", orgId);

		this.orgService.deleteOrganiser(orgId);
		LOGGER.info("Organiser deleted successfully");

		return new ResponseEntity<ApiResponse>(new ApiResponse("Organiser Deleted Successfully", true), HttpStatus.OK);
	}
}
