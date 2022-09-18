package br.com.teteuser.cloudparking.util;

import br.com.teteuser.cloudparking.model.Parking;

import java.time.LocalDateTime;

public class ParkingFactory {

    public static Parking buildParking(){
        return Parking.builder()
                .Id(1L)
                .license("CNH-0000")
                .state("SP")
                .model("Gol")
                .color("Black")
                .entryDate(LocalDateTime.of(2022, 09, 18, 10, 0))
                .build();
    }

}
