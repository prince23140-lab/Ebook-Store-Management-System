package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Location;
import auca.ac.rw.ebook.model.ELocationType;
import auca.ac.rw.ebook.service.LocationService;
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
@RequestMapping("/api/location")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    // CRUD Endpoints with parent-child support
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLocation(@RequestParam(required = false) String parentCode, 
                                         @RequestBody Location location) {
        String response = locationService.saveLocation(parentCode, location);
        if (response.contains("saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLocationById(@PathVariable UUID id) {
        Optional<Location> location = locationService.getLocationById(id);
        if (location.isPresent()) {
            return new ResponseEntity<>(location.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLocationByCode(@PathVariable String code) {
        Optional<Location> location = locationService.getLocationByCode(code);
        if (location.isPresent()) {
            return new ResponseEntity<>(location.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        String response = locationService.updateLocation(location);
        if (response.equals("Location updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable UUID id) {
        String response = locationService.deleteLocation(id);
        if (response.equals("Location deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // Rwandan Hierarchy Endpoints
    @GetMapping(value = "/provinces")
    public ResponseEntity<List<Location>> getAllProvinces() {
        List<Location> provinces = locationService.getProvinces();
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }
    
    @GetMapping(value = "/province/{provinceCode}/districts")
    public ResponseEntity<List<Location>> getDistrictsByProvince(@PathVariable String provinceCode) {
        List<Location> districts = locationService.getDistrictsByProvince(provinceCode);
        return new ResponseEntity<>(districts, HttpStatus.OK);
    }
    
    @GetMapping(value = "/district/{districtCode}/sectors")
    public ResponseEntity<List<Location>> getSectorsByDistrict(@PathVariable String districtCode) {
        List<Location> sectors = locationService.getSectorsByDistrict(districtCode);
        return new ResponseEntity<>(sectors, HttpStatus.OK);
    }
    
    @GetMapping(value = "/sector/{sectorCode}/cells")
    public ResponseEntity<List<Location>> getCellsBySector(@PathVariable String sectorCode) {
        List<Location> cells = locationService.getCellsBySector(sectorCode);
        return new ResponseEntity<>(cells, HttpStatus.OK);
    }
    
    @GetMapping(value = "/cell/{cellCode}/villages")
    public ResponseEntity<List<Location>> getVillagesByCell(@PathVariable String cellCode) {
        List<Location> villages = locationService.getVillagesByCell(cellCode);
        return new ResponseEntity<>(villages, HttpStatus.OK);
    }
    
    @GetMapping(value = "/parent/{parentCode}/children")
    public ResponseEntity<List<Location>> getChildrenByParent(@PathVariable String parentCode) {
        List<Location> children = locationService.getChildrenByParentCode(parentCode);
        return new ResponseEntity<>(children, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{locationCode}/full-path")
    public ResponseEntity<String> getFullLocationPath(@PathVariable String locationCode) {
        String path = locationService.getFullLocationPath(locationCode);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }
    
    // Midterm Requirement: Get users by province
    @GetMapping(value = "/province/{provinceCode}/users")
    public ResponseEntity<?> getUsersByProvinceCode(@PathVariable String provinceCode) {
        List<Object[]> usersWithLocations = locationService.getUsersByProvinceCode(provinceCode);
        return new ResponseEntity<>(usersWithLocations, HttpStatus.OK);
    }
    
    @GetMapping(value = "/province/{provinceName}/users-by-name")
    public ResponseEntity<?> getUsersByProvinceName(@PathVariable String provinceName) {
        List<Object[]> usersWithLocations = locationService.getUsersByProvinceName(provinceName);
        return new ResponseEntity<>(usersWithLocations, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/type/{type}/page")
    public ResponseEntity<Page<Location>> getLocationsByTypeWithPagination(
            @PathVariable ELocationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Location> locations = locationService.getLocationsByTypeWithPagination(type, pageable);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    @GetMapping(value = "/provinces/page")
    public ResponseEntity<Page<Location>> getProvincesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Location> provinces = locationService.getProvincesWithPagination(pageable);
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }
}