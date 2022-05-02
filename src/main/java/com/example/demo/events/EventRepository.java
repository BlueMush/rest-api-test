package com.example.demo.events;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.events.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{

}
