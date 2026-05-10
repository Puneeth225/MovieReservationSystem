package com.project.movie.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.movie.reservation.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Enumerated(value = EnumType.STRING)
    private SeatStatus status;

    private double price;
    private int number;
    private String area;

    // ADD THIS RELATIONSHIP
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;
}
