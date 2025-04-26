package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.services.VoucherService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VoucherServiceTest {

    VoucherEntity voucher = new VoucherEntity();
    ReservationEntity reservation = new ReservationEntity();
    VoucherService voucherService = new VoucherService();

    @Test
    public void whenDurationIs30AndBasePriceIs0_ThenBasePriceIs15k(){

        reservation.setDuration(30);
        voucher.setBasePrice(0);

        voucherService.setBasePrice(voucher, reservation);

        assertThat(voucher.getBasePrice()).isEqualTo(15000);
    }

    @Test
    public void whenDurationIsXAndBasePriceIsY_ThenBasePriceIsY(){

        reservation.setDuration(30);
        voucher.setBasePrice(1802349);//Un entero aleatorio

        voucherService.setBasePrice(voucher, reservation);

        assertThat(voucher.getBasePrice()).isEqualTo(1802349);
    }

    @Test
    public void whenDurationIs35AndBasePriceIs0_ThenBasePriceIs20k(){
        reservation.setDuration(35);
        voucher.setBasePrice(0);

        voucherService.setBasePrice(voucher, reservation);

        assertThat(voucher.getBasePrice()).isEqualTo(20000);
    }
}
