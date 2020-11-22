package pl.weatherbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.weatherbot.model.Model;
import pl.weatherbot.weather.Weather;

import java.io.IOException;

public class Bot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        String userName = update.getMessage().getChat().getFirstName();

        if (message != null && message.hasText()) {
            switch (message.getText()) {

                case "/start":
                    System.out.println("Request detected");
                    SendMessage sendWelcomeMessage = new SendMessage();
                    sendWelcomeMessage.setText("Hello " + userName + " welcome to the weather bot!" + "\nProvide city to obtain current weather conditions");
                    sendWelcomeMessage.setParseMode(ParseMode.MARKDOWN);
                    sendWelcomeMessage.setChatId(message.getChatId());
                    try {
                        execute(sendWelcomeMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    String city = update.getMessage().getText();
                    long chatId = update.getMessage().getChatId();
                    SendMessage sendWeather = new SendMessage();
                    sendWeather.setParseMode(ParseMode.MARKDOWN);
                    sendWeather.setChatId(chatId);
                    try {
                        sendWeather.setText(Weather.getWeather(city, model));
                    } catch (IOException e) {
                        try {
                            execute(new SendMessage(chatId, "Incorrect city, try again"));
                        } catch (TelegramApiException telegramApiException) {
                            telegramApiException.printStackTrace();
                        }
                    }
                    try {
                        execute(sendWeather);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public String getBotUsername() {
        return "KorobkaObjebkaBot";
    }

    public String getBotToken() {
        return "1255739334:AAFvz8qRJ_ZswIRMbzWBS-KvSXcSdF_V-Gc";
    }
}
