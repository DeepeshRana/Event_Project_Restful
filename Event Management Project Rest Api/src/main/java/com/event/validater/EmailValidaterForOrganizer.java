package com.event.validater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.event.dto.OrganiserDto;
import com.event.dto.StudentDto;
import com.event.entity.Organiser;
import com.event.entity.Student;
import com.event.repo.OrganiserRepo;

@Component
public class EmailValidaterForOrganizer implements Validator {

	@Autowired
	OrganiserRepo organiserRepo;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub

		return OrganiserDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		OrganiserDto organiserDto = (OrganiserDto) target;

		Organiser organiser = organiserRepo.findByEmail(organiserDto.getEmail());

		if (organiser != null) {
			errors.rejectValue("email", "404", "Email already Exist");
		}
	}

}
