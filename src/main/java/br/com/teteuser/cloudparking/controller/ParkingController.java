package br.com.teteuser.cloudparking.controller;

import br.com.teteuser.cloudparking.controller.dto.ParkingRequest;
import br.com.teteuser.cloudparking.controller.dto.ParkingResponse;
import br.com.teteuser.cloudparking.controller.mapper.ParkingMapper;
import br.com.teteuser.cloudparking.model.Parking;
import br.com.teteuser.cloudparking.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    private final ParkingService parkingService;
    private final ParkingMapper parkingMapper;

    public ParkingController(ParkingService parkingService, ParkingMapper parkingMapper) {
        this.parkingService = parkingService;
        this.parkingMapper = parkingMapper;
    }

    @GetMapping
    public ResponseEntity<List<ParkingResponse>> findAll(){
        List<Parking> parkingList =  parkingService.findAll();
        List<ParkingResponse> result = parkingMapper.parkingListToResponseList(parkingList);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingResponse> findById(@PathVariable String id){
        Parking returnedParking = parkingService.findById(id);
        ParkingResponse parkingResponse = parkingMapper.parkingToResponse(returnedParking);
        return ResponseEntity.ok().body(parkingResponse);
    }

    @PostMapping
    public ResponseEntity<ParkingResponse> save(@RequestBody ParkingRequest request){
        Parking parkingToBeSaved = parkingMapper.requestToParking(request);
        Parking parking = parkingService.save(parkingToBeSaved);
        ParkingResponse savedParkingResponse = parkingMapper.parkingToResponse(parking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParkingResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingResponse> update(@PathVariable String id, @RequestBody ParkingRequest request){
        Parking parkingToBeUpdated = parkingMapper.requestToParking(request);
        Parking parking = parkingService.update(id, parkingToBeUpdated);
        ParkingResponse savedParkingResponse = parkingMapper.parkingToResponse(parking);
        return ResponseEntity.status(HttpStatus.OK).body(savedParkingResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ParkingResponse> exit(@PathVariable String id, @RequestBody ParkingRequest request){
        Parking parkingToBeUpdated = parkingMapper.requestToParking(request);
        Parking parking = parkingService.exit(id, parkingToBeUpdated);
        ParkingResponse changedParkingResponse = parkingMapper.parkingToResponse(parking);
        return ResponseEntity.status(HttpStatus.OK).body(changedParkingResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id){
        parkingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
