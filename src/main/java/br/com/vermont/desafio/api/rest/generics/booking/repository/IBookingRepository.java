package br.com.vermont.desafio.api.rest.generics.booking.repository;

import br.com.vermont.desafio.api.rest.generics.booking.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBookingRepository extends JpaRepository<BookingModel, UUID> {
    Optional<BookingModel> findByReserveName(@Param("reserveName") String reserveName);
}
