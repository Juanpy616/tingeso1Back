package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.AnalyticsRepository;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.services.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Mock
    private VoucherEntity voucher;

    @Mock
    private ReservationEntity reservation;

    @Mock
    private AnalyticsEntity analyticsEntity;

    @Test
    void shouldCreateAnalyticsIfNotExistAndUpdateTenMinsSmallGroup() {
        // Arrange
        VoucherEntity voucher = new VoucherEntity();
        voucher.setReservationId(1L);
        voucher.setPriceAfterDiscount(50);

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(1L);
        reservation.setDuration(30);
        reservation.setQuantity(2);
        reservation.setDate(LocalDate.of(2025, Month.APRIL, 15));

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.APRIL, 2025)).thenReturn(null);

        // Mock saving new AnalyticsEntity
        AnalyticsEntity newAnalytics = new AnalyticsEntity();
        newAnalytics.setMonth(Month.APRIL);
        newAnalytics.setYear(2025);
        newAnalytics.setTenMins(0L);
        newAnalytics.setSmallGroup(0L);
        when(analyticsRepository.findByMonthAndYear(Month.APRIL, 2025)).thenReturn(newAnalytics);

        // Act
        analyticsService.addToAnalytics(voucher);

        // Assert
        assertEquals(50L, newAnalytics.getTenMins());
        assertEquals(50L, newAnalytics.getSmallGroup());
    }
    @Test
    void shouldUpdateFifteenMinsAndMediumGroupWhenAnalyticsExists() {
        // Arrange
        VoucherEntity voucher = new VoucherEntity();
        voucher.setReservationId(2L);
        voucher.setPriceAfterDiscount(80); // suponiendo que es long

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(2L);
        reservation.setDuration(35);
        reservation.setQuantity(4);
        reservation.setDate(LocalDate.of(2025, Month.MARCH, 10));

        AnalyticsEntity existingAnalytics = new AnalyticsEntity();
        existingAnalytics.setMonth(Month.MARCH);
        existingAnalytics.setYear(2025);
        existingAnalytics.setFifteenMins(100L);
        existingAnalytics.setMediumGroup(200L);

        // Mocks
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.MARCH, 2025)).thenReturn(existingAnalytics);

        // Act
        analyticsService.addToAnalytics(voucher);

        // Assert
        assertEquals(180L, existingAnalytics.getFifteenMins());    // 100 + 80
        assertEquals(280L, existingAnalytics.getMediumGroup());    // 200 + 80
    }
    @Test
    void shouldSubtractFifteenMinsAndMediumGroupWhenAnalyticsExists() {
        // Arrange
        VoucherEntity voucher = new VoucherEntity();
        voucher.setReservationId(2L);
        voucher.setPriceAfterDiscount(80);  // Asumiendo que es un long

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(2L);
        reservation.setDuration(35);  // Duración de 35 minutos
        reservation.setQuantity(4);   // Tamaño del grupo: 4 personas
        reservation.setDate(LocalDate.of(2025, Month.MARCH, 10));

        AnalyticsEntity existingAnalytics = new AnalyticsEntity();
        existingAnalytics.setMonth(Month.MARCH);
        existingAnalytics.setYear(2025);
        existingAnalytics.setFifteenMins(100L);  // Valor inicial de 15 minutos
        existingAnalytics.setMediumGroup(200L); // Valor inicial de grupo medio

        // Mocks
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.MARCH, 2025)).thenReturn(existingAnalytics);

        // Act
        analyticsService.subtractFromAnalytics(voucher);

        // Assert
        assertEquals(20L, existingAnalytics.getFifteenMins());  // 100L - 80L (precio después del descuento)
        assertEquals(120L, existingAnalytics.getMediumGroup());  // 200L - 80L (precio después del descuento)
    }

    @Test
    void shouldUpdateTenMinsAndSmallGroupWhenAnalyticsExists() {
        // Arrange
        VoucherEntity voucher = new VoucherEntity();
        voucher.setReservationId(3L);
        voucher.setPriceAfterDiscount(50);  // Asumiendo que es un long

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(3L);
        reservation.setDuration(30);  // Duración de 30 minutos
        reservation.setQuantity(2);   // Tamaño del grupo: 2 personas (grupo pequeño)
        reservation.setDate(LocalDate.of(2025, Month.MARCH, 10));

        AnalyticsEntity existingAnalytics = new AnalyticsEntity();
        existingAnalytics.setMonth(Month.MARCH);
        existingAnalytics.setYear(2025);
        existingAnalytics.setTenMins(150L);  // Valor inicial de 10 minutos
        existingAnalytics.setSmallGroup(300L); // Valor inicial de grupo pequeño

        // Mocks
        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.MARCH, 2025)).thenReturn(existingAnalytics);

        // Act
        analyticsService.subtractFromAnalytics(voucher);

        // Assert
        assertEquals(100L, existingAnalytics.getTenMins());  // 150L - 50L (precio después del descuento)
        assertEquals(250L, existingAnalytics.getSmallGroup());  // 300L - 50L (precio después del descuento)
    }



}


