package com.example.JWT.model.repository;


import com.example.JWT.model.domain.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {

    public AppUser findByUserName(String username);
}
