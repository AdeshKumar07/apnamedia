package com.example.CRUD;


import com.example.CRUD.entity.User;
import com.example.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
// it handles incomming http requests and sends the response
// what you see in response is ultimately what is returned from the controller
// @RequestMapping gives the path for the whole controller
public class Controller {

    @Autowired
    UserService userService;

    // path inside the coontroller

    @GetMapping("")
    public String ping(){
        return "Hello World";

    }


    @GetMapping("/ping")
    public String hello(){
        return "Hello World 2 ";

    }

    @GetMapping("/json")
    public Map<String, Object> json(){


        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "Hello World");
        map.put("data", 1);
        map.put("data2", 2);
        map.put("data3", 3);
        return map;



    }


    @GetMapping("html")
    public String html(){

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Minimal Page</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    Hello World\n" +
                "  </body>\n" +
                "</html>\n" ;
    }


    @GetMapping("/test")
    User test(){
        User user = new User();
        user.setEmail("test@gmail") ;
        user.setPassword("123456");
        user.setUsername("test") ;


        return user;
    }
}
