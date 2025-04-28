package com.tgs.JPkarts.services;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.AnalyticsRepository;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
public class AnalyticsService {
    @Autowired
    AnalyticsRepository analyticsRepository;
    @Autowired
    ReservationRepository reservationRepository;

    public AnalyticsEntity createAnalytics(Month month, int year) {
        AnalyticsEntity analyticsEntity = new AnalyticsEntity();
        analyticsEntity.setMonth(month);
        analyticsEntity.setYear(year);
        return analyticsRepository.save(analyticsEntity);
    }
    public List<AnalyticsEntity> getAllAnalytics() { return analyticsRepository.findAll();}

    //Cuando se crea un voucher, se suman sus ganancias al resumen
    public void addToAnalytics(VoucherEntity voucher) {
        ReservationEntity reservation = reservationRepository.findById(voucher.getReservationId()).get();
        Month month = reservation.getDate().getMonth();
        int year = reservation.getDate().getYear();
        //Si no existe un Analytics para el mes actual, se crea
        if (analyticsRepository.findByMonthAndYear(month, year) == null) {
            AnalyticsEntity analyticsEntity = new AnalyticsEntity();
            analyticsEntity.setMonth(month);
            analyticsEntity.setYear(year);
            analyticsRepository.save(analyticsEntity);
        }
        AnalyticsEntity analyticsEntity = analyticsRepository.findByMonthAndYear(month, year);
        if(reservation.getDuration()==30){
            analyticsEntity.setTenMins(analyticsEntity.getTenMins() + voucher.getPriceAfterDiscount());
        }
        else if(reservation.getDuration()==35){
            analyticsEntity.setFifteenMins(analyticsEntity.getFifteenMins() + voucher.getPriceAfterDiscount());
        }
        else{
            analyticsEntity.setTwentyMins(analyticsEntity.getTwentyMins() + voucher.getPriceAfterDiscount());
        }
        if(reservation.getQuantity()<=2){
            analyticsEntity.setSmallGroup(analyticsEntity.getSmallGroup() + voucher.getPriceAfterDiscount());
        }
        else if(reservation.getQuantity()<=5&&reservation.getQuantity()>=3){
            analyticsEntity.setMediumGroup(analyticsEntity.getMediumGroup() + voucher.getPriceAfterDiscount());
        }
        else if(reservation.getQuantity()<=10&&reservation.getQuantity()>=6){
            analyticsEntity.setBigGroup(analyticsEntity.getBigGroup() + voucher.getPriceAfterDiscount());
        }
        else{
            analyticsEntity.setVeryBigGroup(analyticsEntity.getVeryBigGroup() + voucher.getPriceAfterDiscount());
        }
    }

    //Si es que se elimina un voucher, se restan las ganancias del resumen
    public void subtractFromAnalytics(VoucherEntity voucher) {
        ReservationEntity reservation = reservationRepository.findById(voucher.getReservationId()).get();
        Month month = reservation.getDate().getMonth();
        int year = reservation.getDate().getYear();
        AnalyticsEntity analyticsEntity = analyticsRepository.findByMonthAndYear(month, year);
        if(reservation.getDuration()==30){
            analyticsEntity.setTenMins(analyticsEntity.getTenMins() - voucher.getPriceAfterDiscount());
        }
        else if(reservation.getDuration()==35){
            analyticsEntity.setFifteenMins(analyticsEntity.getFifteenMins() - voucher.getPriceAfterDiscount());
        }
        else{
            analyticsEntity.setTwentyMins(analyticsEntity.getTwentyMins() - voucher.getPriceAfterDiscount());
        }
        if(reservation.getQuantity()<=2){
            analyticsEntity.setSmallGroup(analyticsEntity.getSmallGroup() - voucher.getPriceAfterDiscount());
        }
        else if(reservation.getQuantity()<=5&&reservation.getQuantity()>=3){
            analyticsEntity.setMediumGroup(analyticsEntity.getMediumGroup() - voucher.getPriceAfterDiscount());
        }
        else if(reservation.getQuantity()<=10&&reservation.getQuantity()>=6){
            analyticsEntity.setBigGroup(analyticsEntity.getBigGroup() - voucher.getPriceAfterDiscount());
        }
        else{
            analyticsEntity.setVeryBigGroup(analyticsEntity.getVeryBigGroup() - voucher.getPriceAfterDiscount());
        }
    }





}
