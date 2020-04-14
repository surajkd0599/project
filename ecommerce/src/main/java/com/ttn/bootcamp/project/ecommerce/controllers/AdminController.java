package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/ecommerce/admin/home")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping(path = "/customers")
    public MappingJacksonValue getCustomers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10")String size, @RequestParam(defaultValue = "userId") String SortBy){
        return adminService.registeredCustomers(page, size, SortBy);
    }

    @GetMapping(path = "/sellers")
    public MappingJacksonValue getSellers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10")String size, @RequestParam(defaultValue = "userId") String SortBy){
        return adminService.registeredSellers(page, size, SortBy);
    }

    @PatchMapping(path = "/activateCustomer/{id}")
    public String activateCustomer(@PathVariable(value = "id") Long id, HttpServletResponse response){

        String message = adminService.activateUser(id);

        if(!message.equals("Account activated")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PatchMapping(path = "/de-activateCustomer/{id}")
    public String deactivateCustomer(@PathVariable(value = "id") Long id,HttpServletResponse response){
        String message = adminService.deactivateUser(id);
        if(!message.equals("Account de-activated")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PatchMapping(path = "/activateSeller/{id}")
    public String activateSeller(@PathVariable(value = "id") Long id, HttpServletResponse response){
        String message = adminService.activateUser(id);
        if(!message.equals("Account activated")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PatchMapping(path = "/de-activateSeller/{id}")
    public String deactivateSeller(@PathVariable(value = "id") Long id, HttpServletResponse response){
        String message = adminService.deactivateUser(id);
        if(!message.equals("Account de-activated")){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return message;
    }
}

