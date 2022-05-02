package com.example.demo.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
public class EventController {
	@PostMapping("/api/events")
	public ResponseEntity createEvent(@RequestBody Event event) {
		URI createdUri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
		
		return ResponseEntity.created(createdUri).build();
	}
}
