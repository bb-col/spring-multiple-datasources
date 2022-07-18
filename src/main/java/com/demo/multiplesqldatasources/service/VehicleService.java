package com.demo.multiplesqldatasources.service;

import com.demo.multiplesqldatasources.model.entity.Vehicle;
import com.demo.multiplesqldatasources.repository.secondary.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicleRepository.findAll().forEach(vehicles::add);
        return vehicles;
    }
}
