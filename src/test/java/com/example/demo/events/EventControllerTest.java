package com.example.demo.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.common.RestDocsConfiguration;
import com.example.demo.events.entity.Event;
import com.example.demo.events.entity.EventDto;
import com.example.demo.events.entity.EventValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	
	
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
			.location("�����������")
			.build();
		
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_UTF8)				//	contentType ->MediaType �� ��û�� JSON�� ��Ƽ� ����
				.accept(MediaTypes.HAL_JSON_VALUE)							//	accept ����� ���� ��� ������ ���ϴ��� �˷���  Hypertext Application Language
				.content(objectMapper.writeValueAsString(event)))			
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
			.andExpect(jsonPath("id").exists())							//	id �� �ִ��� Ȯ��
			.andExpect(jsonPath("free").value(false))
			.andExpect(jsonPath("offline").value(false)) 
			.andExpect(jsonPath("id").value(Matchers.not(100)))
			.andExpect(jsonPath("_links.self").exists())
//			.andExpect(jsonPath("_links.profile").exists())
			.andExpect(jsonPath("_links.update-event").exists());
	}
	
	@Test
	@DisplayName("�� �� ���� ��û30���� �̺�Ʈ�� 10���� �ι�° ������ ��ȸ�ϱ�")
	public void  queryEvents() throws Exception {
		//Given
		IntStream.range(0, 30).forEach(i -> {
			this.generation(i);
		});
		
		//When
		this.mockMvc.perform(get("/api/events")
				.param("page", "1")
				.param("size", "10")
				.param("sort", "name,DESC"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("page").exists())
			.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
			;
	}

	@Test
	@DisplayName("������ �̺�Ʈ �ϳ� ��ȸ�ϱ�")
	public void getEvent() throws Exception {
		//Given
		Event event = this.generation(100);
		
		// When
		this.mockMvc.perform(get("/api/events/{id}", event.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").exists())
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("_links.self").exists());
	}
	
	@Test
	@DisplayName("���� �̺�Ʈ ��ȸ�� 404 ����ޱ�")
	public void getEvent404() throws Exception {
		// When
		this.mockMvc.perform(get("/api/events/1133"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("���� �̺�Ʈ")
	public void updateEvent() throws Exception{
		//Given
		Event event = this.generation(200);
		String eventName = "update event";
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		eventDto.setName(eventName);
		
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.objectMapper.writeValueAsString(eventDto)))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("name").value(eventName))
					.andExpect(jsonPath("_links.self").exists())
					;
	}
	
	
	
	@Test
	@DisplayName("���� �̺�Ʈ�� ���� ���")
	public void notUpdateEvent() throws Exception{
		Event event = this.generation(200);
		EventDto eventDto = new EventDto();
		
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("���� �̺�Ʈ�� �߸��� ���")
	public void falseUpdateEvent() throws Exception{
		Event event = this.generation(200);
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		eventDto.setBasePrice(200000);
		eventDto.setMaxPrice(1000);
		
		this.mockMvc.perform(put("/api/events/{id}", event.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("�������� �ʴ� �̺�Ʈ ���� ����")
	public void updateEvent404() throws Exception{
		Event event = this.generation(200);
		EventDto eventDto = this.modelMapper.map(event, EventDto.class);
		
		this.mockMvc.perform(put("/api/events/1123333333", event.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isNotFound());
	}
	
	private Event generation(int index) {
		Event event = Event.builder()
				.name("event" + index)
				.description("test event")
				.name("Spring")
				.description("REST API Dev with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
				.closeEnrollmentDateTime(LocalDateTime.of(2022, 05, 02, 18, 32)) 
				.beginEventDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
				.endEventDateTime(LocalDateTime.of(2022, 05, 02, 18, 32))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("�����������")
				.free(false)
				.offline(true)
				.eventStatus(EventStatus.DRAFT)
				.build();
		
		return this.eventRepository.save(event);
	}
}
