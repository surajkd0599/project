package com.ttn.bootcamp.project.ecommerce.dtos;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class SellerProfileDto {

    private Long id;

    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Must provide first name")
    private String firstName;

    @NotEmpty(message = "Must provide last name")
    private String lastName;

    @NotEmpty(message = "Must provide gst number")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",message = "The first 2 digits denote the State Code (01-37) as defined in the Code List for Land Regions.\n" +
            "\n" +
            "The next 10 characters pertain to PAN Number in AAAAA9999X format.\n" +
            "\n" +
            "13th character indicates the number of registrations an entity has within a state for the same PAN.\n" +
            "\n" +
            "14th character is currently defaulted to \"Z\"\n" +
            "\n" +
            "15th character is a checksum digit")
    private String gst;

    @NotEmpty(message = "Must provide company contact number")
    @Pattern(regexp="\\d{10}", message="Mobile number is invalid")
    private String companyContact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

}
