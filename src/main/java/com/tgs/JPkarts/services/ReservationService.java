package com.tgs.JPkarts.services;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    public List<ReservationEntity> getAllReservations(){
        return reservationRepository.findAll();
    }

    public ReservationEntity saveReservation(ReservationEntity reservation) {
        setEndTime(reservation);
        return reservationRepository.save(reservation);
    }

    public ReservationEntity getReservationById(Long id) {
        return reservationRepository.findById(id).get();
    }

    public ReservationEntity updateReservation(ReservationEntity reservation) {
        //Se borran todos los vouchers relacionados con la reserva
        setEndTime(reservation);
        List<VoucherEntity> vouchers = voucherRepository.findByReservationId(reservation.getId());
        voucherRepository.deleteAll(vouchers);
        return reservationRepository.save(reservation);
    }

    public boolean deleteReservationById(Long id) throws Exception {
        try {
            ReservationEntity reservation = reservationRepository.findById(id).get();
            //Se borran todos los vouchers relacionados con la reserva
            List<VoucherEntity> vouchers = voucherRepository.findByReservationId(reservation.getId());
            voucherRepository.deleteAll(vouchers);
            reservationRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void setEndTime(ReservationEntity reservation) {
        LocalTime endTime = reservation.getStartTime().plusMinutes(reservation.getDuration());
        reservation.setEndTime(endTime);
    }


}
