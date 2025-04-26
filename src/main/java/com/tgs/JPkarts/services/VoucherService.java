package com.tgs.JPkarts.services;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static java.lang.Math.round;

@Service
public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    public List<VoucherEntity> getAllVouchers() {return voucherRepository.findAll();}

    public VoucherEntity saveVoucher(VoucherEntity voucher) {
        ReservationEntity reservation = reservationRepository.findById(voucher.getReservationId()).get();
        setBasePrice(voucher, reservation);
        calcDiscounts(voucher, reservation);
        return voucherRepository.save(voucher);
    }

    public VoucherEntity getVoucherById(long id) {return voucherRepository.findById(id).get();}

    //Obtiene descuento según la cantidad de visitas en el mes
    public int discountByVisits(VoucherEntity voucher) {
        String clientName = voucher.getClientName();
        List<VoucherEntity> visitsByThisClient = voucherRepository.findByClientName(clientName);
        int visitsThisMonth = 0;
        Month currentMonth = LocalDate.now().getMonth();
        Year currentYear = Year.now();
        int discount = 0;
        //Primero se calcula la cantidad de visitas en el mes actual
        for (VoucherEntity visitEntity : visitsByThisClient) {
            ReservationEntity reservation = reservationRepository.findById(visitEntity.getReservationId()).get();
            if(reservation.getDate().getMonth() == currentMonth && reservation.getDate().getYear() == currentYear.getValue()) {
                visitsThisMonth++;
            }
        }
        //Luego se calcula el descuento
        if(visitsThisMonth >= 2 && visitsThisMonth <= 4){
            discount = (int)round(voucher.getBasePrice()*0.1);
        }
        else if(visitsThisMonth >= 5 && visitsThisMonth <= 6){
            discount = (int)round(voucher.getBasePrice()*0.2);
        }
        else if(visitsThisMonth >= 7){
            discount = (int)round(voucher.getBasePrice()*0.3);
        }
        return discount;
    }

    public void makePDF(VoucherEntity voucher) {
        
    }

    public boolean deleteVoucherById(@PathVariable long id) throws Exception{
        try{
            reservationRepository.deleteById(id);
            return true;
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    //Aplica el precio correspondiente al voucher
    public void setBasePrice(VoucherEntity voucher, ReservationEntity reservation) {
        if (voucher.getBasePrice() != 0){
            return;
        }
        //Si el precio base es 0, significa que este se asignará automáticamente
        //Esto dependerá de la duración
        if (reservation.getDuration() == 30) {
            voucher.setBasePrice(15000);
        }
        else if (reservation.getDuration() == 35) {
            voucher.setBasePrice(20000);
        }
        else if (reservation.getDuration() == 40) {
            voucher.setBasePrice(25000);
        }
    }

    public void calcDiscounts(VoucherEntity voucher, ReservationEntity reservation) {
        //Primero se aplica el descuento por tamaño de grupo
        if(reservation.getQuantity() >= 3 && reservation.getQuantity() <= 5){
            voucher.setSizeDiscount((int)round(voucher.getBasePrice() * 0.1));
        }
        else if(reservation.getQuantity() >= 6 && reservation.getQuantity() <= 10){
            voucher.setSizeDiscount((int)round(voucher.getBasePrice() * 0.2));
        }
        else if(reservation.getQuantity() >= 11 && reservation.getQuantity() <= 15){
            voucher.setSizeDiscount((int)round(voucher.getBasePrice() * 0.3));
        }
        //Luego se aplica cualquier descuento especial que haya
        if(voucher.getSpecialDiscount() != 0){
            double discount = voucher.getSpecialDiscount()/100.0;
            voucher.setSpecialDiscount((int)round(voucher.getBasePrice()*discount));
        }
        //Si no se especifica un descuento especial, se aplica descuento por visitas este mes
        else{
            voucher.setSpecialDiscount(discountByVisits(voucher));
        }
    }
}

