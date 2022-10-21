package com.example.JWT.api;


import com.example.JWT.model.domain.AppUser;
import com.example.JWT.model.service.impl.AppServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppServiceImpl appService;

    @GetMapping("/players")
    public ResponseEntity<?> getResults(){
        try {
            return ResponseEntity.ok().body(appService.getRankingUsers());
        }catch(Exception exc){
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @PostMapping("/players")
    public ResponseEntity<?> createUSer(@RequestBody AppUser appUser){
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/players").toUriString());
            AppUser created = appService.addUser(appUser);
            return ResponseEntity.created(uri).body(created);
        }catch(Exception exc){
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @PutMapping("/players")
    public ResponseEntity<?> modifyAppUser(@RequestBody AppUser appUser){
        try {
            return ResponseEntity.accepted().body(appService.modifyUser(appUser));
        }catch (Exception exc){
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @GetMapping("/players/ranking")
    public ResponseEntity<?> getRankingTotalResum(){
        try{
            return ResponseEntity.ok().body(appService.getResultatsTotals());
        }catch (Exception exc){
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @GetMapping("/players/ranking/looser")
    public ResponseEntity<?> getLooser(){
        try{
            return ResponseEntity.ok().body(appService.getLooser());
        }catch (Exception exc){
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @GetMapping("/players/ranking/winner")
    public ResponseEntity<?> getWinner(){
        try{
            AppUser winner = appService.getWinner();
            return ResponseEntity.ok().body(winner);
        }catch (Exception exc){
            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @GetMapping("/players/{id}/games")
    public ResponseEntity<?> llistatDeJugades(@PathVariable("id") String id){
        try{
            AppUser appUser = appService.getUserById(id);
            if (appUser == null){
                return ResponseEntity.badRequest().body("There is no user with this id");
            }

            ArrayList<Integer[]> resultat = appUser.getHistoricJugadesFetes();
            if (resultat == null){
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(resultat);
        }catch (Exception exc){

            return ResponseEntity.internalServerError().body("There was an error in the server");
        }
    }

    @PostMapping("/players/{id}/games")
    public ResponseEntity<?> appUserMove(@PathVariable("id") String id){
        Integer[] moveResult = null;
        AppUser appUser = null;
        try{
            appUser = appService.getUserById(id);
            if (appUser == null){
                return ResponseEntity.badRequest().body("There is no user with this id");
            }

            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equalsIgnoreCase(appUser.getUserName())) {
                moveResult = new Integer[2];
                moveResult[0] = new Random().nextInt(6) + 1;
                moveResult[1] = new Random().nextInt(6) + 1;

                appUser.addOneToHistoricDeJugadesFetes(moveResult);

                if (moveResult[0] + moveResult[1] == 7) {
                    appUser.afegirPartidaGuanyada();
                }

                appUser.afegirPartidaFeta();
                appService.modifyUser(appUser);

                return ResponseEntity.ok().body(moveResult);
            }else {
                return ResponseEntity.badRequest().body("You are not allowed to make this operation");
            }
        }catch(Exception exc){
            return ResponseEntity.internalServerError().body("There has been an internal server error");
        }


    }


    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<?> deleteUserResults(@PathVariable("id") String id){
        try {
            AppUser appUser = appService.getUserById(id);
            if (appUser == null) {
                return ResponseEntity.badRequest().body("There is no user with this id");
            }

            return ResponseEntity.accepted().body(appService.deleteUserMoves(appUser));
        }catch(Exception exc){
            return ResponseEntity.internalServerError().body("There was an internal server error");
        }
    }


}
