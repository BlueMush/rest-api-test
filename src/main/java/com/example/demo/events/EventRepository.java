package com.example.demo.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.example.demo.events.entity.Event;

@Service
public interface EventRepository extends JpaRepository<Event, Integer>{

}
