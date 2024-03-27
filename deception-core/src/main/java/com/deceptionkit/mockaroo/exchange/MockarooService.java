package com.deceptionkit.mockaroo.exchange;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/api")
public interface MockarooService {


    @GetExchange(value = "/types")
    String getTypes();

    @PostExchange(value = "/datasets/{name}", accept = {"text/csv", "text/plain"})
    String createDataset(@PathVariable("name") String name, @RequestParam(value = "filename", required = false) String filename, @RequestBody String test);

}
