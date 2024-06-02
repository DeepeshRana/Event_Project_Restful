package com.event.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.event.dto.CustomPage;
import com.event.dto.StudentDto;

public interface StudentService {
	StudentDto createStudent(StudentDto studentDto);

	StudentDto updateStudent(StudentDto studentDto, Integer stdId);

	// List<StudentDto> getAllStudents();

	CustomPage<StudentDto> getAllStudents(int page, int size);
	
    //CustomPage<StudentDto> getAllStudents(int page, int size);


	StudentDto getStudentById(Integer stdId);

	void deleteStudent(Integer stdId);
}
