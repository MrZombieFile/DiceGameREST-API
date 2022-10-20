package com.example.JWT.model.service.impl;

import com.example.JWT.model.domain.AppUser;
//import com.example.JWT.model.domain.Role;
import com.example.JWT.model.domain.Statistic;
import com.example.JWT.model.repository.AppUserRepository;
//import com.example.JWT.model.repository.RoleUserRepository;
import com.example.JWT.model.repository.StatisticRepository;
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
    private final StatisticRepository statisticRepository;
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

    public void afegirUnAJugadesGuanyades(AppUser appUser){
        appUser.afegirPartidaGuanyada();
        appUserRepository.save(appUser);
    }

    @Override
    public AppUser getWinner() {
        return statisticRepository.findAll().get(0).getWinner();
    }

    @Override
    public AppUser getLooser() {
        return statisticRepository.findAll().get(0).getLooser();
    }

    @Override
    public Statistic setWinner(AppUser appUser) {
        AppUser winner = statisticRepository.findAll().get(0).getWinner();
        Statistic toReturn = null;
        if (appUser.getPercentatgeExit() > winner.getPercentatgeExit()){
            Statistic statistic = statisticRepository.findAll().get(0);
            statistic.setWinner(appUser);
            toReturn = statisticRepository.save(statistic);
        }
        return toReturn;
    }

    @Override
    public Statistic setLooser(AppUser appUser) {
        Statistic toReturn = statisticRepository.findAll().get(0);
        AppUser looser = toReturn.getLooser();
        if (appUser.getPercentatgeExit() < looser.getPercentatgeExit()){
            toReturn.setLooser(appUser);
        }
        return statisticRepository.save(toReturn);


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
    public Integer addGameToUser(AppUser appUser){
        AppUser user = appUserRepository.findByUserName(appUser.getUserName());
        user.afegirPartidaFeta();
        return user.getNombreDePartidesFetes();
    }

    @Override
    public Double getResultatsTotals() {
        System.out.println(statisticRepository.findAll());
        AppUser[] resultats = statisticRepository.findAll().get(0).getResultats();
        List<Float> llistaPercentatgeExit = Arrays.stream(resultats).map(x -> x.getPercentatgeExit()).collect(Collectors.toList());

        return llistaPercentatgeExit.stream().mapToDouble(x -> x).average().getAsDouble();
    }

    @Override
    public ArrayList<Integer[]> getHistoricJugadesAppUser(AppUser appUser) {
        return appUserRepository.findByUserName(appUser.getUserName()).getHistoricJugadesFetes();
    }

    @Override
    public AppUser addOneToHistoricJugadesAppUser(String userName, Integer[] aAfegir) {
        AppUser au = appUserRepository.findByUserName(userName);
        ArrayList<Integer[]> jugadesFetes = au.getHistoricJugadesFetes();
        jugadesFetes.add(aAfegir);
        au.setHistoricJugadesFetes(jugadesFetes);

        return appUserRepository.save(au);

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
