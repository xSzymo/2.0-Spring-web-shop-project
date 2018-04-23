package com.shop.data.tables;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;

    public Address() {

    }

    public Address(String street, String postalCode, String city, String country) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public boolean equals(Address address) {
        boolean sameObjects = true;
        if(!this.id.equals(address.getId()))
            sameObjects = false;
        if(!this.street.equals(address.getStreet()))
            sameObjects = false;
        if(!this.postalCode.equals(address.getPostalCode()))
            sameObjects = false;
        if(!this.city.equals(address.getCity()))
            sameObjects = false;
        if(!this.country.equals(address.getCountry()))
            sameObjects = false;
        return sameObjects;
    }

    @Override
    public String toString() {
        return "street : " + street + ", postal Code : " + postalCode + ", city : " + city
                + ", country : " + country;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
