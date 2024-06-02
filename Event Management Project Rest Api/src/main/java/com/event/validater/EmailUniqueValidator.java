package com.event.validater;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OperatorNot;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.event.dto.OrganiserDto;
import com.event.dto.StudentDto;
import com.event.entity.Organiser;
import com.event.entity.Student;
import com.event.repo.OrganiserRepo;
import com.event.repo.StudentRepo;

import jakarta.validation.ConstraintViolation;

import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;

@Component
public class EmailUniqueValidator implements Validator {

	@Autowired
	private StudentRepo studentRepo;

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		StudentDto studentDto = (StudentDto) target;
//		OrganiserDto organiserDto = (OrganiserDto) target;

		Student s = studentRepo.findByEmail(studentDto.getEmail());
//		 Organiser organiser = organiserRepo.findByEmail(organiserDto.getEmail());

		
		if (s != null) {
			errors.rejectValue("email", "404", "Email already Exist");
		}
//		if (organiser != null) {
//			errors.rejectValue("email", "404" , "Email already Exist");
//		}

	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return StudentDto.class.isAssignableFrom(clazz);

	}

}
