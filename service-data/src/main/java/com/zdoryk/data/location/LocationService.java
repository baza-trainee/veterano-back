package com.zdoryk.data.location;

import com.zdoryk.data.dto.LocationFindRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public void saveLocation(Location location){locationRepository.save(location);}

    public Location getLocationByCountry(String country){
        return locationRepository.getLocationByCountry(country)
                .orElseThrow(() -> new RuntimeException("exc"));
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationByCountryAndCity(LocationFindRequest locationFindRequest) {
        return locationRepository.getLocationByCountryAndCity(
                locationFindRequest.getCountry(),
                locationFindRequest.getCity()
        );
    }



}
