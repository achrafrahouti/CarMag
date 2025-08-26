package com.garagetest.repository;

import com.garagetest.model.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    
    // Find garages by name containing the given string (case insensitive)
    Page<Garage> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find garages by city (extracted from address)
    @Query("SELECT g FROM Garage g WHERE LOWER(g.address) LIKE LOWER(CONCAT('%', :city, '%'))")
    Page<Garage> findByCity(@Param("city") String city, Pageable pageable);
    
    // Find garages that have vehicles of a specific brand
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v WHERE LOWER(v.brand) = LOWER(:brand)")
    List<Garage> findByVehicleBrand(@Param("brand") String brand);
    
    // Find garages that have vehicles of a specific model
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v WHERE LOWER(v.model) = LOWER(:model)")
    List<Garage> findByVehicleModel(@Param("model") String model);
    
    // Find garages that have vehicles with a specific accessory type
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v JOIN v.accessories a WHERE LOWER(a.type) = LOWER(:accessoryType)")
    List<Garage> findByAccessoryType(@Param("accessoryType") String accessoryType);
}