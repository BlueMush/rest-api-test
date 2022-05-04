package com.example.demo.events.common;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;

import com.example.demo.events.index.IndexController;

public class ErrorsResource extends EntityModel<Errors>{
	public ErrorsResource(Errors content) {
		super(content);
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}
}
