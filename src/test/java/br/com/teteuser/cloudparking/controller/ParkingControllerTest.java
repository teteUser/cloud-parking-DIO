package br.com.teteuser.cloudparking.controller;

import br.com.teteuser.cloudparking.controller.dto.ParkingRequest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingControllerTest {

    @LocalServerPort
    private int randomPort;

    @BeforeEach
    public void setUpTest(){
        System.out.println(randomPort);
        RestAssured.port = randomPort;
    }

    @Test
    void whenFindAllCheckResult() {
        RestAssured.given()
                .when()
                .get("parking")
                .then()
                .statusCode(200);
                //.body("license[0]", Matchers.equalTo("UFA-1234"));
    }

    @Test
    void whenSaveThenCheckIsSaved(){

        var saveDTO = new ParkingRequest();
        saveDTO.setColor("Amarelo");
        saveDTO.setLicense("UAU-0505");
        saveDTO.setModel("Gol");
        saveDTO.setState("SP");

        RestAssured.given()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(saveDTO)
                .post("parking")
                .then()
                .statusCode(201)
                .body("license", Matchers.equalTo("UAU-0505"))
                .body("color", Matchers.equalTo("Amarelo"))
                .body("model", Matchers.equalTo("Gol"))
                .body("state", Matchers.equalTo("SP"));
    }

}