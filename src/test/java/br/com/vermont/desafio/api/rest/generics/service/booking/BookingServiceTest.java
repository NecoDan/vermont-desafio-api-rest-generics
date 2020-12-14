package br.com.vermont.desafio.api.rest.generics.service.booking;

import br.com.vermont.desafio.api.rest.generics.booking.service.BookingService;
import br.com.vermont.desafio.api.rest.generics.booking.model.BookingModel;
import br.com.vermont.desafio.api.rest.generics.booking.repository.IBookingRepository;
import br.com.vermont.desafio.api.rest.generics.util.RandomicoUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class BookingServiceTest {

    @TestConfiguration
    static class BookingServiceConfiguration {
        @Bean
        public BookingService bookingServiceBean() {
            return new BookingService();
        }
    }

    @Autowired
    BookingService bookingService;

    @MockBean
    IBookingRepository bookingRepository;

    @Before
    public void setUp() {
        LocalDateTime dataCheckIn = LocalDateTime.now();
        LocalDateTime dataCheckOut = dataCheckIn.plusDays(10);

        String nomeClienteReserva = "Daniel";
        BookingModel bookingModel = BookingModel.builder()
                .id(UUID.randomUUID())
                .checkIn(dataCheckIn)
                .checkOut(dataCheckOut)
                .reserveName(nomeClienteReserva)
                .numberGuests(RandomicoUtil.gerarValorRandomico())
                .build();

        Mockito.when(bookingRepository.findByReserveName(nomeClienteReserva)).thenReturn(Optional.of(bookingModel));
    }

    @Test
    public void bookingTestServiceCalculaTotalDias() {
        String nome = "Daniel";
        int totalDias = bookingService.calcularTotalDiasWithDatabase(nome);
        assertEquals(totalDias, 10);
    }
}
