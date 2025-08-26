package com.garagetest.repository;

import com.garagetest.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // Find vehicles by garage id
    List<Vehicle> findByGarageId(Long garageId);
    
    // Find vehicles by brand
    List<Vehicle> findByBrandIgnoreCase(String brand);
    
    // Find vehicles by model
    List<Vehicle> findByModelIgnoreCase(String model);
    
    // Find vehicles by model across all garages
    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.model) = LOWER(:model)")
    List<Vehicle> findAllByModel(@Param("model") String model);
    
    // Find vehicles by fuel type
    List<Vehicle> findByFuelTypeIgnoreCase(String fuelType);
    
    // Find vehicles by manufacturing year
    List<Vehicle> findByManufacturingYear(Integer year);
    
    // Count vehicles in a garage
    long countByGarageId(Long garageId);
}