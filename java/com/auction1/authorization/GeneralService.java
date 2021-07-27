package com.auction1.authorization;

import com.auction1.models.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class GeneralService {

    public JSONObject getUsers() {

        JSONParser parser = new JSONParser();
        JSONObject arrJson = null;
        try (FileReader reader = new FileReader("users.json")) {

            arrJson = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            System.out.println("Parsing error" + e.toString());
        }
        return arrJson;
    }

    public JSONObject postUser(User user) throws IOException {

        JSONArray jsonArray = (JSONArray) getUsers().get("users");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", user.getLogin());
        jsonObject.put("password", user.getPassword());

        jsonArray.add(jsonObject);

        JSONObject jsonName = new JSONObject();
        jsonName.put("users", jsonArray);

        File file = new File("users.json");
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(jsonName.toJSONString());
        printWriter.close();

        return jsonName;
    }

}
