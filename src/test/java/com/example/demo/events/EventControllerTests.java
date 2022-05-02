package com.example.demo.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
	
	@MockBean
	EventRepository eventRepository;
	
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
			.location("새우버섯농장")
			.build();
		
		event.setId(10);
		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_UTF8)				//	contentType ->MediaType 이 요청에 JSON을 담아서 보냄
				.accept(MediaTypes.HAL_JSON_VALUE)							//	accept 헤더를 통해 어떠한 응답을 원하는지 알려줌  Hypertext Application Language
				.content(objectMapper.writeValueAsString(event)))			
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists());							//	id 가 있는지 확인
	}
}
