package com.geo.bot;


import com.geo.dao.PersonRepositoty;
import com.geo.model.Picture;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class CliStarter implements CommandLineRunner {
    final static Logger log = Logger.getLogger(CliStarter.class);

    public static Map<String, Picture> pics = new HashMap<>();

    @Autowired
    PersonRepositoty personRepositoty;

    @Value("${bot.pics}")
    private String patch;

    @Override
    public void run(String... args) throws Exception {

// prepare Persons
        personRepositoty.findAll().stream().forEach(p -> {
            Bot.mapPersons.put(p.getId(), p);
        });
        System.out.println("Persons -> " + Bot.mapPersons.size());

//prepare pictures

        Files.walk(Paths.get(patch))
                .filter(f -> f.getFileName().toString().endsWith(".jpg"))
                .forEach(f -> {

                            try {
                                pics.put(f.getFileName().toString(),
                                        Picture.builder()
                                                .name(f.getFileName().toString())
                                                .picture(new FileInputStream(f.toFile()).readAllBytes())
                                                .build());

                            } catch (FileNotFoundException e) {
                                log.info(e.toString());
                            } catch (IOException e) {
                                log.info(e.toString());
                            }

                        }
                );








    }



}
