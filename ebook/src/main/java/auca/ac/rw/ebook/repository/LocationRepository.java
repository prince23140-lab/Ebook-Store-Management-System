package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.Location;
import auca.ac.rw.ebook.model.ELocationType;
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
    Optional<Location> findByCode(String code);
    List<Location> findByName(String name);
    List<Location> findByType(ELocationType type);
    
    // Parent-child relationship queries
    List<Location> findByParentCode(String parentCode);
    List<Location> findByParentIsNull(); // Root level (Provinces)
    
    // FIXED: Corrected method name and query
    @Query("SELECT l FROM Location l WHERE l.parent.location_id = :parentId")
    List<Location> findByParentId(@Param("parentId") UUID parentId);
    
    // existsBy... queries
    Boolean existsByCode(String code);
    Boolean existsByNameAndType(String name, ELocationType type);
    
    // Sorting and Pagination
    Page<Location> findByType(ELocationType type, Pageable pageable);
    Page<Location> findByParentIsNull(Pageable pageable); // Paginated provinces
    
    // Custom queries for Rwandan hierarchy
    @Query("SELECT l FROM Location l WHERE l.type = 'PROVINCE'")
    List<Location> findAllProvinces();
    
    @Query("SELECT l FROM Location l WHERE l.type = 'DISTRICT' AND l.parent.code = :provinceCode")
    List<Location> findDistrictsByProvinceCode(@Param("provinceCode") String provinceCode);
    
    @Query("SELECT l FROM Location l WHERE l.type = 'SECTOR' AND l.parent.code = :districtCode")
    List<Location> findSectorsByDistrictCode(@Param("districtCode") String districtCode);
    
    @Query("SELECT l FROM Location l WHERE l.type = 'CELL' AND l.parent.code = :sectorCode")
    List<Location> findCellsBySectorCode(@Param("sectorCode") String sectorCode);
    
    @Query("SELECT l FROM Location l WHERE l.type = 'VILLAGE' AND l.parent.code = :cellCode")
    List<Location> findVillagesByCellCode(@Param("cellCode") String cellCode);
    
    // Find full hierarchy for a location
    @Query("SELECT l FROM Location l WHERE l.code = :code")
    Optional<Location> findByCodeWithHierarchy(@Param("code") String code);
    
    // Find users by province (Midterm requirement) - FIXED
    @Query("SELECT u FROM User u JOIN u.location v JOIN v.parent c JOIN c.parent s JOIN s.parent d JOIN d.parent p WHERE p.code = :provinceCode")
    List<Object[]> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);
    
    @Query("SELECT u FROM User u JOIN u.location v JOIN v.parent c JOIN c.parent s JOIN s.parent d JOIN d.parent p WHERE p.name = :provinceName")
    List<Object[]> findUsersByProvinceName(@Param("provinceName") String provinceName);
}