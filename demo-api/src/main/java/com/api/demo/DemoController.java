package com.demo;

import com.deceptionkit.response.model.SimpleResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class DemoController {

    public DemoController() {
        super();
    }

    @GetMapping("/")
    @ResponseBody
    public SimpleResponse homePage() {
        return new SimpleResponse(200, "message");
    }

    @GetMapping("/infos")
    @ResponseBody
    public SimpleResponse infos(Principal principal, Model model) {
        return new SimpleResponse(200, principal.getName());
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

}
