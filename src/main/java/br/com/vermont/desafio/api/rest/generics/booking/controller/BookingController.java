package br.com.vermont.desafio.api.rest.generics.booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @GetMapping("/")
    @ResponseBody
    public String getAll() {
        return "It's Ok!";
    }
}
