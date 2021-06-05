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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataStarter implements CommandLineRunner {
    final static Logger log = Logger.getLogger(DataStarter.class);

    public static Map<String, Picture> pics = new HashMap<>();

    @Autowired
    PersonRepositoty personRepositoty;

    @Value("${bot.pics}")
    private String patch;

    public static ArrayList<String[]> urls = new ArrayList<>();

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

//Yoga section

            urls.add(new String[]{"https://star-wiki.ru/wiki/Bakasana", "https://i.imgur.com/uns9yPi.jpg", "Bakasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Matsyasana", "https://i.imgur.com/I6Zbm5U.jpg", "Matsyasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Mayurasana", "https://i.imgur.com/UATCHaF.jpg", "Mayurasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Salabhasana", "https://i.imgur.com/Ajfqzl6.jpg", "Salabhasana"});
            urls.add(new String[]{"https://en.wikipedia.org/wiki/Bidalasana", "https://i.imgur.com/Gh0Te8U.jpg", "Marjaryasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Bidalasana", "https://i.imgur.com/feY6xE9.jpg", "Vyaghrasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Hanumanasana", "https://i.imgur.com/1QobnsG.jpg", "Hanumanasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Garudasana#Variations", "https://i.imgur.com/DDCNdOD.jpg", "Vatayanasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Garudasana", "https://i.imgur.com/zA990JH.jpg", "Garudasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Bhujangasana", "https://i.imgur.com/2s1RQUg.jpg", "Bhujangasana"});
            urls.add(new String[]{"https://ru.wikipedia.org/wiki/%D0%A0%D0%B0%D1%81%D1%82%D1%8F%D0%B3%D0%B8%D0%B2%D0%B0%D1%8E%D1%89%D0%B8%D0%B5_%D0%B0%D1%81%D0%B0%D0%BD%D1%8B#%D0%91%D0%B0%D0%B4%D0%B4%D1%85%D0%B0_%D0%9A%D0%BE%D0%BD%D0%B0%D1%81%D0%B0%D0%BD%D0%B0%20Ustrasana%20-%3E%20https://star-wiki.ru/wiki/Ustrasana", "https://i.imgur.com/vfKHhNB.jpg", "Baddhakonasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Bakasana", "https://i.imgur.com/csioUYj.jpg", "Bakasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Bakasana", "https://i.imgur.com/uns9yPi.jpg", "Bakasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Ustrasana", "https://i.imgur.com/514zKsW.jpg", "Ustrasana"});
            urls.add(new String[]{"https://www.oum.ru/yoga/asany/sobaka-mordoy-vniz/", "https://i.imgur.com/Xjvp8vd.jpg", "Adhomukhasvanasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Kapotasana", "https://i.imgur.com/bU3vL7u.jpg", "Bitilasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Balasana#Variations", "https://i.imgur.com/X3PHA7N.jpg", "Sasangasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Koundinyasana#Variations", "https://i.imgur.com/V2jKK7Q.jpg", "Galavasana"});
            urls.add(new String[]{"https://www.oum.ru/encyclopedia/asanas/%D0%90/ardha-pincha-mayurasana", "https://i.imgur.com/5TQ12UI.jpg", "Ardjapinchamaryasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Hanumanasana", "https://i.imgur.com/ImfTeud.jpg", "Hanumanasana"});
            urls.add(new String[]{"https://star-wiki.ru/wiki/Tittibhasana", "https://i.imgur.com/oCI04Dq.jpg", "Tittibhasana"});








    }



}
