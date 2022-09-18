package br.com.teteuser.cloudparking.service;

import br.com.teteuser.cloudparking.exception.ParkingNotFoundException;
import br.com.teteuser.cloudparking.model.Parking;
import br.com.teteuser.cloudparking.repository.ParkingRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    private ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository){
        this.parkingRepository = parkingRepository;
    }

    @Transactional(readOnly = true)
    public List<Parking> findAll(){
        return parkingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Parking findById(Long id){
        return parkingRepository.findById(id).orElseThrow(() -> new ParkingNotFoundException(id));
    }

    @Transactional
    public Parking save(Parking parkingToBeSaved){
        parkingToBeSaved.setEntryDate(LocalDateTime.now());
        return parkingRepository.save(parkingToBeSaved);
    }

    @Transactional
    public Boolean delete(Long id) {
        parkingRepository.delete(findById(id));
        return true;
    }

    @Transactional
    public Parking update(Long id, Parking parkingToBeUpdated) {
        Parking foundParking = findById(id);
        BeanUtils.copyProperties(parkingToBeUpdated, foundParking);
        return parkingRepository.save(foundParking);
    }

    @Transactional
    public Parking checkOut(Long id){
        Parking foundParking = findById(id);
        foundParking.setExitDate(LocalDateTime.now());
        foundParking.setBill(ParkingCheckOut.getBill(foundParking));
        return parkingRepository.save(foundParking);
    }

}
