package com.garagetest.repository;

import com.garagetest.model.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    
    // Find accessories by vehicle id
    List<Accessory> findByVehicleId(Long vehicleId);
    
    // Find accessories by type
    List<Accessory> findByTypeIgnoreCase(String type);
    
    // Find accessories by name containing the given string
    List<Accessory> findByNameContainingIgnoreCase(String name);
    
    // Find accessories by price less than or equal to the given value
    List<Accessory> findByPriceLessThanEqual(Double price);
    
    // Find accessories by vehicle model
    @Query("SELECT a FROM Accessory a JOIN a.vehicle v WHERE LOWER(v.model) = LOWER(:model)")
    List<Accessory> findByVehicleModel(@Param("model") String model);
    
    // Find accessories by garage id
    @Query("SELECT a FROM Accessory a JOIN a.vehicle v JOIN v.garage g WHERE g.id = :garageId")
    List<Accessory> findByGarageId(@Param("garageId") Long garageId);
}