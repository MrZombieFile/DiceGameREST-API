package com.example.JWT.model.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity @Data @AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id //@GeneratedValue(strategy = AUTO)
    private String id;
    private String name;
    private String userName;
    private String password;
    private ArrayList<Integer[]> historicJugadesFetes = new ArrayList<Integer[]>();
    private Integer nombreDePartidesFetes;
    private Integer jugadesGuanyades;




    public void afegirPartidaFeta(){
        this.nombreDePartidesFetes += 1;
    }

    public void afegirPartidaGuanyada(){
        this.jugadesGuanyades += 1;
    }

    public ArrayList<Integer[]> addOneToHistoricDeJugadesFetes(Integer[] aAfegir){
        this.historicJugadesFetes.add(aAfegir);
        return this.historicJugadesFetes;
    }

    public Float getPercentatgeExit(){
        return this.nombreDePartidesFetes != 0 ? (float) (this.jugadesGuanyades / this.nombreDePartidesFetes * 100) : 0;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Integer[]> getHistoricJugadesFetes() {
        return historicJugadesFetes;
    }

    public void setHistoricJugadesFetes(ArrayList<Integer[]> historicJugadesFetes) {
        this.historicJugadesFetes = historicJugadesFetes;
    }

    public Integer getNombreDePartidesFetes() {
        return nombreDePartidesFetes;
    }

    public void setNombreDePartidesFetes(Integer nombreDePartidesFetes) {
        this.nombreDePartidesFetes = nombreDePartidesFetes;
    }

    public Integer getJugadesGuanyades() {
        return jugadesGuanyades;
    }

    public void setJugadesGuanyades(Integer jugadesGuanyades) {
        this.jugadesGuanyades = jugadesGuanyades;
    }

}
