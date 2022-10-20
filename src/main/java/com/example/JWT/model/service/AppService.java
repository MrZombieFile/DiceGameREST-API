package com.example.JWT.model.service;


import com.example.JWT.model.domain.AppUser;
import java.util.Map;

public interface AppService {

    public AppUser addUser(AppUser appUser);

    public AppUser getUser(AppUser appUser);

    public AppUser modifyUser(AppUser appUser);

    public AppUser deleteUserMoves(AppUser appUser);

    public AppUser getWinner();

    public AppUser getLooser();

    public Double getResultatsTotals();

    public Map<String, Float> getRankingUsers();

    public AppUser getUserById(String id);


    }
