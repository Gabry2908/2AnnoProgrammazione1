package com.dsbd.project.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "L'origine non puo essere vuota")
    private String origin;

    @NotNull(message = "la Destinazione non puo essere vuota!")
    private String destination;

    @NotNull(message = "Il Tempo della tratta non puo essere vuota")
    private LocalDateTime departureTime;

    @NotNull(message = "Il Prezzo del biglietto non puo essere vuoto!")
    @PositiveOrZero(message = "Il prezzo deve essere positivo!")
    private BigDecimal price;

    // --- Getters e Setters ---

    public Integer getId() {
        return id;
    }

    public Trip setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public Trip setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public Trip setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Trip setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Trip setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
