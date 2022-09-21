package br.com.teteuser.cloudparking.controller;

import br.com.teteuser.cloudparking.controller.dto.ParkingRequest;
import br.com.teteuser.cloudparking.model.Parking;
import br.com.teteuser.cloudparking.service.ParkingService;
import br.com.teteuser.cloudparking.util.ParkingFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "user", password = "Dio@123")
class ParkingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void findAll() throws Exception {
        Parking parking = ParkingFactory.buildParking();
        List<Parking> result = List.of(parking);
        when(parkingService.findAll()).thenReturn(result);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/parking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].license").value(result.get(0).getLicense()))
                .andExpect(jsonPath("$[0].state").value(result.get(0).getState()))
                .andExpect(jsonPath("$[0].model").value(result.get(0).getModel()))
                .andExpect(jsonPath("$[0].color").value(result.get(0).getColor()));

    }

    @Test
    void findById() throws Exception {
        Parking parking = ParkingFactory.buildParking();
        when(parkingService.findById(1L)).thenReturn(parking);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/parking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.license").value(parking.getLicense()))
                .andExpect(jsonPath("$.state").value(parking.getState()))
                .andExpect(jsonPath("$.model").value(parking.getModel()))
                .andExpect(jsonPath("$.color").value(parking.getColor()));
    }

    @Test
    void save() throws Exception {
        ParkingRequest request = new ParkingRequest("CNH-0000", "SP", "Gol", "Black");
        Parking parking = new Parking();
        parking.setLicense(request.getLicense());
        parking.setState(request.getState());
        parking.setModel(request.getModel());
        parking.setColor(request.getColor());
        Parking savedParking = ParkingFactory.buildParking();
        when(parkingService.save(parking)).thenReturn(savedParking);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/parking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.license").value(parking.getLicense()))
                .andExpect(jsonPath("$.state").value(parking.getState()))
                .andExpect(jsonPath("$.model").value(parking.getModel()))
                .andExpect(jsonPath("$.color").value(parking.getColor()));
    }

    @Test
    void update() throws Exception {
        ParkingRequest request = new ParkingRequest("CNH-0000", "SP", "Gol", "Black");
        Parking parking = new Parking();
        parking.setLicense(request.getLicense());
        parking.setState(request.getState());
        parking.setModel(request.getModel());
        parking.setColor(request.getColor());
        Parking savedParking = ParkingFactory.buildParking();
        when(parkingService.update(1L, parking)).thenReturn(savedParking);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/parking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.license").value(parking.getLicense()))
                .andExpect(jsonPath("$.state").value(parking.getState()))
                .andExpect(jsonPath("$.model").value(parking.getModel()))
                .andExpect(jsonPath("$.color").value(parking.getColor()));
    }

    @Test
    void exit() throws Exception {
        Parking exitParking = ParkingFactory.buildParking();
        exitParking.setExitDate(exitParking.getEntryDate().plusMinutes(50L));
        exitParking.setBill(5D);
        when(parkingService.checkOut(1L)).thenReturn(exitParking);
        this.mockMvc
                .perform(MockMvcRequestBuilders.patch("/parking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.license").value(exitParking.getLicense()))
                .andExpect(jsonPath("$.state").value(exitParking.getState()))
                .andExpect(jsonPath("$.model").value(exitParking.getModel()))
                .andExpect(jsonPath("$.color").value(exitParking.getColor()));
    }

    @Test
    void delete() throws Exception {
        Parking savedParking = ParkingFactory.buildParking();
        when(parkingService.delete(1L)).thenReturn(true);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/parking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}