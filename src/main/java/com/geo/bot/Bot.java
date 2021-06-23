package com.geo.bot;


import com.geo.dao.PersonRepositoty;
import com.geo.enumerators.UserRoles;
import com.geo.keys.Keys0;
import com.geo.keys.KeysGallery;
import com.geo.keys.KeysOtzyv;
import com.geo.keys.KeysUslugi;
import com.geo.model.Person;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

    final private String BACK = "⬅️  Back";
    final private String NEXT = "Next ➡️";
    final private String INDEX_OUT_OF_RANGE = "Requested index is out of range!";


    @Autowired
    PersonRepositoty personRepositoty;

//emoji
//    private String chat = EmojiParser.parseToUnicode(":fire: Чат" );
//    private String gallery = EmojiParser.parseToUnicode(":fire: Галлерея ");
//    private String services = EmojiParser.parseToUnicode(":smile: Услуги");


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
            SendMessage message = new SendMessage();

                if(update.hasMessage()) {
                    message.setChatId(update.getMessage().getChatId());
                    HashMap<String, Callable<SendMessage>> map = new HashMap<String, Callable<SendMessage>>() {
                        {
                            put("/start", () -> {
                                return methodStart(update, message);
                            });

                            put("В начало", () -> {
                                return methodStart(update, message);
                            });
                            put("getPrivilegies", () -> {
                                return methodUpdatePrivilegies(update, message);
                            });
                            put(EmojiParser.parseToUnicode(":hugging: Услуги"), () -> {
                                return methodService(update, message);
                            });
//                            put("Галерея", () -> {
//                                return methodGallery(update, message);
//                            });
                            put(EmojiParser.parseToUnicode(":purple_heart: Галлерея"), () -> {
                                return methodPics(update, message);
                            });
                            put(EmojiParser.parseToUnicode(":fire: Скидки и предложения"), () -> {
                                return methodStart(update, message);
                            });
                            put(EmojiParser.parseToUnicode(":thumbsup: Отзывы"), () -> {
                                return methodOtzyv(update, message);
                            });



                        }
                    };
                    try {
                        sendAnswer(map.get(update.getMessage().getText()).call());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else if (update.hasCallbackQuery()) {
                    if (update.hasCallbackQuery()) {
                        CallbackQuery callbackquery = update.getCallbackQuery();
                        String[] data = callbackquery.getData().split(":");

                        for (String str : data) {
                            System.out.println(str);
                        }

                        int index = Integer.parseInt(data[2]);

                        if (data[0].equals("gallery")) {

                            InlineKeyboardMarkup markup = null;

                            if (data[1].equals("back")) {
                                markup = this.getGalleryView(Integer.parseInt(data[2]), 1);
                                if (index > 0) {
                                    index--;
                                }
                            } else if (data[1].equals("next")) {
                                markup = this.getGalleryView(Integer.parseInt(data[2]), 2);
                                if (index < DataStarter.urls.size() - 1) {
                                    index++;
                                }
                            } else if (data[1].equals("text")) {
                                try {
                                    this.sendAnswerCallbackQuery("Пожалуйста, свяжитесь с нами и забронируйте посещение", false, callbackquery);
                                } catch (TelegramApiException exception) {
                                    exception.printStackTrace();
                                }
                            }

                            if (markup == null) {
                                try {
                                    this.sendAnswerCallbackQuery(INDEX_OUT_OF_RANGE, false, callbackquery);
                                } catch (TelegramApiException exception) {
                                    exception.printStackTrace();
                                }
                            } else {

                                EditMessageText editMarkup = new EditMessageText();
                                editMarkup.setChatId(callbackquery.getMessage().getChatId().toString());
                                editMarkup.setInlineMessageId(callbackquery.getInlineMessageId());
                                editMarkup.setText("(" + DataStarter.urls.get(index)[1] + ")\n" + DataStarter.urls.get(index)[3]);
//                    editMarkup.enableMarkdown(true);
                                editMarkup.setMessageId(callbackquery.getMessage().getMessageId());
                                editMarkup.setReplyMarkup(markup);
                                try {
                                    execute(editMarkup);
                                } catch (TelegramApiException exception) {
                                    exception.printStackTrace();
                                }

                            }


                        }


                    }


                }

                //general commands

            }

    private SendMessage methodUpdatePrivilegies(Update update, SendMessage message) {
        //keys
        keys.clear();
        Keys0 arr[] = Keys0.values();
        for (Keys0 i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //logic
        personRepositoty.updateRobotRequestStatus(update.getMessage().getChatId(), UserRoles.ADMIN);


        answer.setLength(0);
        answer.append("privilegies updated success");
        message.setText(answer.toString());
        return message;

    }


    private SendMessage methodOtzyv(Update update, SendMessage message) {
        keys.clear();
        KeysOtzyv arr[] = KeysOtzyv.values();
        for (KeysOtzyv i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);


        answer.setLength(0);
        answer.append("Пожалуйста, оставьте отзыв от нашей компании:\n");
        message.setText(answer.toString());

        //inlineKBD
        message.enableMarkdown(true);
        message.setReplyMarkup(this.getOtzyvyKeys());


        return message;

    }

    private SendMessage methodPics(Update update, SendMessage message) {
        keys.clear();
        KeysGallery arr[] = KeysGallery.values();
        for (KeysGallery i : arr) {
            keys.add(i.toString());
        }

        addKeys(message, keys);
        message.setText("(" + DataStarter.urls.get(0)[1] + ")\n" + DataStarter.urls.get(0)[3]);
        message.enableMarkdown(true);
        message.setReplyMarkup(this.getGalleryView(0, -1));
        message.setChatId(update.getMessage().getChatId());
        return message;
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
                currentPerson.setUserRoles(UserRoles.USER);


                mapPersons.put(update.getMessage().getChatId(), currentPerson);
                personRepositoty.save(currentPerson);
                log.info("Save new user " + currentPerson.getId());


            }

            //Prepare first message
            answer.setLength(0);
            answer.append("Добро пожаловать в бот SUP туристического клуба 'XXX' \n");
            answer.append("У нас вы можете хорошо провести время\n");
            answer.append("Наши координаты -> " + "https://www.google.com/search?q=56.625099470791916%2C+60.368910812978946\n");
            answer.append("Телефон для бронирования -> +79221878899");
            message.setText(answer.toString());
            return message;

        }

//Gallery Keyboard
    private InlineKeyboardMarkup getGalleryView(int index, int action){
        /*
         * action = 1 -> back
         * action = 2 -> next
         * action = -1 -> nothing
         */

        if(action == 1 && index > 0){
            index--;
        }
        else if((action == 1 && index == 0)){
            return null;
        }
        else if(action == 2 && index >= DataStarter.urls.size()-1){
            return null;
        }
        else if(action == 2){
            index++;
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(DataStarter.urls.get(index)[2]).setCallbackData("gallery:text:" + index));


        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText(BACK).setCallbackData("gallery:back:" + index));
        rowInline2.add(new InlineKeyboardButton().setText(NEXT).setCallbackData("gallery:next:" + index));

//        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
//        rowInline3.add(new InlineKeyboardButton().setText("Link").setUrl(DataStarter.urls.get(index)[0]));


        rowsInline.add(rowInline);
//        rowsInline.add(rowInline3);
        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

//Otzyvy Keyboard
    private InlineKeyboardMarkup getOtzyvyKeys(){

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Google").setUrl("https://www.google.com/"));
        rowInline.add(new InlineKeyboardButton().setText("Yandex").setUrl("https://yandex.ru/"));
        rowInline.add(new InlineKeyboardButton().setText("Flamp").setUrl("https://ekaterinburg.flamp.ru/"));

//        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
//        rowInline2.add(new InlineKeyboardButton().setText(BACK).setCallbackData("gallery:back:" + index));
//        rowInline2.add(new InlineKeyboardButton().setText(NEXT).setCallbackData("gallery:next:" + index));

//        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
//        rowInline3.add(new InlineKeyboardButton().setText("Link").setUrl(DataStarter.urls.get(index)[0]));


        rowsInline.add(rowInline);
//        rowsInline.add(rowInline3);
//        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }



    private void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) throws TelegramApiException{
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        execute(answerCallbackQuery);
    }


    private SendMessage methodService(Update update, SendMessage message) {
        keys.clear();
        KeysUslugi arr[] = KeysUslugi.values();
        for (KeysUslugi i : arr) {
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

@Deprecated  //files
    private SendMessage methodGallery(Update update, SendMessage message) {
        keys.clear();
        KeysGallery arr[] = KeysGallery.values();
        for (KeysGallery i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //Photo

        DataStarter.pics.entrySet().stream().limit(5).forEach(e -> {
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
            KeyboardRow fiveRow = new KeyboardRow();




            for (String k : keys) {

                if (indexButton == 0 || indexButton ==1) {
                    firstRow.add(k);
                } else if (indexButton == 2 || indexButton ==3) {
                    secondRow.add(k);
                } else if (indexButton == 4 || indexButton ==5) {
                    thirdRow.add(k);
                }
                indexButton++;
            }

            keyboardRows.add(firstRow);
            keyboardRows.add(secondRow);
            keyboardRows.add(thirdRow);
            keyboardRows.add(fourthRow);
            keyboardRows.add(fiveRow);


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

