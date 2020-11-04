package br.com.vermont.desafio.api.rest.generics.model.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingModel {
    private Long id;
    private String reserveName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDateTime dtLancamento;
    private int numberGuests;
}
