package com.focusGureumWebApp.focusGureumWebdemo.controller;

import com.focusGureumWebApp.focusGureumWebdemo.models.Event;
import com.focusGureumWebApp.focusGureumWebdemo.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{userId}")
    public String getEventService(@PathVariable Integer userId, Model model) {
        List<Event> events = eventService.getAllByUser_Id(userId);
        model.addAttribute("events", events);
        return "events";
    }
}
