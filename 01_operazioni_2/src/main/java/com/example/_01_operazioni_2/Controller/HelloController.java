package com.example._01_operazioni_2.Controller;

import com.example._01_operazioni_2.Service.Operazioni;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//localhost:8082/??

@Controller
public class HelloController {

    Operazioni operazioni = new Operazioni();

    @RequestMapping (path = "/somma/{num1}/{num2}")
    public @ResponseBody
    String somma(@PathVariable float num1, @PathVariable float num2) {
        return "Somma: " + operazioni.somma(num1, num2);
    }

    @RequestMapping (path = "/sottrazione/{num1}/{num2}")
    public @ResponseBody
    String sottrazione(@PathVariable float num1, @PathVariable float num2) {
        return "Sottrazione: " + operazioni.sottrazione(num1, num2);
    }

    @RequestMapping (path = "/moltiplicazione/{num1}/{num2}")
    public @ResponseBody
    String moltiplicazione(@PathVariable float num1, @PathVariable float num2) {
        return "Moltiplicazione: " + operazioni.moltiplicazione(num1, num2);
    }

    @RequestMapping (path = "/divisione/{num1}/{num2}")
    public @ResponseBody
    String divisione(@PathVariable float num1, @PathVariable float num2) {
        return "Divisione: " + operazioni.divisione(num1, num2);
    }
}

