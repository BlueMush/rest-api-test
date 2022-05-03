package com.example.demo.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.example.demo.events.controller.EventController;
import com.example.demo.events.entity.Event;

public class EventResource extends EntityModel<Event>{
//	@JsonUnwrapped
//	private Event event;
//	
//	public EventResource(Event event) {
//		this.event = event;
//	}
//	
//	public Event getEvent() {
//		return event;
//	}
	
	public EventResource(Event event, Link... links) {
		super(event);
		add(WebMvcLinkBuilder.linkTo(EventController.class).slash(event.getId()).withSelfRel());
	}
}
