package com.dsbd.project.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TripRepository extends CrudRepository<Trip, Integer> {
    List<Trip> findByOrigin(String origin);
    List<Trip> findByDestination(String destination);
}
