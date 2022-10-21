package com.example.JWT.model.service.impl;

import com.example.JWT.model.domain.AppUser;
import com.example.JWT.model.repository.AppUserRepository;
import com.example.JWT.model.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class AppServiceImpl implements AppService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public AppUser addUser(AppUser appUser) {
        AppUser appUserNew = new AppUser();
        log.info("Saving new user {} to the database", appUser.getName());
        appUserNew.setName(appUser.getName() != null ? appUser.getName() : "Anonym");
        appUserNew.setUserName(appUser.getUserName());
        appUserNew.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserNew.setHistoricJugadesFetes(appUser.getHistoricJugadesFetes() != null ? appUser.getHistoricJugadesFetes() : new ArrayList<Integer[]>());
        appUserNew.setNombreDePartidesFetes(appUser.getNombreDePartidesFetes() != null ? appUser.getNombreDePartidesFetes() : 0);
        appUserNew.setJugadesGuanyades(appUser.getJugadesGuanyades() != null ? appUser.getJugadesGuanyades() : 0);


        return appUserRepository.save(appUserNew);
    }

    @Override
    public AppUser getUser(AppUser appUser) {
        return appUserRepository.findByUserName(appUser.getUserName());
    }

    @Override
    public AppUser modifyUser(AppUser appUser) {
        AppUser New = new AppUser();
        log.info("Saving modified user {} to the database", appUser.getName());
        New.setId(appUser.getId());
        New.setName(appUser.getName() != null ? appUser.getName() : "Anonym");
        New.setUserName(appUser.getUserName());
        New.setPassword(passwordEncoder.encode(appUser.getPassword()));
        New.setHistoricJugadesFetes(appUser.getHistoricJugadesFetes() != null ? appUser.getHistoricJugadesFetes() : new ArrayList<Integer[]>());
        New.setNombreDePartidesFetes(appUser.getNombreDePartidesFetes() != null ? appUser.getNombreDePartidesFetes() : 0);
        New.setJugadesGuanyades(appUser.getJugadesGuanyades() != null ? appUser.getJugadesGuanyades() : 0);


        return appUserRepository.save(New);
    }

    @Override
    public AppUser deleteUserMoves(AppUser appUser) {
        ArrayList<Integer[]> resultatsAZero = new ArrayList<Integer[]>();
        AppUser modifiedAppUSer = appUserRepository.findByUserName(appUser.getUserName());
        modifiedAppUSer.setHistoricJugadesFetes(resultatsAZero);
        return appUserRepository.save(modifiedAppUSer);
    }

    @Override
    public Double getResultatsTotals() {
        Double guanyades = appUserRepository.findAll().stream().mapToDouble(AppUser::getJugadesGuanyades).sum();
        Double fetes = appUserRepository.findAll().stream().mapToDouble(AppUser::getNombreDePartidesFetes).sum();

        return guanyades/fetes;
    }

    @Override
    public AppUser getLooser() {
        List<AppUser> tots = appUserRepository.findAll();
        Double aCercar = tots.stream().mapToDouble( x -> x.getJugadesGuanyades() / x.getNombreDePartidesFetes()).max().getAsDouble();
        Iterator<AppUser> iterator = tots.iterator();
        int i = 0;
        boolean trobat = false;
        AppUser winner = null;
        while(iterator.hasNext() && trobat == false){
            AppUser next = iterator.next();
            if(next.getJugadesGuanyades() / next.getNombreDePartidesFetes() == aCercar){
                trobat = true;
                winner = next;
            }
        }
        return winner;
    }

    @Override
    public AppUser getWinner() {
        List<AppUser> tots = appUserRepository.findAll();
        Double aCercar = tots.stream().mapToDouble( x -> x.getJugadesGuanyades() / x.getNombreDePartidesFetes()).max().getAsDouble();
        Iterator<AppUser> iterator = tots.iterator();
        int i = 0;
        boolean trobat = false;
        AppUser winner = null;
        while(iterator.hasNext() && trobat == false){
            AppUser next = iterator.next();
            if(next.getJugadesGuanyades() / next.getNombreDePartidesFetes() == aCercar){
                trobat = true;
                winner = next;
            }
        }
        return winner;
    }

    @Override
    public Map<String, Float> getRankingUsers() {
        List<AppUser> listAppUsers = appUserRepository.findAll();
        Map<String, Float> toReturn = null;
        Map<String, Float> inCaseThereArent = null;
        if (listAppUsers != null) {
            toReturn = listAppUsers.stream().collect(Collectors.toMap(AppUser::getName, AppUser::getPercentatgeExit));
            toReturn = toReturn.entrySet().stream().sorted(Map.Entry.<String, Float>comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            inCaseThereArent = new HashMap<>();
        }
        inCaseThereArent.put("no-user", 0.0f);
        return toReturn != null ? toReturn : inCaseThereArent;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUserName(username);
        if(appUser == null){
            log.info("Username not found in the database");
            throw new UsernameNotFoundException("The user was not found in the database");
        }else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            return new User(appUser.getUserName(), appUser.getPassword(), authorities);
        }
    }

    @Override
    public AppUser getUserById(String id){
        return appUserRepository.findById(id).get();
    }

}
