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
    
    // Authentication endpoint
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        // This would require authentication service implementation
        return new ResponseEntity<>("Login endpoint - implement authentication logic", HttpStatus.OK);
    }
}