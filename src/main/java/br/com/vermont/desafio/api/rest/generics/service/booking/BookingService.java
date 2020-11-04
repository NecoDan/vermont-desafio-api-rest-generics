package br.com.vermont.desafio.api.rest.generics.service.booking;

import br.com.vermont.desafio.api.rest.generics.model.booking.BookingModel;
import br.com.vermont.desafio.api.rest.generics.repository.booking.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public int calcularTotalDiasWithDatabase(String nome) {
        Optional<BookingModel> optionalBookingModel = this.bookingRepository.findByReserveName(nome);
        return Period.between(optionalBookingModel.get().getCheckIn().toLocalDate(), optionalBookingModel.get().getCheckOut().toLocalDate()).getDays();
    }
}
