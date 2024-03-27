package com.deceptionkit.mockaroo.exchange;

import com.deceptionkit.mockaroo.exchange.model.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/api")
public interface MockarooService {


    @GetExchange(value = "/types")
    @ResponseBody
    String getTypes();

    @PostExchange(value = "/datasets/{name}", accept = {"text/csv", "text/plain"})
    @ResponseBody
    Response createDataset(@PathVariable("name") String name, @RequestParam(value = "filename", required = false) String filename, @RequestBody String csv);

    @DeleteExchange(value = "/datasets/{name}")
    @ResponseBody
    Response deleteDataset(@PathVariable("name") String name);

    @PostExchange(value = "/generate.{format}", accept = {"text/csv", "text/plain"})
    @ResponseBody
    String generateData(@PathVariable("format") String format, @RequestBody String schema);

}
