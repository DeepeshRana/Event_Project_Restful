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
import com.event.dto.StudentDto;
import com.event.service.StudentService;
import com.event.validater.EmailUniqueValidator;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentController {

	@Autowired
	public StudentService stdService;

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	public EmailUniqueValidator emailUniqueValidator;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		if (dataBinder.getTarget() != null && StudentDto.class.equals(dataBinder.getTarget().getClass())) {
			dataBinder.addValidators(emailUniqueValidator);
		}
	}

	@PostMapping("/")
	public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
		LOGGER.info("Creating student: {}", studentDto);

		StudentDto createStudent = this.stdService.createStudent(studentDto);

		return new ResponseEntity<>(createStudent, HttpStatus.CREATED);
	}

	@PutMapping("/{stdId}")
	public ResponseEntity<StudentDto> updateStudent(@Valid @RequestBody StudentDto studentDto,
			@PathVariable Integer stdId) {
		StudentDto updateStudent = this.stdService.updateStudent(studentDto, stdId);

		return new ResponseEntity<>(updateStudent, HttpStatus.OK);
	}

	@GetMapping("/{stdId}")
	public ResponseEntity<StudentDto> getStudent(@PathVariable Integer stdId) {
		return ResponseEntity.ok(this.stdService.getStudentById(stdId));
	}

//	@GetMapping("/")
//	public ResponseEntity<List<StudentDto>> getAllStudents() {
//		return ResponseEntity.ok(this.stdService.getAllStudents());
//	}

//	@GetMapping("/")
//	public ResponseEntity<Page<StudentDto>> getAllStudents(@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "5") int size) {
//
//		Page<StudentDto> studentsPage = stdService.getAllStudents(page, size);
//		return new ResponseEntity<>(studentsPage, HttpStatus.OK);
//	}
	
	@GetMapping("/")
	public ResponseEntity<CustomPage<StudentDto>> getAllStudents(@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    CustomPage<StudentDto> studentsPage = stdService.getAllStudents(page, size);
	    return new ResponseEntity<>(studentsPage, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	

	@DeleteMapping("/{stdId}")
	public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Integer stdId) {
		LOGGER.info("Deleting student with ID: {}", stdId);

		this.stdService.deleteStudent(stdId);
		LOGGER.info("Student deleted successfully");

		return new ResponseEntity<ApiResponse>(new ApiResponse("Student Deleted Successfully", true), HttpStatus.OK);
	}
}
