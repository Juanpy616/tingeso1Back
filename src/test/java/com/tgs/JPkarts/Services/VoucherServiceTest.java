package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.AnalyticsRepository;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.repositories.VoucherRepository;
import com.tgs.JPkarts.services.AnalyticsService;
import com.tgs.JPkarts.services.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {
    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private VoucherService voucherService;

    // Datos para las pruebas
    private VoucherEntity voucher;
    private ReservationEntity reservation;

    @BeforeEach
    void setUp() {
        voucher = new VoucherEntity();
        reservation = new ReservationEntity();
    }

    @Test
    void saveVoucher_shouldSaveVoucherAndCreateAnalyticsIfNotExists() {
        // Arrange
        Long reservationId = 1L;
        voucher.setReservationId(reservationId);
        voucher.setBasePrice(20000);
        voucher.setSpecialDiscount(0);
        voucher.setSizeDiscount(0);

        // Crear una reserva simulada
        reservation.setId(reservationId);
        reservation.setDuration(35);
        reservation.setQuantity(4);
        reservation.setDate(LocalDate.of(2025, Month.APRIL, 5));

        // Mock de la búsqueda de la reserva por ID
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Simulando que no existe Analytics para este mes/año
        when(analyticsRepository.findByMonthAndYear(Month.APRIL, 2025)).thenReturn(null);

        // Simulando la creación de Analytics
        AnalyticsEntity mockAnalytics = new AnalyticsEntity();
        when(analyticsService.createAnalytics(Month.APRIL, 2025)).thenReturn(mockAnalytics);

        // Simulando que voucherRepository guarde y retorne el voucher
        when(voucherRepository.save(voucher)).thenReturn(voucher);

        // Act
        VoucherEntity savedVoucher = voucherService.saveVoucher(voucher);

        // Assert
        assertNotNull(savedVoucher);  // Verifica que el voucher guardado no sea null
        verify(analyticsService, times(1)).createAnalytics(Month.APRIL, 2025);  // Verifica que se haya creado el Analytics
        verify(voucherRepository, times(1)).save(voucher);  // Verifica que se haya guardado el voucher
    }

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
