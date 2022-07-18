package com.demo.multiplesqldatasources.repository.secondary;

import com.demo.multiplesqldatasources.model.entity.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, UUID> {
}
