package com.example.JWT.api;


import com.example.JWT.model.domain.AppUser;
import com.example.JWT.model.service.impl.AppServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class AppController {
/*
* ###########################################################################
*                    Falten les responseEntities d'error i relacionades
* ###########################################################################
* */
    private final AppServiceImpl appService;

    @GetMapping("/players")
    public ResponseEntity<Map<String, Float>> getResults(){
        return ResponseEntity.ok().body(appService.getRankingUsers());
    }

    @PostMapping("/players")
    public ResponseEntity<AppUser> createUSer(@RequestBody AppUser appUser){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/players").toUriString());
        AppUser created = appService.addUser(appUser);
        System.out.println("\n\n\n\n\n " + appUser.getUserName() + appUser.getPassword() + appUser.getName());
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/players")
    public ResponseEntity<AppUser> modifyAppUser(@RequestBody AppUser appUser){
        return ResponseEntity.accepted().body(appService.modifyUser(appUser));
    }

    @GetMapping("/players/ranking")
    public ResponseEntity<Double> getRankingTotalResum(){
        return ResponseEntity.ok().body(appService.getResultatsTotals());
    }

    @GetMapping("/players/ranking/looser")
    public ResponseEntity<AppUser> getLooser(){
        return ResponseEntity.ok().body(appService.getLooser());
    }

    @GetMapping("/players/ranking/winner")
    public ResponseEntity<AppUser> getWinner(){
        return ResponseEntity.ok().body(appService.getWinner());
    }

    @GetMapping("/players/{id}/games")
    public ResponseEntity<ArrayList<Integer[]>> llistatDeJugades(@PathVariable("id") String id){
        AppUser appUser = appService.getUserById(id);
        ArrayList<Integer[]> resultat = appUser.getHistoricJugadesFetes();
        return ResponseEntity.ok().body(resultat);
    }

    @PostMapping("/players/{id}/games")
    public ResponseEntity<Integer[]> appUserMove(@PathVariable("id") String id){
        Integer[] moveResult = null;
        AppUser appUser = appService.getUserById(id);

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equalsIgnoreCase(appUser.getUserName())) {
            moveResult = new Integer[2];
            moveResult[0] = new Random().nextInt(6) + 1;
            moveResult[1] = new Random().nextInt(6) + 1;

            appUser.addOneToHistoricDeJugadesFetes(moveResult);
            //appUser.setHistoricJugadesFetes(jugadesFetes);
            //_____________________________________________________________________________________
            //appUser.setHistoricJugadesFetes(appUser.addOneToHistoricDeJugadesFetes(moveResult));
            //no se guarda l'historic de jugades fetes - creo que deberia ir en el service en vez de en el entity
            ////appService.addOneToHistoricJugadesAppUser(appUser.getUserName(), moveResult);
            //tampoco va
            //
            // no van los rankings winner looser

            if (moveResult[0] + moveResult[1] == 7){
                appUser.afegirPartidaGuanyada();
                appUser.afegirPartidaFeta();
            }else if (moveResult[0] + moveResult[1] != 7){
                appUser.afegirPartidaFeta();
            }

            appService.modifyUser(appUser);
            /*
            appService.setWinner(appUser);
            appService.setLooser(appUser);
*/
        }else {
            moveResult = new Integer[2];
            moveResult[0] = 0;
            moveResult[1] = 0;
        }
        return ResponseEntity.ok().body(moveResult);
    }


    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<AppUser> deleteUserResults(@PathVariable("id") String id){
        AppUser appUser = appService.getUserById(id);
        return ResponseEntity.accepted().body(appService.deleteUserMoves(appUser));
    }


}
