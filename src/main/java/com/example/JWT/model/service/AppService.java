package com.example.JWT.model.service;


import com.example.JWT.model.domain.AppUser;
//import com.example.JWT.model.domain.Role;
import com.example.JWT.model.domain.Statistic;

import java.util.ArrayList;
import java.util.Map;

public interface AppService {

    public AppUser addUser(AppUser appUser);

    public AppUser getUser(AppUser appUser);

    public AppUser modifyUser(AppUser appUser);

    public AppUser deleteUserMoves(AppUser appUser);

    public Statistic setWinner(AppUser appUSer);

    public Statistic setLooser(AppUser appUSer);

    public AppUser getWinner();

    public AppUser getLooser();

    public Double getResultatsTotals();

    public Map<String, Float> getRankingUsers();

    public ArrayList<Integer[]> getHistoricJugadesAppUser(AppUser appUser);

    public AppUser addOneToHistoricJugadesAppUser(String userName, Integer[] arrayDeResultats);

    public Integer addGameToUser(AppUser appUser);

    public AppUser getUserById(String id);

    //public Role addRole(Role role);

    //public AppUser addRoleToAppUSer(String role, String userName);


    }
