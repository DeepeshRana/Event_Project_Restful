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
import com.event.dto.OrganiserDto;
import com.event.entity.Organiser;
import com.event.exception.ResourceNotFoundException;
import com.event.repo.OrganiserRepo;
import com.event.service.OrganiserService;

@Service
public class OrganiserServiceImpl implements OrganiserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganiserServiceImpl.class);

	@Autowired
	private OrganiserRepo orgRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public OrganiserDto createOrganiser(OrganiserDto organiserDto) {
		LOGGER.info("Creating organiser: {}", organiserDto);

		Organiser organiser = this.modelMapper.map(organiserDto, Organiser.class);
		Organiser savedOrganiser = this.orgRepo.save(organiser);
		LOGGER.info("Organiser created successfully: {}", savedOrganiser);

		return this.modelMapper.map(savedOrganiser, OrganiserDto.class);
	}

	@Override
	public OrganiserDto updateOrganiser(OrganiserDto organiserDto, Integer orgId) {
		LOGGER.info("Updating organiser with ID {}: {}", orgId, organiserDto);

		Organiser organiser = this.orgRepo.findById(orgId)
				.orElseThrow(() -> new ResourceNotFoundException("Organiser", "orgId", orgId));
		organiser.setOrgName(organiserDto.getOrgName());
		organiser.setEmail(organiserDto.getEmail());
		Organiser updateOrganiser = this.orgRepo.save(organiser);
		LOGGER.info("Organiser updated successfully: {}", updateOrganiser);

		return this.modelMapper.map(updateOrganiser, OrganiserDto.class);
	}

//	@Override
//	public List<OrganiserDto> getAllOrganisers() {
//		List<Organiser> organisers = this.orgRepo.findAll();
//		List<OrganiserDto> organiserDtos = organisers.stream()
//				.map((org) -> this.modelMapper.map(org, OrganiserDto.class)).collect(Collectors.toList());
//		return organiserDtos;
//	}

//	@Override
//	public Page<OrganiserDto> getAllOrganisers(int page, int size) {
//		LOGGER.info("Retrieving all organisers (page={}, size={})", page, size);
//
//		PageRequest pageable = PageRequest.of(page, size);
//		Page<Organiser> organisersPage = orgRepo.findAll(pageable);
//		LOGGER.info("Retrieved {} organisers", organisersPage.getNumberOfElements());
//
//		return organisersPage.map(org -> modelMapper.map(org, OrganiserDto.class));
//	}
	
	@Override
	public CustomPage<OrganiserDto> getAllOrganisers(int page, int size) {
	    LOGGER.info("Retrieving all organisers (page={}, size={})", page, size);

	    PageRequest pageable = PageRequest.of(page, size);
	    Page<Organiser> organisersPage = orgRepo.findAll(pageable);
	    LOGGER.info("Retrieved {} organisers", organisersPage.getNumberOfElements());

	    List<OrganiserDto> organiserDtos = organisersPage.getContent().stream()
	            .map(org -> modelMapper.map(org, OrganiserDto.class))
	            .collect(Collectors.toList());

	    return new CustomPage<>(
	            organiserDtos,
	            organisersPage.getTotalPages(),
	            organisersPage.getTotalElements(),
	            organisersPage.getNumber(),
	            organisersPage.getSize(),
	            organisersPage.hasNext(),
	            organisersPage.hasPrevious()
	    );
	}


	@Override
	public OrganiserDto getOrganiserById(Integer orgId) {
		LOGGER.info("Retrieving organiser with ID: {}", orgId);

		Organiser organiser = this.orgRepo.findById(orgId)
				.orElseThrow(() -> new ResourceNotFoundException("Organiser", "orgId", orgId));
		LOGGER.info("Retrieved organiser: {}", organiser);

		return this.modelMapper.map(organiser, OrganiserDto.class);
	}

	@Override
	public void deleteOrganiser(Integer orgId) {
		LOGGER.info("Deleting organiser with ID: {}", orgId);

		this.orgRepo.delete(this.orgRepo.findById(orgId)
				.orElseThrow(() -> new ResourceNotFoundException("Organiser", "orgId", orgId)));
		LOGGER.info("Organiser deleted successfully");

	}

}
