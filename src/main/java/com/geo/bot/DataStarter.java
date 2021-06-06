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

//prepare pictures from disk

//        Files.walk(Paths.get(patch))
//                .filter(f -> f.getFileName().toString().endsWith(".jpg"))
//                .forEach(f -> {
//
//                            try {
//                                pics.put(f.getFileName().toString(),
//                                        Picture.builder()
//                                                .name(f.getFileName().toString())
//                                                .picture(new FileInputStream(f.toFile()).readAllBytes())
//                                                .build());
//
//                            } catch (FileNotFoundException e) {
//                                log.info(e.toString());
//                            } catch (IOException e) {
//                                log.info(e.toString());
//                            }
//
//                        }
//                );

//Yoga section

            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/MSdc46O.jpg", "Sup - туры на реке Чусовая","Спортивные и развлекательные туры по живописной реке Чусовая в близи Екатеринбурга"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/7BvYfMy.jpg", "Активный отдых","В дружеской компании, яркие эмоции, отличное времяпровождение"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/qN86KDM.jpg", "Уникальная и загадочная уральская природа","Чистая река, свежий воздух, "});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/tUtdMq8.jpg", "Река Чусовая","Живописность берегов Чусовой, наличие многочисленных достопримечательностей и многочисленные упоминания о ней в художественной литературе сделали её популярным туристическим объектом Урала"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/VHxcSrT.jpg", "Профессиональное оборудование","Профессиональные SUP премиум класса от мировых производителей"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/H5PjOiE.jpg", "Обучение","Инструктаж и профессиональное обучение позволит вам удержаться на плаву даже в сильный ветер и большие волны"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/j6RNx1X.jpg", "Дружественная атмосфера","Позитивные люди, хорошая музыка, развлечения на берегу и на воде"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/CCGV2jF.jpg", "Гамаки","Отдых в гамаках на берегу теки в тихом приятном месте на свежем воздухе"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/seDN00v.jpg", "Пикники на природе","Вы можете заказать плов, шашлыки, люля-кебаб или другое любимое юл.до на ваш вкус"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/iSDtuzq.jpg", "Позитивное настроение","Чувство отдыха и позитивного настроения надолго останется у Вас после такого отдыха"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/wpd1zPZ.jpg", "Семейный отдых","SUP серфинг прекрасно подходит для людей разного возраста"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/0cGwyPw.jpg", "Прогулки по реке от 40 км","Речные прогулки в неизведанные места, с возможностью высадится в любой точке"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/Jy0NAQW.jpg", "Занятия йогой на природе","Индивидуальные и групповые занятия йогой на природе с профессиональными тренерами по расписанию"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/b1MV61P.jpg", "Баня","В конце Вас ждет баня с профессиональным парением и купель с горячей водой на свежем воздухе на берегу реки"});
            urls.add(new String[]{"https://supbereg.ru/", "https://i.imgur.com/HvWEEhc.jpg", "Зимние забавы", "Баня, рыбалка, прогулки на снегоходе, катание на сноуборде за снегоходом"});








    }



}
