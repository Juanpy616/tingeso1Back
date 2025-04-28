package com.tgs.JPkarts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Entity
@Table(name = "analyitics")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AnalyticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    Month month;
    int year;

    Long tenMins = 0L;
    Long fifteenMins = 0L;
    Long twentyMins = 0L;

    Long smallGroup = 0L;
    Long mediumGroup = 0L;
    Long bigGroup = 0L;
    Long veryBigGroup = 0L;
}
