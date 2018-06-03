package com.storageapp.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.storageapp.model.MariaDB;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class used for communication with client and to send him information.
 */

@RestController
@RequestMapping("/server")
public class PasswordStorageAppController {
    String login, pass;

    /**
     * Method used for login in client
     * @param LoginDto String map containing client login and password
     * @return http communication code
     */
    @RequestMapping(value = "/login", headers = "Accept=application/json", method = RequestMethod.POST)
    public int login(@RequestBody Map<String, String> LoginDto) {




        login = LoginDto.get("login");
        pass = LoginDto.get("password");
        if(MariaDB.IsAUser(login, pass))
        {
            return 200;
        } else {
            return 401;
        }
    }

    /**
     * Method used for registering user
     * @param LoginDto String map containing client login and password
     * @return http communication code
     */
    @RequestMapping(value = "/register", headers = "Accept=application/json", method = RequestMethod.POST)
    public int register(@RequestBody Map<String, String> LoginDto) {
        if(MariaDB.CheckLogin(LoginDto.get("login")))
            return 409;
        if(MariaDB.RegisterUser(LoginDto.get("login"),LoginDto.get("password")))
            return 200;
        else
            return 403;
    }

    /**
     * Method used for receiving passwords from client and sending them to data base
     * @param str string in json format with all passwords
     * @return http communication code
     */
    @RequestMapping(value = "/password", headers = "Accept=application/json", method = RequestMethod.POST)
    public int password(@RequestBody String str)  {

        Gson gson = new Gson();
        if(str.equals("null"))
        {
            MariaDB.DropPasswords(login, pass);
            return 200;
        }
        else
        {
            JsonArray javaArrayListFromGSON = gson.fromJson(str, JsonArray.class);
            List<JsonObject> list = new ArrayList<JsonObject>();
            for(int i=0;i<javaArrayListFromGSON.size();i++)
            {
                JsonObject json = javaArrayListFromGSON.get(i).getAsJsonObject();
                list.add(json);
            }
            MariaDB.DropPasswords(login, pass);
            for(int i =0;i< list.size();i++)
            {
                MariaDB.AddPassword(login, pass, list.get(i).get("password").getAsString(), list.get(i).get("domain").getAsString(), list.get(i).get("login").getAsString());
            }
            login = "";
            pass = "";
            return 200;
        }
    }

    /**
     * Method used for sending passwords to client
     * @return string with password
     */
    @RequestMapping(value = "/getpasswords", method = RequestMethod.GET)
    public String logout()  {
        List<    Map< String, Object >    > lista = MariaDB.ListPasswords(login, pass);
        Gson gson = new Gson();
        String json = gson.toJson(lista);
        return json;
    }
}


