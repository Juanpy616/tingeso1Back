package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.services.ReservationService;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationServiceTest {

    ReservationService reservationService = new ReservationService();
    ReservationEntity reservation = new ReservationEntity();

    @Test
    public void whenDurationIs30_thenEndTimeIs1930(){
        //given
        reservation.setDuration(30);
        reservation.setStartTime(LocalTime.of(19, 0));

        reservationService.setEndTime(reservation);

        assertThat(reservation.getEndTime()).isEqualTo(LocalTime.of(19, 30));
    }

    @Test public void whenDurationIs40_thenEndTimeIs2245(){
        reservation.setDuration(40);
        reservation.setStartTime(LocalTime.of(22, 0));

        reservationService.setEndTime(reservation);

        assertThat(reservation.getEndTime()).isEqualTo(LocalTime.of(22, 40));
    }
}
