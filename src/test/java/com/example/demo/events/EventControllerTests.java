package com.example.demo.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class EventControllerTests {
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@SuppressWarnings("deprecation")
	@Test
	public void createEvent() throws Exception {
		
		Event event = Event.builder()
		.name("Spring")
		.description("REST API Dev with Spring")
		.beginEnrollmentDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
		.closeEnrollmentDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
		.beginEventDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
		.endEventDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
		.basePrice(100)
		.maxPrice(200)
		.limitOfEnrollment(100)
		.location("»õ¿ì¹ö¼¸³óÀå")
		.build();
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaTypes.HAL_JSON_VALUE)
				.content(objectMapper.writeValueAsString(event)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists());
	}
}
