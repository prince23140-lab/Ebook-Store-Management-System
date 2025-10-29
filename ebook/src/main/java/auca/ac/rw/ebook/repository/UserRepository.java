package auca.ac.rw.ebook.repository;

import auca.ac.rw.ebook.model.User;
import auca.ac.rw.ebook.model.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    
    // findBy... queries
    Optional<User> findByEmail(String email);
    List<User> findByRole(EUserRole role);
    
    // FIXED: Using @Query for full_name
    @Query("SELECT u FROM User u WHERE u.full_name LIKE %:name%")
    List<User> findByFullNameContaining(@Param("name") String name);
    
    List<User> findByPhone(String phone);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    // existsBy... queries
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.password = :password")
    Boolean existsByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    // Location-based queries (Midterm Requirement)
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.province = :province")
    List<User> findByProvince(@Param("province") String province);
    
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.district = :district")
    List<User> findByDistrict(@Param("district") String district);
    
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.sector = :sector")
    List<User> findBySector(@Param("sector") String sector);
    
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.province = :province AND l.district = :district")
    List<User> findByProvinceAndDistrict(@Param("province") String province, @Param("district") String district);
    
    // Sorting and Pagination
    Page<User> findByRole(EUserRole role, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.full_name LIKE %:name%")
    Page<User> findByFullNameContaining(@Param("name") String name, Pageable pageable);
    
    Page<User> findAll(Pageable pageable);
    
    // Custom query to get user with location details
    @Query("SELECT u, l FROM User u JOIN u.location l WHERE u.user_id = :userId")
    Optional<Object[]> findUserWithLocationById(@Param("userId") UUID userId);
}