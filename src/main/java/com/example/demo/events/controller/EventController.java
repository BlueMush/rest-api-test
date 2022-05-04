package com.example.demo.events.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.events.EventRepository;
import com.example.demo.events.EventResource;
import com.example.demo.events.common.ErrorsResource;
import com.example.demo.events.entity.Event;
import com.example.demo.events.entity.EventDto;
import com.example.demo.events.entity.EventValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
	
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
//	private final EventValidator eventValidator;
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody EventDto eventDto, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		} 
		
		Event event = modelMapper.map(eventDto, Event.class);
		Event newEvent = this.eventRepository.save(event);
		
		WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
		
		URI createdUri = selfLinkBuilder.toUri();
		EventResource eventResource = new EventResource(event);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		
		return ResponseEntity.created(createdUri).body(eventResource);
	}
	
	@GetMapping
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> page = this.eventRepository.findAll(pageable);
		var pageModel = assembler.toModel(page, e -> new EventResource(e));
		
		return ResponseEntity.ok(pageModel);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
//		eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
		
		return ResponseEntity.ok(eventResource);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity updateEvent(@PathVariable Integer id, 
									@RequestBody @Validated EventDto eventDto,
									Errors errors) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
//		this.eventValidator.validate(eventDto, errors);
//		if(errors.hasErrors()) {
//			return badRequest(errors);
//		}
		
		Event existingEvent = optionalEvent.get();
		this.modelMapper.map(eventDto, existingEvent);
		Event save = this.eventRepository.save(existingEvent);
		
		EventResource eventResource = new EventResource(save); 
//		eventResource.add(null)
		
		return ResponseEntity.ok(eventResource);
	}
	
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
}
