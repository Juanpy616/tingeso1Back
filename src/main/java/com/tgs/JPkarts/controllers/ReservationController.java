package com.tgs.JPkarts.controllers;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tgs/rmkarts/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/")
    public ResponseEntity<List<ReservationEntity>> listReservations(){
        List<ReservationEntity>  reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable long id){
        ReservationEntity reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/")
    public ResponseEntity<ReservationEntity> createReservation(@RequestBody ReservationEntity reservation){
        ReservationEntity reservationNew = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(reservationNew);
    }

    @PutMapping("/")
    public ResponseEntity<ReservationEntity> updateReservation(@RequestBody ReservationEntity reservation){
        ReservationEntity reservationUpdated = reservationService.updateReservation(reservation);
        return ResponseEntity.ok(reservationUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReservation(@PathVariable long id) throws Exception {
        var isDeleted = reservationService.deleteReservationById(id);
        return ResponseEntity.noContent().build();
    }
}
