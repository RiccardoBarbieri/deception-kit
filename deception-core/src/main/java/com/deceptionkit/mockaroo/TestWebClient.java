package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.exchange.MockarooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestWebClient {

    public MockarooService mockarooService;

    @GetMapping("/types")
    @ResponseBody
    public String temp() {
        return mockarooService.getTypes();
    }

    @PostMapping(value = "/datasets", consumes = {"text/csv", "text/plain"})
    @ResponseBody
    public String createDataset(String name, String filename) {
        return mockarooService.createDataset(name, filename, "____TODO____REMOVETHIS");
    }

    @Autowired
    public void setMockarooTypes(MockarooService mockarooService) {
        this.mockarooService = mockarooService;
    }
}
