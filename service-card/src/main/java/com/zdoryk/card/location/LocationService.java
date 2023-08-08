package com.zdoryk.card.location;

import com.zdoryk.card.dto.LocationFindRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public Location saveLocation(Location location){
        return locationRepository.save(location);
    }

    public Location getLocationByCountry(String country){
        return locationRepository.getLocationByCountry(country)
                .orElseThrow(() -> new RuntimeException("exc"));
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationByCountryAndCity(LocationFindRequest locationFindRequest) {
        return locationRepository.getLocationByCountryAndCity(locationFindRequest.getCountry(), locationFindRequest.getCity());
    }
}
