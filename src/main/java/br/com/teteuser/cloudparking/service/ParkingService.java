package br.com.teteuser.cloudparking.service;

import br.com.teteuser.cloudparking.controller.dto.ParkingRequest;
import br.com.teteuser.cloudparking.exception.ParkingNotFoundException;
import br.com.teteuser.cloudparking.model.Parking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private static Map<String, Parking> parkingMap = new HashMap();

    public List<Parking> findAll(){
        return parkingMap.values().stream().collect(Collectors.toList());
    }

    private static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Parking findById(String id){
        Parking parking = parkingMap.get(id);
        if(parking == null){
            throw new ParkingNotFoundException(id);
        }
        return parking;
    }

    public Parking save(Parking parkingToBeSaved){
        var newId = getUUID();
        parkingToBeSaved.setId(newId);
        parkingToBeSaved.setEntryDate(LocalDateTime.now());
        parkingMap.put(newId, parkingToBeSaved);
        var savedParking = parkingToBeSaved;
        return savedParking;
    }

    public void delete(String id) {
        findById(id);
        parkingMap.remove(id);
    }

    public Parking update(String id, Parking parkingToBeUpdated) {
        var foundParking = findById(id);
        foundParking.setColor(parkingToBeUpdated.getColor());
        foundParking.setModel(parkingToBeUpdated.getModel());
        foundParking.setLicense(parkingToBeUpdated.getLicense());
        foundParking.setState(parkingToBeUpdated.getState());
        parkingMap.replace(id, foundParking);
        return foundParking;
    }

    public Parking exit(String id, Parking parking){
        var foundParking = findById(id);
        LocalDateTime saida = LocalDateTime.now();
        foundParking.setExitDate(saida);
        parkingMap.replace(id, foundParking);
        return foundParking;
    }

}
