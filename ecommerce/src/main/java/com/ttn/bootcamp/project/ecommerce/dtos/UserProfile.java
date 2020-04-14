package com.ttn.bootcamp.project.ecommerce.dtos;

import com.ttn.bootcamp.project.ecommerce.models.Address;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

public class UserProfile {
    private Long userId;

    private boolean isActive;

    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Must provide your first name")
    private String firstName;

    @NotEmpty(message = "Must provide your last name")
    private String lastName;

    private int age;

    @Temporal(TemporalType.DATE)
    @Past
    private Date dateOfBirth;

    private String gender;

    @NotEmpty(message = "Enter your email")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Enter your mobile number")
    @Pattern(regexp="\\d{10}", message="Mobile number is invalid")
    private String mobileNo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
