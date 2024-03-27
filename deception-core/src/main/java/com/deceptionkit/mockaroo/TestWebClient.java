package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.exchange.MockarooService;
import com.deceptionkit.mockaroo.exchange.model.Response;
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
    public Response createDataset(@PathVariable("name") String name, @RequestParam(value = "filename", required = false) String filename, @RequestBody String csv) {
        return mockarooService.createDataset(name, filename, csv);
    }

    @Autowired
    public void setMockarooTypes(MockarooService mockarooService) {
        this.mockarooService = mockarooService;
    }
}
