package br.com.vermont.desafio.api.rest.generics.controller.booking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void deveRetonarTodosComUmaListaBookings() throws Exception {
        log.info("{} ", "#TEST: deveRetonarTodosComUmaListaBookings: ");

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

