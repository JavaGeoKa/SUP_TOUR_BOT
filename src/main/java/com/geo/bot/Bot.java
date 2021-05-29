package com.geo.bot;


import com.geo.dao.PersonRepositoty;
import com.geo.keys.Keys0;
import com.geo.model.Person;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Bot extends TelegramLongPollingBot {

    final static Logger log = Logger.getLogger(Bot.class);


    @Autowired
    PersonRepositoty personRepositoty;

//keyboard
    List<String> keys = new ArrayList<>();
    public StringBuilder answer = new StringBuilder();
    SendPhoto photoOutMessage = new SendPhoto();


    public static Map<Long, Person> mapPersons = new ConcurrentHashMap<>();
    Person currentPerson = new Person();




        //Input section --------------------------------
        @SneakyThrows
        @Override
        public void onUpdateReceived(Update update) {
            long chat_id = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(chat_id);

                //general commands
                HashMap<String, Callable<SendMessage>> map = new HashMap<String, Callable<SendMessage>>() {
                    {
                        put("/start", () -> {
                            return methodStart(update, message);
                        });

                        put("В начало", () -> {
                            return methodStart(update, message);
                        });
                        put("Услуги", () -> {
                            return methodService(update, message);
                        });
                        put("Галерея", () -> {
                            return methodGallery(update, message);
                        });



                    }
                };





                try {
                    sendAnswer(map.get(update.getMessage().getText()).call());


                } catch (Exception e) {

                    e.printStackTrace();



                }


            }




//  METHODS----------------------------------------------------------------------------

// /start
        private SendMessage methodStart(Update update, SendMessage message) {
        //keys
        keys.clear();
        Keys0 arr[] = Keys0.values();
        for (Keys0 i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //logic
            if (mapPersons.containsKey(update.getMessage().getChatId())) {
                currentPerson = mapPersons.get(update.getMessage().getChatId());
            } else {
                currentPerson = new Person();
                currentPerson.setRequests(0);
                currentPerson.setId(update.getMessage().getChatId());
                currentPerson.setFirstName(update.getMessage().getFrom().getFirstName() == null ?
                        "undefined" : update.getMessage().getFrom().getFirstName());
                currentPerson.setLastName(update.getMessage().getFrom().getLastName() == null ?
                        "undefined" : update.getMessage().getFrom().getLastName());
                currentPerson.setConnectTime(LocalDateTime.now());


                mapPersons.put(update.getMessage().getChatId(), currentPerson);
                personRepositoty.save(currentPerson);
                log.info("Save new user " + currentPerson.getId());


            }

            //Prepare first message
            answer.setLength(0);
            answer.append("Добро пожаловать в бот XXX SUP STATION\n");


            message.setText(answer.toString());
            return message;

        }


    private SendMessage methodService(Update update, SendMessage message) {
        keys.clear();
        Keys0 arr[] = Keys0.values();
        for (Keys0 i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //Prepare first message
        answer.setLength(0);
        answer.append("Аренда SUP 1 час 700 руб.\n");
        answer.append("Аренда беседки 1 час 2000 руб.\n");
        answer.append("Аренда мангала 1 час 500 руб.\n");
        answer.append("Аренда кресло-мешок 1 час 500 руб.\n");
        answer.append("парковка 300 руб.\n");
        answer.append("Баня 1 час 3000 руб.\n");
        answer.append("Услуга парения 1 час 3000 руб.\n");
        answer.append("Кальян 1000 руб.\n");

        message.setText(answer.toString());
        return message;


    }

    private SendMessage methodGallery(Update update, SendMessage message) {
        keys.clear();
        Keys0 arr[] = Keys0.values();
        for (Keys0 i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //Photo

        CliStarter.pics.entrySet().stream().limit(5).forEach(e -> {
            photoOutMessage.setPhoto("SUP", new ByteArrayInputStream(e.getValue().getPicture()));
            photoOutMessage.setChatId(message.getChatId());

            try {
                execute(photoOutMessage);
            } catch (TelegramApiException telegramApiException) {
                log.info(telegramApiException);
            }

        });

                    //Prepare first message
        answer.setLength(0);
        answer.append("Галерея SUP TOUR EKB\n");
        message.setText(answer.toString());
        return message;


    }


// "SIMPLE"

//Save Current Parameters
//    private void saveCurrentParameters(Long chatId) {
//
//        currentPerson.setLastUseTime(LocalDateTime.now());
//        Integer i = currentPerson.getRequests();
//        currentPerson.setRequests(i);
//
//        personRepositoty.updateLastRequestTime(chatId, currentPerson.getLastUseTime());
//        personRepositoty.updateRequests(chatId, currentPerson.getRequests());
//
//
//    }











    // KEYS ------------------------------------------------------------------------
        private void addKeys(SendMessage message, List<String> keys) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            message.setReplyMarkup(replyKeyboardMarkup);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);

//        //Create list keyboard rows
            List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
//
//        //Calculate keyboard rows for keys
            Integer indexButton = 0;
            KeyboardRow firstRow = new KeyboardRow();
            KeyboardRow secondRow = new KeyboardRow();
            KeyboardRow thirdRow = new KeyboardRow();
            KeyboardRow fourthRow = new KeyboardRow();




            for (String k : keys) {

                if (indexButton == 0) {
                    firstRow.add(k);
                } else if (indexButton == 1) {
                    secondRow.add(k);
                } else if (indexButton == 2) {
                    thirdRow.add(k);
                } else if (indexButton == 3) {
                    fourthRow.add(k);
                }
                indexButton++;
            }

            keyboardRows.add(firstRow);
            keyboardRows.add(secondRow);
            keyboardRows.add(thirdRow);
            keyboardRows.add(fourthRow);


            replyKeyboardMarkup.setKeyboard(keyboardRows);


        }

        //SendAnswer general
        private void sendAnswer(SendMessage message) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                System.out.println("===========================executeException");
            }
        }






        //Bot service data
        @Value("${bot.name}")
        private String botUsername;

        @Value("${bot.token}")
        private String botToken;

        public String getBotUsername() {
            return botUsername;
        }

        public String getBotToken() {
            return botToken;
        }





}

