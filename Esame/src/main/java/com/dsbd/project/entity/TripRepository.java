package com.dsbd.project.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends CrudRepository<Trip, Integer> {

    // Filtri singoli
    List<Trip> findByOrigin(String origin);
    List<Trip> findByDestination(String destination);
    List<Trip> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);

    // Combinazioni di filtri
    List<Trip> findByOriginAndDestination(String origin, String destination);
    List<Trip> findByOriginAndDepartureTimeBetween(String origin, LocalDateTime start, LocalDateTime end);
    List<Trip> findByDestinationAndDepartureTimeBetween(String destination, LocalDateTime start, LocalDateTime end);
    List<Trip> findByOriginAndDestinationAndDepartureTimeBetween(String origin, String destination, LocalDateTime start, LocalDateTime end);
}