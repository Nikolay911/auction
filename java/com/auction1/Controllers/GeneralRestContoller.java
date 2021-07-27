package com.auction1.Controllers;

import com.auction1.authorization.GeneralService;
import com.auction1.models.User;
import org.json.simple.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GeneralRestContoller {

    private GeneralService generalService;

    public GeneralRestContoller(GeneralService generalService) {
        this.generalService = generalService;
    }


    @GetMapping("/users")
    public JSONObject getUsers() {
        return generalService.getUsers();
    }

    @PostMapping("/user")
    public JSONObject postUser(@Nullable @RequestBody User body) throws IOException {
        return generalService.postUser(body);
    }
}
