package com.deceptionkit;

import com.deceptionkit.mockaroo.CsvType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.IOException;
import java.util.List;

public class TempYaml {

    public static void main(String[] args) {
        CsvMapper csvMapper = new CsvMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MappingIterator<Object> list = csvMapper.readerFor(CsvType.class).readValues("name,value,checked\ntrue,value1,true\ntrue,value2,false\ntrue,value3,true\ntrue,value4,false\ntrue,value5,true\ntrue,value6,false\ntrue,value7,true\n");
            List<Object> objects = list.readAll();
            objects.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
