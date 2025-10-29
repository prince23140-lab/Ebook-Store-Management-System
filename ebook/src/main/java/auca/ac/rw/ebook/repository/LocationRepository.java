package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    
    // findBy... queries
    List<Location> findByProvince(String province);
    List<Location> findByDistrict(String district);
    List<Location> findBySector(String sector);
    List<Location> findByCell(String cell);
    List<Location> findByVillage(String village);
    
    Optional<Location> findByProvinceAndDistrictAndSectorAndCellAndVillage(
        String province, String district, String sector, String cell, String village);
    
    // existsBy... queries
    Boolean existsByProvinceAndDistrictAndSectorAndCellAndVillage(
        String province, String district, String sector, String cell, String village);
    
    Boolean existsByProvince(String province);
    Boolean existsByDistrict(String district);
    
    // Sorting and Pagination
    Page<Location> findByProvince(String province, Pageable pageable);
    Page<Location> findByDistrict(String district, Pageable pageable);
    Page<Location> findAll(Pageable pageable);
    
    // Custom queries for Rwandan hierarchy
    @Query("SELECT DISTINCT l.province FROM Location l")
    List<String> findAllDistinctProvinces();
    
    @Query("SELECT l FROM Location l WHERE l.province = :province AND l.district = :district")
    List<Location> findByProvinceAndDistrict(@Param("province") String province, @Param("district") String district);
    
    @Query("SELECT l FROM Location l WHERE l.province = :provinceName")
    List<Location> findLocationsByProvinceName(@Param("provinceName") String provinceName);
}