package com.tgs.JPkarts.services;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.AnalyticsRepository;
import com.tgs.JPkarts.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
public class AnalyticsService {
    @Autowired
    ReservationService reservationService;
    @Autowired
    VoucherService voucherService;
    @Autowired
    AnalyticsRepository analyticsRepository;
    @Autowired
    VoucherRepository reservationRepository;

    public List<AnalyticsEntity> getAllAnalytics() { return analyticsRepository.findAll();}
/*
    //Cuando se crea un voucher, se suman sus ganancias al resumen
    public void addToAnalytics(VoucherEntity voucher) {
        ReservationEntity reservation = reservationRepository.findById(voucher.getReservationId()).get();
        //Si no existe un Analytics para el mes actual, se crea
    }

    //Si es que se elimina un voucher, se restan las ganancias del resumen
    public void subtractFromAnalytics(VoucherEntity voucher) {
        ReservationEntity reservation = reservationRepository.findById(voucher.getReservationId()).get();
        Month month = reservation.getDate().getMonth();
        int year = reservation.getDate().getYear();
        AnalyticsEntity analyticsEntity = analyticsRepository.findByMonthAndYear(month, year);
        if(reservation.getDuration()==30){
            analyticsEntity.setTenMins(analyticsEntity.getTenMins() - voucher.getFinalPrice());
        }
        else if(reservation.getDuration()==35){
            analyticsEntity.setFifteenMins(analyticsEntity.getFifteenMins() - voucher.getFinalPrice());
        }
        else{
            analyticsEntity.setTwentyMins(analyticsEntity.getTwentyMins() - voucher.getFinalPrice());
        }
        if(reservation.getQuantity()<=2){
            analyticsEntity.setSmallGroup(analyticsEntity.getSmallGroup() - voucher.getFinalPrice());
        }
        else if(reservation.getQuantity()<=5&&reservation.getQuantity()>=3){
            analyticsEntity.setMediumGroup(analyticsEntity.getMediumGroup() - voucher.getFinalPrice());
        }
        else if(reservation.getQuantity()<=10&&reservation.getQuantity()>=6){
            analyticsEntity.setBigGroup(analyticsEntity.getBigGroup() - voucher.getFinalPrice());
        }
        else{
            analyticsEntity.setVeryBigGroup(analyticsEntity.getVeryBigGroup() - voucher.getFinalPrice());
        }
    }*/




}
