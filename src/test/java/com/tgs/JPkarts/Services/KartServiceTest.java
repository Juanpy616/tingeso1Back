package com.tgs.JPkarts.Services;

import com.tgs.JPkarts.entities.KartEntity;
import com.tgs.JPkarts.repositories.KartRepository;
import com.tgs.JPkarts.services.KartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KartServiceTest {

    @Mock
    private KartRepository kartRepository;

    @InjectMocks
    private KartService kartService;

    private KartEntity testKart;
    private KartEntity savedKart;
    private List<KartEntity> kartList;

    @BeforeEach
    void setUp() {
        testKart = new KartEntity();
        testKart.setId(1L);
        testKart.setName("Test Kart");
        testKart.setActive(true);

        savedKart = new KartEntity();
        savedKart.setId(1L);
        savedKart.setName("Test Kart");
        savedKart.setActive(true);

        KartEntity kart2 = new KartEntity();
        kart2.setId(2L);
        kart2.setName("Inactive Kart");
        kart2.setActive(false);

        kartList = Arrays.asList(testKart, kart2);
    }

    @Test
    void getAllKarts_ShouldReturnAllKarts() {
        when(kartRepository.findAll()).thenReturn(kartList);

        List<KartEntity> result = kartService.getAllKarts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Kart", result.get(0).getName());
        assertTrue(result.get(0).isActive());
        assertEquals("Inactive Kart", result.get(1).getName());
        assertFalse(result.get(1).isActive());
        verify(kartRepository, times(1)).findAll();
    }

    @Test
    void saveKart_ShouldSaveAndReturnKart() {
        when(kartRepository.save(testKart)).thenReturn(savedKart);

        KartEntity result = kartService.saveKart(testKart);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Kart", result.getName());
        assertTrue(result.isActive());
        verify(kartRepository, times(1)).save(testKart);
    }

    @Test
    void updateKart_ShouldUpdateAndReturnKart() {
        KartEntity updatedKart = new KartEntity();
        updatedKart.setId(1L);
        updatedKart.setName("Updated Kart");
        updatedKart.setActive(false); // Changing active status

        when(kartRepository.save(updatedKart)).thenReturn(updatedKart);

        KartEntity result = kartService.updateKart(updatedKart);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Kart", result.getName());
        assertFalse(result.isActive());
        verify(kartRepository, times(1)).save(updatedKart);
    }

    @Test
    void deleteKartById_WhenKartExists_ShouldReturnTrue() throws Exception {
        Long kartId = 1L;
        doNothing().when(kartRepository).deleteById(kartId);

        boolean result = kartService.deleteKartById(kartId);

        assertTrue(result);
        verify(kartRepository, times(1)).deleteById(kartId);
    }

    @Test
    void deleteKartById_WhenKartDoesNotExist_ShouldReturnFalse() throws Exception {
        Long kartId = 99L;
        doThrow(new RuntimeException()).when(kartRepository).deleteById(kartId);

        boolean result = kartService.deleteKartById(kartId);

        assertFalse(result);
        verify(kartRepository, times(1)).deleteById(kartId);
    }
}