package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.exchange.MockarooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class TestWebClient {

    public MockarooService mockarooService;

    @GetMapping("/types")
    @ResponseBody
    public String temp() {
        return mockarooService.getTypes();
    }

    @PostMapping(value = "/datasets/{name}", consumes = {"text/csv", "text/plain"})
    @ResponseBody
    public String createDataset(@PathVariable("name") String name, @RequestParam(value = "filename", required = false) String filename, @RequestBody String csv) {
        System.out.println("_-_-_-_");
//        csv.forEach(System.out::println);
        System.out.println(csv);
        System.out.println("_-_-_-_");
        return "asd";
//        return mockarooService.createDataset(name, filename, Collections.singletonList(csv));
    }

    @Autowired
    public void setMockarooTypes(MockarooService mockarooService) {
        this.mockarooService = mockarooService;
    }
}
