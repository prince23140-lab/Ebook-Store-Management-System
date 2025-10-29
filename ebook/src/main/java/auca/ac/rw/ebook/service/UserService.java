// UserService.java - Fix the update method
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.User;
import auca.ac.rw.ebook.model.EUserRole;
import auca.ac.rw.ebook.model.Location;
import auca.ac.rw.ebook.repository.UserRepository;
import auca.ac.rw.ebook.repository.LocationRepository;
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

    @Autowired
    private LocationRepository locationRepository;
    
    // CRUD Operations
    public String saveUser(User user) {
        return saveUserWithLocation(user, null);
    }

    public String saveUserWithLocation(User user, UUID locationId) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            // Set created date
            user.setCreated_at(LocalDateTime.now());
            
            // Set location if provided
            if (locationId != null) {
                Optional<Location> locationOpt = locationRepository.findById(locationId);
                if (locationOpt.isPresent()) {
                    user.setLocation(locationOpt.get());
                } else {
                    return "Location not found with ID: " + locationId;
                }
            }
            
            userRepository.save(user);
            return "User saved successfully";
        } else {
            return "User with this email already exists";
        }
    }

    // FIXED: Update user method to handle password properly
    public String updateUser(User user) {
        Optional<User> existingUserOpt = userRepository.findById(user.getUser_id());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            
            // Update fields only if they are provided (not null)
            if (user.getFull_name() != null) {
                existingUser.setFull_name(user.getFull_name());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            // FIX: Only update password if it's provided and not null
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }
            if (user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if (user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }
            if (user.getLocation() != null) {
                existingUser.setLocation(user.getLocation());
            }
            
            userRepository.save(existingUser);
            return "User updated successfully";
        } else {
            return "User not found";
        }
    }

    // Alternative update method for partial updates
    public String updateUserPartial(UUID userId, User userUpdates) {
        Optional<User> existingUserOpt = userRepository.findById(userId);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            
            // Update only the fields that are provided
            if (userUpdates.getFull_name() != null) {
                existingUser.setFull_name(userUpdates.getFull_name());
            }
            if (userUpdates.getEmail() != null) {
                existingUser.setEmail(userUpdates.getEmail());
            }
            if (userUpdates.getPassword() != null && !userUpdates.getPassword().trim().isEmpty()) {
                existingUser.setPassword(userUpdates.getPassword());
            }
            if (userUpdates.getPhone() != null) {
                existingUser.setPhone(userUpdates.getPhone());
            }
            if (userUpdates.getRole() != null) {
                existingUser.setRole(userUpdates.getRole());
            }
            
            userRepository.save(existingUser);
            return "User updated successfully";
        } else {
            return "User not found";
        }
    }

    // NEW: Update user location
    public String updateUserLocation(UUID userId, UUID locationId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Location> locationOpt = locationRepository.findById(locationId);
        
        if (userOpt.isPresent() && locationOpt.isPresent()) {
            User user = userOpt.get();
            user.setLocation(locationOpt.get());
            userRepository.save(user);
            return "User location updated successfully";
        } else if (!userOpt.isPresent()) {
            return "User not found";
        } else {
            return "Location not found";
        }
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserWithLocationById(UUID id) {
        return userRepository.findById(id);
    }
    
    public String deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
    
    // Other existing methods remain the same...
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
    
    // Location-based queries (Midterm Requirement)
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

    // NEW: Get users by location ID
    public List<User> getUsersByLocationId(UUID locationId) {
        Optional<Location> locationOpt = locationRepository.findById(locationId);
        if (locationOpt.isPresent()) {
            List<User> allUsers = userRepository.findAll();
            return allUsers.stream()
                    .filter(user -> user.getLocation() != null && user.getLocation().getLocation_id().equals(locationId))
                    .toList();
        }
        return List.of();
    }

    // NEW: Update user password only
    public String updateUserPassword(UUID userId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return "Password updated successfully";
        } else {
            return "User not found";
        }
    }
}