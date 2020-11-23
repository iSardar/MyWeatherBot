package pl.weatherbot.weather;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.weatherbot.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import com.vdurmont.emoji.EmojiParser;

public class Weather {
    public static String getWeather(String city, Model model) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=cd0ad9ddd505d5069f36896e8ddbe8df");

        Scanner in = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (in.hasNext()) {
            result.append(in.nextLine());
        }

        JSONObject object = new JSONObject(result.toString());
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject objects = getArray.getJSONObject(i);
            model.setIcon((String) objects.get("icon"));
            model.setMain((String) objects.get("main"));
        }

        return EmojiParser.parseToUnicode(":city_sunrise:City: " + model.getName() + "\n" +
                "\uD83C\uDF21️Temperature: " + model.getTemp() + "°C" + "\n" +
                ":droplet:Humidity: " + model.getHumidity() + "%" + "\n" +
                ":partly_sunny:Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + ".png");

    }
}
