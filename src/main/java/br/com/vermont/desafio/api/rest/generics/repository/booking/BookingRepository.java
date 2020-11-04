package br.com.vermont.desafio.api.rest.generics.repository.booking;

import br.com.vermont.desafio.api.rest.generics.model.booking.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    Optional<BookingModel> findByReserveName(@Param("reserveName") String reserveName);
}
