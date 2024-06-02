package com.event.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.event.dto.CustomPage;
import com.event.dto.StudentDto;
import com.event.entity.Student;
import com.event.exception.InvalidEmailUpdatation;
import com.event.exception.ResourceNotFoundException;
import com.event.repo.StudentRepo;
import com.event.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo studentRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public StudentDto createStudent(StudentDto studentDto) {
		LOGGER.info("Creating student: {}", studentDto);

		Student student = this.modelMapper.map(studentDto, Student.class);
		Student savedStudent = this.studentRepo.save(student);
		LOGGER.info("Student created successfully: {}", savedStudent);

		return this.modelMapper.map(savedStudent, StudentDto.class);
	}

	@Override
	public StudentDto updateStudent(StudentDto studentDto, Integer stdId) {
		LOGGER.info("Updating student with ID {}: {}", stdId, studentDto);

		Student student = this.studentRepo.findById(stdId)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId));
		student.setFirstName(studentDto.getFirstName());
		student.setLastName(studentDto.getLastName());
		student.setEmail(studentDto.getEmail());
		Student updateStudent = this.studentRepo.save(student);
		LOGGER.info("Student updated successfully: {}", updateStudent);

		return this.modelMapper.map(updateStudent, StudentDto.class);
	}

//	@Override
//	public List<StudentDto> getAllStudents() {
//		List<Student> students = this.studentRepo.findAll();
//		List<StudentDto> studentDtos = students.stream().map((std) -> this.modelMapper.map(std, StudentDto.class))
//				.collect(Collectors.toList());
//		return studentDtos;
//	}

	@Override
	public StudentDto getStudentById(Integer stdId) {
		LOGGER.info("Retrieving student with ID: {}", stdId);

		Student student = this.studentRepo.findById(stdId)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "StdId", stdId));
		LOGGER.info("Retrieved student: {}", student);

		return this.modelMapper.map(student, StudentDto.class);
	}

	@Override
	public void deleteStudent(Integer stdId) {
		LOGGER.info("Deleting student with ID: {}", stdId);

		this.studentRepo.delete(this.studentRepo.findById(stdId)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "stdId", stdId)));

		LOGGER.info("Student deleted successfully");

	}

//	@Override
//	public Page<StudentDto> getAllStudents(int page, int size) {
//
//		LOGGER.info("Retrieving all students (page={}, size={})", page, size);
//
//		// TODO Auto-generated method stub
//		PageRequest pageable = PageRequest.of(page, size);
//		Page<Student> studentsPage = studentRepo.findAll(pageable);
//
//		LOGGER.info("Retrieved {} students", studentsPage.getNumberOfElements());
//
//		return studentsPage.map(std -> modelMapper.map(std, StudentDto.class));
//	}
//	

	public CustomPage<StudentDto> getAllStudents(int page, int size) {
	    LOGGER.info("Retrieving all students (page={}, size={})", page, size);
	    PageRequest pageable = PageRequest.of(page, size);
	    Page<Student> studentsPage = studentRepo.findAll(pageable);

	    LOGGER.info("Retrieved {} students", studentsPage.getNumberOfElements());

	    List<StudentDto> studentDtos = studentsPage.getContent().stream()
	            .map(std -> modelMapper.map(std, StudentDto.class))
	            .collect(Collectors.toList());

	    return new CustomPage<>(
	            studentDtos,
	            studentsPage.getTotalPages(),
	            studentsPage.getTotalElements(),
	            studentsPage.getNumber(),
	            studentsPage.getSize(),
	            studentsPage.hasNext(),
	            studentsPage.hasPrevious()
	    );
	}



}
