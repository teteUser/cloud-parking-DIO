package br.com.teteuser.cloudparking.service;

import br.com.teteuser.cloudparking.exception.ParkingNotFoundException;
import br.com.teteuser.cloudparking.model.Parking;
import br.com.teteuser.cloudparking.repository.ParkingRepository;
import br.com.teteuser.cloudparking.util.ParkingFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ParkingServiceTest {

    @MockBean
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingService parkingService;

    @Test
    void returnListOfParkingWhenFindAllMethodIsCalled() {
        // Given
        Parking parking = ParkingFactory.buildParking();
        List<Parking> result = List.of(parking);

        // When
        when(parkingRepository.findAll()).thenReturn(result);

        // Then
        List<Parking> actual = parkingService.findAll();
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
        assertEquals(parking, actual.get(0));
    }

    @Test
    void returnParkingRecordWhenFindByIdMethodIsCalled() {
        // Given
        Parking parking = ParkingFactory.buildParking();

        // When
        when(parkingRepository.findById(1L)).thenReturn(Optional.of(parking));

        // Then
        Parking actual = parkingService.findById(1L);
        assertEquals(parking, actual);
        assertEquals(parking.getLicense(), actual.getLicense());
        assertEquals(parking.getState(), actual.getState());
        assertEquals(parking.getModel(), actual.getModel());
        assertEquals(parking.getColor(), actual.getColor());
        assertEquals(parking.getEntryDate(), actual.getEntryDate());
    }

    @Test
    void throwExceptionWhenIdIsNotFound(){
        // Given
        Parking parking = ParkingFactory.buildParking();

        // When
        when(parkingRepository.findById(7L)).thenThrow(new ParkingNotFoundException(7L));

        // Then
        assertThrows(ParkingNotFoundException.class, () -> parkingService.findById(7L));
    }

    @Test
    void persistParkingWhenSaveMethodIsCalled() {
        // Given
        Parking parking = ParkingFactory.buildParking();

        // When
        when(parkingRepository.save(parking)).thenReturn(parking);

        // Then
        Parking actual = parkingService.save(parking);
        assertNotNull(actual);
        assertEquals(parking, actual);
    }

    @Test
    void deleteParkingRecordWhenDeleteMethodIsCalled() {
        // Given
        Parking parking = ParkingFactory.buildParking();

        // When
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));

        // Then
        Boolean isDeleted = parkingService.delete(parking.getId());
        assertTrue(isDeleted);
    }

    @Test
    void updateParkingRecordWhenUpdateMethodIsCalled() {
        // Given
        Parking parking = ParkingFactory.buildParking();
        Parking body = ParkingFactory.buildParking();
        body.setState("SC");

        // When
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
        when(parkingRepository.save(any())).thenReturn(body);
        Parking updatedParking = parkingService.update(parking.getId(), body);

        // Then
        assertNotNull(updatedParking);
        assertFalse(updatedParking.getState().equals("SP"));
    }

    @Test
    void setExitDateAndCalculateBillWhenCheckOutMethodIsCalled() {
        // Given
        Parking parking = ParkingFactory.buildParking();
        parking.setEntryDate(LocalDateTime.now().minusMinutes(50));

        // When
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
        when(parkingRepository.save(any())).thenReturn(parking);

        // Then
        Parking checkedOut = parkingService.checkOut(parking.getId());
        assertNotNull(checkedOut);
        assertEquals(5D, checkedOut.getBill());
    }
}