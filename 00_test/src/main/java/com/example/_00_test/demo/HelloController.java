package com.example._00_test.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    //localhost:8082/hello
    @RequestMapping (path = "/hello")
    public @ResponseBody
    String hello() {
        return "Hello World ;-)";
    }

    //localhost:8082/hello/Andrea
    @RequestMapping (path = "/hello/{name}")
    public @ResponseBody
    String hello(@PathVariable String name) {
        return "Hello World " + name + "!!";
    }
}
