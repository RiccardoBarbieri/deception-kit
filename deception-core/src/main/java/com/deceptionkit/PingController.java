package com.deceptionkit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }

}
