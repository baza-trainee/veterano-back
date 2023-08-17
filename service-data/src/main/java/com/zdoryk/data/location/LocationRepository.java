package com.zdoryk.data.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {

    Optional<Location> getLocationByCountry(String country);
    Optional<Location> getLocationByCity(String city);

    Optional<Location> getLocationByCountryAndCity(String country, String city);
}
