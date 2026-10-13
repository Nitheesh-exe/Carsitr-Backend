package com.nash.carrental.repository;

import com.nash.carrental.entity.Car;
import com.nash.carrental.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    List<Car> findByCategoryIgnoreCase(String category);

    List<Car> findByStatus(CarStatus status);

    List<Car> findByBrandContainingIgnoreCase(String brand);

    long countByStatus(CarStatus status);
}
