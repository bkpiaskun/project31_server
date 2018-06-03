package com.storageapp;

import com.storageapp.model.MariaDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class Application {
    public static void main(String[] args){
        MariaDB.Configure();
        SpringApplication.run(Application.class, args);
    }
}
