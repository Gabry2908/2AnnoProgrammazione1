package com.example._01_operazioni_3.Controller;

import com.example._01_operazioni_3.Service.Operazioni;
import com.example._01_operazioni_3.Service.OperazioneRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//localhost:8082/??

@RestController
public class HelloController {

    Operazioni operazioni = new Operazioni();

    @RequestMapping (path = "/somma/{num1}/{num2}")
    public String somma(@PathVariable float num1, @PathVariable float num2) {
        return "Somma: " + operazioni.somma(num1, num2);
    }

    @GetMapping("/sottrazione")
    public String sottrazione(@RequestParam float num1, @RequestParam float num2) {
        return "Sottrazione: " + operazioni.sottrazione(num1, num2);
    }


    // Body JSON: { "num1": 10, "num2": 2 }
    @PostMapping("/moltiplicazione")
    public String moltiplicazione(@RequestBody OperazioneRequest request) {
        return "Moltiplicazione: " + operazioni.moltiplicazione(request.getNum1(), request.getNum2());
    }
    @PostMapping("/divisione")
    public String divisione(@RequestBody OperazioneRequest request) {
        return "Divisione: " + operazioni.divisione(request.getNum1(), request.getNum2());
    }

}