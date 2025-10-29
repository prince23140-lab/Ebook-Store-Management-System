// UserController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.User;
import auca.ac.rw.ebook.model.EUserRole;
import auca.ac.rw.ebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        String response = userService.saveUser(user);
        if (response.equals("User saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }
    
    // NEW: Save user with location
    @PostMapping(value = "/save-with-location", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUserWithLocation(
            @RequestBody User user,
            @RequestParam(required = false) UUID locationId) {
        
        String response;
        if (locationId != null) {
            response = userService.saveUserWithLocation(user, locationId);
        } else {
            response = userService.saveUser(user);
        }
        
        if (response.equals("User saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    
    // NEW: Get user with full location details
    @GetMapping(value = "/{id}/with-location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserWithLocationById(@PathVariable UUID id) {
        Optional<User> user = userService.getUserWithLocationById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String response = userService.updateUser(user);
        if (response.equals("User updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // NEW: Update user location
    @PutMapping(value = "/{userId}/location/{locationId}")
    public ResponseEntity<?> updateUserLocation(
            @PathVariable UUID userId,
            @PathVariable UUID locationId) {
        
        String response = userService.updateUserLocation(userId, locationId);
        if (response.equals("User location updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        String response = userService.deleteUser(id);
        if (response.equals("User deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable EUserRole role) {
        List<User> users = userService.getUsersByRole(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/search/{name}")
    public ResponseEntity<List<User>> searchUsersByName(@PathVariable String name) {
        List<User> users = userService.getUsersByName(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/phone/{phone}")
    public ResponseEntity<List<User>> getUsersByPhone(@PathVariable String phone) {
        List<User> users = userService.getUsersByPhone(phone);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // NEW: Get users by location
    @GetMapping(value = "/location/{locationId}")
    public ResponseEntity<List<User>> getUsersByLocationId(@PathVariable UUID locationId) {
        List<User> users = userService.getUsersByLocationId(locationId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists/email/{email}")
    public ResponseEntity<Boolean> checkUserExistsByEmail(@PathVariable String email) {
        Boolean exists = userService.checkUserExistsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    @GetMapping(value = "/exists/phone/{phone}")
    public ResponseEntity<Boolean> checkUserExistsByPhone(@PathVariable String phone) {
        Boolean exists = userService.checkUserExistsByPhone(phone);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Authentication endpoint
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        Boolean isValid = userService.validateUserCredentials(email, password);
        if (isValid) {
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                // Return user info without password for security
                User userInfo = user.get();
                // Create a safe response without password
                return new ResponseEntity<>(createSafeUserResponse(userInfo), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/role/{role}/page")
    public ResponseEntity<Page<User>> getUsersByRoleWithPagination(
            @PathVariable EUserRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "full_name") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<User> users = userService.getUsersByRoleWithPagination(role, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/search/{name}/page")
    public ResponseEntity<Page<User>> searchUsersByNameWithPagination(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "full_name") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<User> users = userService.getUsersByNameWithPagination(name, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/all/page")
    public ResponseEntity<Page<User>> getAllUsersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "full_name") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<User> users = userService.getAllUsersWithPagination(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // Location-based queries (Midterm Requirement)
    @GetMapping(value = "/province/{province}")
    public ResponseEntity<List<User>> getUsersByProvince(@PathVariable String province) {
        List<User> users = userService.getUsersByProvince(province);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/district/{district}")
    public ResponseEntity<List<User>> getUsersByDistrict(@PathVariable String district) {
        List<User> users = userService.getUsersByDistrict(district);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/sector/{sector}")
    public ResponseEntity<List<User>> getUsersBySector(@PathVariable String sector) {
        List<User> users = userService.getUsersBySector(sector);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/province/{province}/district/{district}")
    public ResponseEntity<List<User>> getUsersByProvinceAndDistrict(
            @PathVariable String province,
            @PathVariable String district) {
        List<User> users = userService.getUsersByProvinceAndDistrict(province, district);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // Business logic Endpoints
    @PutMapping(value = "/{userId}/role/{newRole}")
    public ResponseEntity<?> changeUserRole(
            @PathVariable UUID userId,
            @PathVariable EUserRole newRole) {
        String response = userService.changeUserRole(userId, newRole);
        if (response.equals("User role updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Statistics endpoints
    @GetMapping(value = "/stats/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        Long count = userService.getTotalUsersCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    @GetMapping(value = "/stats/role/{role}/count")
    public ResponseEntity<Long> getUsersCountByRole(@PathVariable EUserRole role) {
        Long count = userService.getUsersCountByRole(role);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    // Validation endpoint
    @PostMapping(value = "/validate-credentials", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validateUserCredentials(
            @RequestParam String email,
            @RequestParam String password) {
        Boolean isValid = userService.validateUserCredentials(email, password);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
    
    // Helper method to create safe user response without password
    private Object createSafeUserResponse(User user) {
        return new Object() {
            public UUID user_id = user.getUser_id();
            public String full_name = user.getFull_name();
            public String email = user.getEmail();
            public String phone = user.getPhone();
            public EUserRole role = user.getRole();
            public String fullLocationPath = user.getFullLocationPath();
            public String province = user.getProvince();
            public String district = user.getDistrict();
            public String sector = user.getSector();
            public String cell = user.getCell();
            public LocalDateTime created_at = user.getCreated_at();
        };
    }
    
    // NEW: Get user location details
    @GetMapping(value = "/{id}/location-details")
    public ResponseEntity<?> getUserLocationDetails(@PathVariable UUID id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new ResponseEntity<>(createLocationDetailsResponse(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    
    // Helper method for location details response
    private Object createLocationDetailsResponse(User user) {
        return new Object() {
            public UUID user_id = user.getUser_id();
            public String full_name = user.getFull_name();
            public String email = user.getEmail();
            public String fullLocationPath = user.getFullLocationPath();
            public String province = user.getProvince();
            public String district = user.getDistrict();
            public String sector = user.getSector();
            public String cell = user.getCell();
            public String village = user.getLocation() != null ? user.getLocation().getName() : null;
            public UUID location_id = user.getLocation() != null ? user.getLocation().getLocation_id() : null;
        };
    }
    
    // NEW: Bulk operations
    @PostMapping(value = "/bulk/location-update")
    public ResponseEntity<?> updateMultipleUsersLocation(
            @RequestParam UUID locationId,
            @RequestBody List<UUID> userIds) {
        try {
            int successCount = 0;
            int failCount = 0;
            
            for (UUID userId : userIds) {
                String response = userService.updateUserLocation(userId, locationId);
                if (response.equals("User location updated successfully")) {
                    successCount++;
                } else {
                    failCount++;
                }
            }
            
            return new ResponseEntity<>(
                String.format("Location update completed. Success: %d, Failed: %d", successCount, failCount),
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user locations: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // NEW: Get users without location
    @GetMapping(value = "/without-location")
    public ResponseEntity<List<User>> getUsersWithoutLocation() {
        List<User> allUsers = userService.getAllUsers();
        List<User> usersWithoutLocation = allUsers.stream()
                .filter(user -> user.getLocation() == null)
                .toList();
        return new ResponseEntity<>(usersWithoutLocation, HttpStatus.OK);
    }
    
    // NEW: Count users by location presence
    @GetMapping(value = "/stats/location-counts")
    public ResponseEntity<?> getUsersLocationStats() {
        List<User> allUsers = userService.getAllUsers();
        long withLocation = allUsers.stream().filter(user -> user.getLocation() != null).count();
        long withoutLocation = allUsers.stream().filter(user -> user.getLocation() == null).count();
        
        return new ResponseEntity<>(
            new Object() {
                public long totalUsers = allUsers.size();
                public long usersWithLocation = withLocation;
                public long usersWithoutLocation = withoutLocation;
            },
            HttpStatus.OK
        );
    }
}