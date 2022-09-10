package br.com.teteuser.cloudparking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_parking")
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 8)
    private String license;

    @Column(length = 2)
    private String state;

    @Column(length = 50)
    private String model;

    @Column(length = 50)
    private String color;

    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private Double bill;



}
