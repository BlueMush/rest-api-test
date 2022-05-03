package com.example.demo.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.events.entity.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void createEvent() throws Exception {
		
		Event event = Event.builder()
			.id(100)
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
			.free(true)
			.offline(true)
			.build();
		
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_UTF8)				//	contentType ->MediaType 이 요청에 JSON을 담아서 보냄
				.accept(MediaTypes.HAL_JSON_VALUE)							//	accept 헤더를 통해 어떠한 응답을 원하는지 알려줌  Hypertext Application Language
				.content(objectMapper.writeValueAsString(event)))			
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
			.andExpect(jsonPath("id").exists())							//	id 가 있는지 확인
			.andExpect(jsonPath("free").value(false))
			.andExpect(jsonPath("offline").value(false)) 
			.andExpect(jsonPath("id").value(Matchers.not(100)));
	}
}
