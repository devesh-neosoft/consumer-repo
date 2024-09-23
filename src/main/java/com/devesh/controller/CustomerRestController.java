package com.devesh.controller;

import com.devesh.model.Customer;
import com.devesh.service.CustomerService;
import com.devesh.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CustomerRestController {


    @Autowired
    CustomerService customerService;

    @PostMapping("/getAllStudent")
    public ResponseEntity<GenericResponse<List<Customer>>> getAllStudents(){
        return customerService.getAllCustomer();
    }

}
