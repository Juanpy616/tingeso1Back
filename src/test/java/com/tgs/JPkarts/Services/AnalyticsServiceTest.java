package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.AnalyticsEntity;
import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.AnalyticsRepository;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.services.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private VoucherEntity voucher;
    private ReservationEntity reservation;
    private AnalyticsEntity analytics;

    @BeforeEach
    void setup() {
        voucher = new VoucherEntity();
        voucher.setReservationId(1L);
        voucher.setPriceAfterDiscount(100);

        reservation = new ReservationEntity();
        reservation.setId(1L);
        reservation.setDate(LocalDate.of(2025, 4, 10));
        reservation.setDuration(30); // para tenMins
        reservation.setQuantity(2);  // para smallGroup

        analytics = new AnalyticsEntity();
        analytics.setMonth(Month.APRIL);
        analytics.setYear(2025);
        analytics.setTenMins(0L);
        analytics.setSmallGroup(0L);
    }

    @Test
    void testGetAllAnalytics() {
        List<AnalyticsEntity> expectedList = List.of(new AnalyticsEntity());
        when(analyticsRepository.findAll()).thenReturn(expectedList);

        List<AnalyticsEntity> result = analyticsService.getAllAnalytics();

        assertEquals(1, result.size());
        verify(analyticsRepository).findAll();
    }

    @Test
    void testAddToAnalytics_CreatesNewAnalyticsEntity() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.APRIL, 2025)).thenReturn(null);
        when(analyticsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        analyticsService.addToAnalytics(voucher);

        verify(analyticsRepository).save(any());
    }

    @Test
    void testAddToAnalytics_UpdatesExistingAnalyticsEntity() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.APRIL, 2025)).thenReturn(analytics);

        analyticsService.addToAnalytics(voucher);

        assertEquals(100L, analytics.getTenMins());
        assertEquals(100L, analytics.getSmallGroup());
    }

    @Test
    void testSubtractFromAnalytics_UpdatesExistingAnalyticsEntity() {
        analytics.setTenMins(150L);
        analytics.setSmallGroup(150L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(analyticsRepository.findByMonthAndYear(Month.APRIL, 2025)).thenReturn(analytics);

        analyticsService.subtractFromAnalytics(voucher);

        assertEquals(50L, analytics.getTenMins());
        assertEquals(50L, analytics.getSmallGroup());
    }
}



