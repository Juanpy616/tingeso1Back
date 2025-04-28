package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.repositories.ReservationRepository;
import com.tgs.JPkarts.repositories.VoucherRepository;
import com.tgs.JPkarts.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private VoucherRepository voucherRepository;

    @InjectMocks
    private ReservationService reservationService;

    private ReservationEntity testReservation;
    private ReservationEntity savedReservation;
    private VoucherEntity testVoucher;

    @BeforeEach
    void setUp() {
        testReservation = new ReservationEntity();
        testReservation.setId(1L);
        testReservation.setStartTime(LocalTime.of(10, 0));
        testReservation.setDuration(60); // 60 minutes
        testReservation.setEndTime(LocalTime.of(11, 0));

        savedReservation = new ReservationEntity();
        savedReservation.setId(1L);
        savedReservation.setStartTime(LocalTime.of(10, 0));
        savedReservation.setDuration(60);
        savedReservation.setEndTime(LocalTime.of(11, 0));

        testVoucher = new VoucherEntity();
        testVoucher.setId(1L);
        testVoucher.setReservationId(1L); // Updated to use reservationId instead of reservation
    }

    // getAllReservations tests remain the same
    @Test
    void getAllReservations_ShouldReturnAllReservations() {
        ReservationEntity reservation2 = new ReservationEntity();
        reservation2.setId(2L);
        List<ReservationEntity> reservations = Arrays.asList(testReservation, reservation2);

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<ReservationEntity> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    // saveReservation tests remain the same
    @Test
    void saveReservation_ShouldCalculateEndTimeAndSave() {
        testReservation.setEndTime(null);
        when(reservationRepository.save(testReservation)).thenReturn(savedReservation);

        ReservationEntity result = reservationService.saveReservation(testReservation);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalTime.of(11, 0), result.getEndTime());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    // getReservationById tests remain the same
    @Test
    void getReservationById_ShouldReturnReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        ReservationEntity result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    // Updated updateReservation tests to use reservationId
    @Test
    void updateReservation_ShouldDeleteVouchersAndSave() {
        List<VoucherEntity> vouchers = List.of(testVoucher);
        when(voucherRepository.findByReservationId(1L)).thenReturn(vouchers);
        when(reservationRepository.save(testReservation)).thenReturn(savedReservation);

        ReservationEntity result = reservationService.updateReservation(testReservation);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(voucherRepository, times(1)).findByReservationId(1L);
        verify(voucherRepository, times(1)).deleteAll(vouchers);
        verify(reservationRepository, times(1)).save(testReservation);
    }

    // Updated deleteReservationById tests to use reservationId
    @Test
    void deleteReservationById_ShouldDeleteVouchersAndReservation() throws Exception {
        List<VoucherEntity> vouchers = List.of(testVoucher);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(voucherRepository.findByReservationId(1L)).thenReturn(vouchers);
        doNothing().when(reservationRepository).deleteById(1L);

        boolean result = reservationService.deleteReservationById(1L);

        assertTrue(result);
        verify(reservationRepository, times(1)).findById(1L);
        verify(voucherRepository, times(1)).findByReservationId(1L);
        verify(voucherRepository, times(1)).deleteAll(vouchers);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    // setEndTime tests remain the same
    @Test
    void setEndTime_ShouldCalculateCorrectEndTime() {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setStartTime(LocalTime.of(14, 30));
        reservation.setDuration(90);

        reservationService.setEndTime(reservation);

        assertEquals(LocalTime.of(16, 0), reservation.getEndTime());
    }

    // Additional test for voucher with reservationId
    @Test
    void updateReservation_WithMultipleVouchers_ShouldDeleteAll() {
        VoucherEntity voucher2 = new VoucherEntity();
        voucher2.setId(2L);
        voucher2.setReservationId(1L);

        List<VoucherEntity> vouchers = Arrays.asList(testVoucher, voucher2);
        when(voucherRepository.findByReservationId(1L)).thenReturn(vouchers);
        when(reservationRepository.save(testReservation)).thenReturn(savedReservation);

        ReservationEntity result = reservationService.updateReservation(testReservation);

        assertNotNull(result);
        verify(voucherRepository, times(1)).deleteAll(vouchers);
    }
}