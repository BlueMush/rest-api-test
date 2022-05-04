package com.example.demo.events.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.demo.events.EventStatus;
import com.example.demo.events.account.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Event {
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	private String description;
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;
	private String location;		//	(optional)
	private int basePrice;			//	(optional)
	private int maxPrice;			//	(optional)
	private int limitOfEnrollment;	//	(optional)
	private boolean offline;
	private boolean free;
	
	
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus;
	
	@ManyToOne
	private Account account;
	
	public void update() {
		if(this.basePrice == 0 && this.maxPrice == 0) {
			this.free = true;
		} else {
			this.free = false;
		}
		
		if(this.location != null && !this.location.isBlank()) {
			this.offline = true;
		} else {
			this.offline = false;
		}
	}
}
