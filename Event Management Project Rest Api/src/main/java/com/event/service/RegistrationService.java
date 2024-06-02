package com.event.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.event.dto.CustomPage;
import com.event.dto.EventDto;
import com.event.dto.RegistrationDto;
import com.event.dto.StudentDto;
import com.event.entity.Event;
import com.event.entity.Student;

public interface RegistrationService {
	RegistrationDto createRegistration(RegistrationDto regDto, Integer eventId, Integer stdId);

	// List<RegistrationDto> getAllRegistrations();
	CustomPage<RegistrationDto> getAllRegistrations(int page, int size);

	// List<RegistrationDto> getRegistrationByEvent(Integer eventId);

	// Page<RegistrationDto> getRegistrationByEvent(Integer eventId, int page, int
	// size);

	// List<RegistrationDto> getRegistrationByStudent(Integer stdId);

	// Page<RegistrationDto> getRegistrationByStudent(Integer stdId, int page, int
	// size);

	RegistrationDto getRegistrationByEventAndStudent(Integer eventId, Integer stdId);

	CustomPage<RegistrationDto> getRegistrationByStudent(Integer stdId, int page, int size);

	RegistrationDto getRegById(Integer regId);

	void deleteRegistration(Integer regId);

	void deleteRegistrationByEventAndStudentId(Integer eventId, Integer stdId);

	CustomPage<RegistrationDto> getRegistrationByEvent(Integer eventId, int page, int size);

}
