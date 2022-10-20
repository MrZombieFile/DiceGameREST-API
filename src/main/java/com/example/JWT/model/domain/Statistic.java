package com.example.JWT.model.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Statistic {

    @Id
    //@GeneratedValue(strategy = AUTO)
    private String id;

    //@OneToOne
    //@JoinColumn(name = "winner_id")
    private AppUser winner;

    //@OneToOne
    //@JoinColumn(name = "looser_id")
    private AppUser looser;

    //@ManyToOne
    //@JoinColumn(name = "resultats")
    private AppUser[] resultats;

}
