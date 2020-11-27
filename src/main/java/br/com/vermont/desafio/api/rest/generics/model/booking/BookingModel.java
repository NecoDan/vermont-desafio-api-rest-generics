package br.com.vermont.desafio.api.rest.generics.model.booking;

import br.com.vermont.desafio.api.rest.generics.util.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.time.LocalDateTime;

@ToString
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sp10_booking", schema = "vermont_services")
@Inheritance(strategy = InheritanceType.JOINED)
public class BookingModel extends AbstractEntity {

    @Tolerate
    public BookingModel() {
        super();
    }

    private String reserveName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int numberGuests;
}
