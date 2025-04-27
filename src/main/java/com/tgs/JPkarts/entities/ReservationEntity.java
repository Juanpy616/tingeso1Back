package com.tgs.JPkarts.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservationEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private int duration;//Puede ser 30, 35 o 40
    private LocalDate date;//Fecha y hora de la reserva
    private LocalTime startTime;
    private LocalTime endTime;
    private int quantity;//Cantidad de personas que reservan
    private String clientName;//Nombre de quien paga
}