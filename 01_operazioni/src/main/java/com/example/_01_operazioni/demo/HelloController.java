package com.example._01_operazioni.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//localhost:8082/??

@Controller
public class HelloController {

    @RequestMapping (path = "/somma/{num1}/{num2}")
    public @ResponseBody
    String somma(@PathVariable float num1, @PathVariable float num2) {
        float somma = num1 + num2;
        return "Somma: " + somma;
    }

    @RequestMapping (path = "/sottrazione/{num1}/{num2}")
    public @ResponseBody
    String sottrazione(@PathVariable float num1, @PathVariable float num2) {
        float sottrazione = num1 - num2;
        return "Sottrazione: " + sottrazione;
    }

    @RequestMapping (path = "/moltiplicazione/{num1}/{num2}")
    public @ResponseBody
    String moltiplicazione(@PathVariable float num1, @PathVariable float num2) {
        float moltiplicazione = num1 * num2;
        return "Moltiplicazione: " + moltiplicazione;
    }

    @RequestMapping (path = "/divisione/{num1}/{num2}")
    public @ResponseBody
    String divisione(@PathVariable float num1, @PathVariable float num2) {
        float divisione = num1 / num2;
        return "Divisione: " + divisione;
    }
}
