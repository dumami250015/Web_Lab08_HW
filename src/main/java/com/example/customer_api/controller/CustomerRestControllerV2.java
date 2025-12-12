package com.example.customer_api.controller;

import com.example.customer_api.dto.CustomerRequestDTO;
import com.example.customer_api.dto.CustomerResponseDTO;
import com.example.customer_api.dto.CustomerUpdateDTO;
import com.example.customer_api.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Version 2 Controller
 * Enhanced version with pagination, sorting, partial updates, and advanced search
 */
@RestController
@RequestMapping("/api/v2/customers")
@CrossOrigin(origins = "*")
public class CustomerRestControllerV2 {
    
    private final CustomerService customerService;
    
    public CustomerRestControllerV2(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    // ==================== Enhanced Features in V2 ====================
    
    // GET all customers with pagination and sorting
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        
        if (sortBy != null && !sortBy.isEmpty()) {
            Page<CustomerResponseDTO> customerPage = customerService.getAllCustomers(page, size, sortBy, sortDir);
            
            response.put("customers", customerPage.getContent());
            response.put("currentPage", customerPage.getNumber());
            response.put("totalItems", customerPage.getTotalElements());
            response.put("totalPages", customerPage.getTotalPages());
            response.put("pageSize", customerPage.getSize());
            response.put("hasNext", customerPage.hasNext());
            response.put("hasPrevious", customerPage.hasPrevious());
        } else {
            Page<CustomerResponseDTO> customerPage = customerService.getAllCustomers(page, size);
            
            response.put("customers", customerPage.getContent());
            response.put("currentPage", customerPage.getNumber());
            response.put("totalItems", customerPage.getTotalElements());
            response.put("totalPages", customerPage.getTotalPages());
            response.put("pageSize", customerPage.getSize());
            response.put("hasNext", customerPage.hasNext());
            response.put("hasPrevious", customerPage.hasPrevious());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // GET customer by ID with enhanced response
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("data", customer);
        response.put("links", createLinks(id));
        
        return ResponseEntity.ok(response);
    }
    
    // POST create new customer with enhanced response
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(requestDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("data", createdCustomer);
        response.put("message", "Customer created successfully");
        response.put("links", createLinks(createdCustomer.getId()));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // PUT update customer with enhanced response
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, requestDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("data", updatedCustomer);
        response.put("message", "Customer updated successfully");
        response.put("links", createLinks(id));
        
        return ResponseEntity.ok(response);
    }
    
    // PATCH partial update customer (V2 feature)
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> partialUpdateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDTO updateDTO) {
        CustomerResponseDTO updated = customerService.partialUpdateCustomer(id, updateDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("data", updated);
        response.put("message", "Customer partially updated successfully");
        response.put("links", createLinks(id));
        
        return ResponseEntity.ok(response);
    }
    
    // DELETE customer with enhanced response
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("message", "Customer deleted successfully");
        response.put("deletedId", id);
        
        return ResponseEntity.ok(response);
    }
    
    // GET search customers with enhanced response
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchCustomers(@RequestParam String keyword) {
        List<CustomerResponseDTO> customers = customerService.searchCustomers(keyword);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("keyword", keyword);
        response.put("count", customers.size());
        response.put("data", customers);
        
        return ResponseEntity.ok(response);
    }
    
    // GET customers by status with enhanced response
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getCustomersByStatus(@PathVariable String status) {
        List<CustomerResponseDTO> customers = customerService.getCustomersByStatus(status);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("status", status);
        response.put("count", customers.size());
        response.put("data", customers);
        
        return ResponseEntity.ok(response);
    }
    
    // GET advanced search (V2 feature)
    @GetMapping("/advanced-search")
    public ResponseEntity<Map<String, Object>> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status) {
        List<CustomerResponseDTO> customers = customerService.advancedSearch(name, email, status);
        
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion", "v2");
        response.put("filters", Map.of(
            "name", name != null ? name : "",
            "email", email != null ? email : "",
            "status", status != null ? status : ""
        ));
        response.put("count", customers.size());
        response.put("data", customers);
        
        return ResponseEntity.ok(response);
    }
    
    // Helper method to create HATEOAS-style links
    private Map<String, String> createLinks(Long id) {
        Map<String, String> links = new HashMap<>();
        links.put("self", "/api/v2/customers/" + id);
        links.put("update", "/api/v2/customers/" + id);
        links.put("delete", "/api/v2/customers/" + id);
        links.put("collection", "/api/v2/customers");
        return links;
    }
}
