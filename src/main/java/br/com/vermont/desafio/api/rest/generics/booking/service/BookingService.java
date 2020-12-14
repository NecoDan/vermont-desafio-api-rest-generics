package br.com.vermont.desafio.api.rest.generics.booking.service;

import br.com.vermont.desafio.api.rest.generics.booking.model.BookingModel;
import br.com.vermont.desafio.api.rest.generics.booking.repository.IBookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.Optional;

@Slf4j
@Service
public class BookingService {

    @Autowired
    private IBookingRepository bookingRepository;

    public int calcularTotalDiasWithDatabase(String nome) {
        Optional<BookingModel> optionalBookingModel = this.bookingRepository.findByReserveName(nome);
        return Period.between(optionalBookingModel.get().getCheckIn().toLocalDate(), optionalBookingModel.get().getCheckOut().toLocalDate()).getDays();
    }
}
