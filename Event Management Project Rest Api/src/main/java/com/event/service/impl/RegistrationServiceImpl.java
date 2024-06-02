package com.event.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.event.dto.CustomPage;
import com.event.dto.EventDto;
import com.event.dto.RegistrationDto;
import com.event.entity.Event;
import com.event.entity.Registration;
import com.event.entity.Student;
import com.event.exception.ResourceNotFoundException;
import com.event.repo.EventRepo;
import com.event.repo.RegistrationRepo;
import com.event.repo.StudentRepo;
import com.event.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private RegistrationRepo regRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EventRepo eventRepo;

	@Autowired
	private StudentRepo stdRepo;

	private RegistrationDto registrationDto;

	public RegistrationDto createRegistration(RegistrationDto regDto, Integer eventId, Integer stdId) {
		Event event = this.eventRepo.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
		Student student = this.stdRepo.findById(stdId)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));

		// Check if the event is full
		if (eventIsFull(event)) {
			throw new RuntimeException("Event is full, cannot register.");
		}

		// Check if the student is already registered for any event
		List<Registration> registrations = regRepo.findByStudent(student);
		if (!registrations.isEmpty()) {
			throw new RuntimeException("Student is already registered for an event");
		}
		if (eventIsFull(event)) {
	        throw new RuntimeException("Event is full, cannot register.");
	    }

		// Proceed with registration
		Registration reg = this.modelMapper.map(regDto, Registration.class);
		reg.setName(reg.getName());
		reg.setEvent(event);
		reg.setStudent(student);

		Registration newReg = this.regRepo.save(reg);
		return this.modelMapper.map(newReg, RegistrationDto.class);
	}

	private boolean eventIsFull(Event event) {
		// Retrieve the number of registrations for the event
		int numberOfRegistrations = regRepo.countByEvent(event);
		// Check if the number of registrations is equal to or exceeds maxAttendes
		return numberOfRegistrations >= event.getMaxAttendees();
	}

//	@Override
//	public List<RegistrationDto> getAllRegistrations() {
//		List<Registration> regs = this.regRepo.findAll();
//		List<RegistrationDto> regDtos = regs.stream()
//				.map((registration) -> this.modelMapper.map(registration, RegistrationDto.class))
//				.collect(Collectors.toList());
//		return regDtos;
//	}

	@Override
	public RegistrationDto getRegById(Integer regId) {
		Registration reg = this.regRepo.findById(regId)
				.orElseThrow(() -> new ResourceNotFoundException("Registration", "regId", regId));
		return this.modelMapper.map(reg, RegistrationDto.class);
	}

	@Override
	public void deleteRegistration(Integer regId) {
		this.regRepo.delete(this.regRepo.findById(regId)
				.orElseThrow(() -> new ResourceNotFoundException("Registration", "regId", regId)));
	}

	@Override
	public void deleteRegistrationByEventAndStudentId(Integer eventId, Integer stdId) {
		Event event = this.eventRepo.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
		Student student = this.stdRepo.findById(stdId)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));
		this.regRepo.delete(this.regRepo.findByEventAndStudent(event, student));
	}

//	@Override
//	public List<RegistrationDto> getRegistrationByEvent(Integer eventId) {
//		Event event = this.eventRepo.findById(eventId)
//				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
//		List<Registration> regs = this.regRepo.findByEvent(event);
//		List<RegistrationDto> regDtos = regs.stream().map((reg) -> this.modelMapper.map(reg, RegistrationDto.class))
//				.collect(Collectors.toList());
//		return regDtos;
//	}

//	@Override
//	public List<RegistrationDto> getRegistrationByStudent(Integer stdId) {
//		Student std = this.stdRepo.findById(stdId)
//				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));
//		List<Registration> regs = this.regRepo.findByStudent(std);
//		List<RegistrationDto> regDtos = regs.stream().map((reg) -> this.modelMapper.map(reg, RegistrationDto.class))
//				.collect(Collectors.toList());
//		return regDtos;
//	}

	@Override
	public RegistrationDto getRegistrationByEventAndStudent(Integer eventId, Integer stdId) {
		Event event = this.eventRepo.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));
		Student student = this.stdRepo.findById(stdId)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));

		Registration reg = this.regRepo.findByEventAndStudent(event, student);
		System.out.println(reg);
		return this.modelMapper.map(reg, RegistrationDto.class);
	}

//	@Override
//	public Page<RegistrationDto> getAllRegistrations(int page, int size) {
//		PageRequest pageable = PageRequest.of(page, size);
//		Page<Registration> registrationsPage = regRepo.findAll(pageable);
//		return registrationsPage.map(reg -> modelMapper.map(reg, RegistrationDto.class));
//	}

	public CustomPage<RegistrationDto> getAllRegistrations(int page, int size) {
	    PageRequest pageable = PageRequest.of(page, size);
	    Page<Registration> registrationsPage = regRepo.findAll(pageable);
	    
	    List<RegistrationDto> registrationDtos = registrationsPage.getContent().stream()
	            .map(reg -> modelMapper.map(reg, RegistrationDto.class))
	            .collect(Collectors.toList());

	    return new CustomPage<>(
	            registrationDtos,
	            registrationsPage.getTotalPages(),
	            registrationsPage.getTotalElements(),
	            registrationsPage.getNumber(),
	            registrationsPage.getSize(),
	            registrationsPage.hasNext(),
	            registrationsPage.hasPrevious()
	    );
	}
	
//	@Override
//	public Page<RegistrationDto> getRegistrationByStudent(Integer stdId, int page, int size) {
//		// TODO Auto-generated method stub
//		Student std = stdRepo.findById(stdId)
//				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));
//
//		Pageable pageable = PageRequest.of(page, size);
//		Page<Registration> registrationsPage = regRepo.findByStudent(std, pageable);
//		return registrationsPage.map(reg -> modelMapper.map(reg, RegistrationDto.class));
//	}
	
	@Override
	public CustomPage<RegistrationDto> getRegistrationByStudent(Integer stdId, int page, int size) {
	    Student std = stdRepo.findById(stdId)
	            .orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));

	    Pageable pageable = PageRequest.of(page, size);
	    Page<Registration> registrationsPage = regRepo.findByStudent(std, pageable);
	    
	    List<RegistrationDto> registrationDtos = registrationsPage.getContent().stream()
	            .map(reg -> modelMapper.map(reg, RegistrationDto.class))
	            .collect(Collectors.toList());

	    return new CustomPage<>(
	            registrationDtos,
	            registrationsPage.getTotalPages(),
	            registrationsPage.getTotalElements(),
	            registrationsPage.getNumber(),
	            registrationsPage.getSize(),
	            registrationsPage.hasNext(),
	            registrationsPage.hasPrevious()
	    );
	}

	@Override
	public CustomPage<RegistrationDto> getRegistrationByEvent(Integer eventId, int page, int size) {
	    Event event = eventRepo.findById(eventId)
	            .orElseThrow(() -> new ResourceNotFoundException("Event", "eventId", eventId));

	    Pageable pageable = PageRequest.of(page, size);
	    Page<Registration> registrationsPage = regRepo.findByEvent(event, pageable);
	    
	    List<RegistrationDto> registrationDtos = registrationsPage.getContent().stream()
	            .map(reg -> modelMapper.map(reg, RegistrationDto.class))
	            .collect(Collectors.toList());

	    return new CustomPage<>(
	            registrationDtos,
	            registrationsPage.getTotalPages(),
	            registrationsPage.getTotalElements(),
	            registrationsPage.getNumber(),
	            registrationsPage.getSize(),
	            registrationsPage.hasNext(),
	            registrationsPage.hasPrevious()
	    );
	}


}
