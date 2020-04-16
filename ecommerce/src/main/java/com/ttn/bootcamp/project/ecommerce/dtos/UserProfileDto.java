package com.ttn.bootcamp.project.ecommerce.dtos;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserProfileDto {
    private Long id;

    private boolean isActive;

    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Must provide your first name")
    private String firstName;

    @NotEmpty(message = "Must provide your last name")
    private String lastName;

    @NotEmpty(message = "Enter your mobile number")
    @Pattern(regexp="\\d{10}", message="Mobile number is invalid")
    private String mobileNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
