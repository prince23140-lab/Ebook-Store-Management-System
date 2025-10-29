// UserService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.User;
import auca.ac.rw.ebook.model.EUserRole;
import auca.ac.rw.ebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // CRUD Operations
    public String saveUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setCreated_at(LocalDateTime.now());
            userRepository.save(user);
            return "User saved successfully";
        } else {
            return "User with this email already exists";
        }
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }
    
    public String updateUser(User user) {
        if (userRepository.existsById(user.getUser_id())) {
            userRepository.save(user);
            return "User updated successfully";
        } else {
            return "User not found";
        }
    }
    
    public String deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
    
    // findBy... methods
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> getUsersByRole(EUserRole role) {
        return userRepository.findByRole(role);
    }
    
    public List<User> getUsersByName(String name) {
        return userRepository.findByFullNameContaining(name);
    }
    
    public List<User> getUsersByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
    
    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    // existsBy... methods
    public Boolean checkUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public Boolean checkUserExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
    
    public Boolean validateUserCredentials(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }
    
    // Sorting and Pagination
    public Page<User> getUsersByRoleWithPagination(EUserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }
    
    public Page<User> getUsersByNameWithPagination(String name, Pageable pageable) {
        return userRepository.findByFullNameContaining(name, pageable);
    }
    
    public Page<User> getAllUsersWithPagination(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    // FIXED: Location-based queries (Midterm Requirement)
    public List<User> getUsersByProvince(String provinceName) {
        return userRepository.findByProvince(provinceName);
    }
    
    public List<User> getUsersByDistrict(String districtName) {
        return userRepository.findByDistrict(districtName);
    }
    
    public List<User> getUsersBySector(String sectorName) {
        return userRepository.findBySector(sectorName);
    }
    
    public List<User> getUsersByProvinceAndDistrict(String provinceName, String districtName) {
        return userRepository.findByProvinceAndDistrict(provinceName, districtName);
    }
    
    // Custom queries
    public Optional<Object[]> getUserWithLocationById(UUID userId) {
        return userRepository.findUserWithLocationById(userId);
    }
    
    // Business logic methods
    public String changeUserRole(UUID userId, EUserRole newRole) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(newRole);
            userRepository.save(user);
            return "User role updated successfully";
        } else {
            return "User not found";
        }
    }
    
    public Long getTotalUsersCount() {
        return userRepository.count();
    }
    
    public Long getUsersCountByRole(EUserRole role) {
        return userRepository.findByRole(role).stream().count();
    }
}