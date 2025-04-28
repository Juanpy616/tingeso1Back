package com.tgs.JPkarts.Services;

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
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {

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

    private VoucherEntity testVoucher;
    private ReservationEntity testReservation;

    @BeforeEach
    void setUp() {
        testVoucher = new VoucherEntity();
        testVoucher.setId(1L);
        testVoucher.setClientName("Test Client");
        testVoucher.setClientEmail("test@example.com");
        testVoucher.setReservationId(1L);
        testVoucher.setBasePrice(20000);
        testVoucher.setSizeDiscount(0);
        testVoucher.setSpecialDiscount(0);
        testVoucher.setPriceAfterDiscount(0);
        testVoucher.setIva(0);
        testVoucher.setFinalPrice(0);

        testReservation = new ReservationEntity();
        testReservation.setId(1L);
        testReservation.setDate(LocalDate.now());
        testReservation.setDuration(30);
        testReservation.setQuantity(4);
    }

    @Test
    void getAllVouchers_ShouldReturnAllVouchers() {
        VoucherEntity voucher2 = new VoucherEntity();
        voucher2.setId(2L);
        List<VoucherEntity> vouchers = Arrays.asList(testVoucher, voucher2);

        when(voucherRepository.findAll()).thenReturn(vouchers);

        List<VoucherEntity> result = voucherService.getAllVouchers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(voucherRepository, times(1)).findAll();
    }

    @Test
    void updateVoucher_ShouldSaveVoucher() {
        when(voucherRepository.save(testVoucher)).thenReturn(testVoucher);

        VoucherEntity result = voucherService.updateVoucher(testVoucher);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(voucherRepository, times(1)).save(testVoucher);
    }

    @Test
    void getVoucherById_ShouldReturnVoucher() {
        when(voucherRepository.findById(1L)).thenReturn(Optional.of(testVoucher));

        VoucherEntity result = voucherService.getVoucherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(voucherRepository, times(1)).findById(1L);
    }

    @Test
    void getVoucherById_WhenNotFound_ShouldThrowException() {
        when(voucherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherService.getVoucherById(99L));
    }
    @Test
    void deleteVoucherById_WhenNotFound_ShouldThrowException() {
        when(voucherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> voucherService.deleteVoucherById(99L));
        verify(voucherRepository, never()).deleteById(any());
    }

    @Test
    void setBasePrice_WhenPriceAlreadySet_ShouldNotChange() {
        testVoucher.setBasePrice(30000);
        voucherService.setBasePrice(testVoucher, testReservation);
        assertEquals(30000, testVoucher.getBasePrice());
    }

    @Test
    void calcDiscounts_ShouldCalculateCorrectDiscounts() {
        // Test size discount
        testReservation.setQuantity(6);
        voucherService.calcDiscounts(testVoucher, testReservation);
        assertEquals(4000, testVoucher.getSizeDiscount()); // 20% of 20000

        // Test special discount (percentage)
        testVoucher.setSpecialDiscount(10); // 10%
        voucherService.calcDiscounts(testVoucher, testReservation);
        assertEquals(2000, testVoucher.getSpecialDiscount()); // 10% of 20000
    }

    @Test
    void applyDiscounts_ShouldCalculateFinalPriceCorrectly() {
        testVoucher.setBasePrice(20000);
        testVoucher.setSizeDiscount(2000);
        testVoucher.setSpecialDiscount(1000);

        voucherService.applyDiscounts(testVoucher);

        assertEquals(17000, testVoucher.getPriceAfterDiscount()); // 20000 - (2000 + 1000)
        assertEquals(3230, testVoucher.getIva()); // 19% of 17000
        assertEquals(20230, testVoucher.getFinalPrice()); // 17000 + 3230
    }

    @Test
    void saveVoucher_ShouldCalculatePricesAndSaveCorrectly() {
        // Arrange
        VoucherEntity voucherToSave = new VoucherEntity();
        voucherToSave.setReservationId(1L);
        voucherToSave.setClientName("Test Client");
        voucherToSave.setClientEmail("test@example.com");
        voucherToSave.setBasePrice(0); // Se calculará automáticamente

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(1L);
        reservation.setDuration(30); // 30 minutos = precio base 15000
        reservation.setQuantity(4);  // Grupo mediano = 10% descuento

        // Configurar mocks
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(voucherRepository.save(any(VoucherEntity.class))).thenAnswer(invocation -> {
            VoucherEntity v = invocation.getArgument(0);
            v.setId(1L); // Simular ID generado
            return v;
        });

        // Act
        VoucherEntity savedVoucher = voucherService.saveVoucher(voucherToSave);

        // Assert
        assertNotNull(savedVoucher);
        assertNotNull(savedVoucher.getId());

        // Verificar precios calculados
        assertEquals(15000, savedVoucher.getBasePrice()); // Precio base para 30 mins
        assertEquals(1500, savedVoucher.getSizeDiscount()); // 10% de 15000
        assertTrue(savedVoucher.getSpecialDiscount() >= 0); // Descuento por visitas

        // Verificar precios finales
        int expectedPriceAfterDiscount = savedVoucher.getBasePrice() -
                savedVoucher.getSizeDiscount() -
                savedVoucher.getSpecialDiscount();
        assertEquals(expectedPriceAfterDiscount, savedVoucher.getPriceAfterDiscount());

        // Verificar IVA (19%)
        assertEquals((int)Math.round(savedVoucher.getPriceAfterDiscount() * 0.19), savedVoucher.getIva());

        // Verificar precio final
        assertEquals(savedVoucher.getPriceAfterDiscount() + savedVoucher.getIva(),
                savedVoucher.getFinalPrice());

        // Verificar interacciones con repositorios
        verify(reservationRepository, times(1)).findById(1L);
        verify(voucherRepository, times(1)).save(any(VoucherEntity.class));
    }

}