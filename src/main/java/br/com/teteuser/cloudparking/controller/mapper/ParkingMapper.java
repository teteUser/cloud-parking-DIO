package br.com.teteuser.cloudparking.controller.mapper;

import br.com.teteuser.cloudparking.controller.dto.ParkingRequest;
import br.com.teteuser.cloudparking.controller.dto.ParkingResponse;
import br.com.teteuser.cloudparking.model.Parking;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParkingMapper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public ParkingResponse parkingToResponse(Parking parking){
        return MODEL_MAPPER.map(parking, ParkingResponse.class);
    }

    public List<ParkingResponse> parkingListToResponseList(List<Parking> parkingList) {
        return parkingList.stream().map(this::parkingToResponse).collect(Collectors.toList());
    }

    public Parking requestToParking(ParkingRequest parkingRequest){
        return MODEL_MAPPER.map(parkingRequest, Parking.class);
    }

    public Parking responseToParking(ParkingResponse parkingResponse){
        return MODEL_MAPPER.map(parkingResponse, Parking.class);
    }

}
