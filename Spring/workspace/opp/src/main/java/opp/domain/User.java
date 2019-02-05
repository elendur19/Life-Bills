package opp.domain;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonSerialize(using = UserSerializer.class)
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @Size(min = 2,  max = 30)
    private String username;

    @Column(unique = true)
    @Size(min = 3,  max = 50)
    private String email;

    @Size(min=4, max = 50)
    private String password;

    @Column(unique = true)
    @Size(min = 11, max = 11)
    private String oib;

    @JsonProperty("usersBills")
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Bill> usersBills;

    @JsonProperty("otherUsersBills")
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Bill> otherUsersBills;

    private String dateOfBirth;

    @Size(min = 3, max = 50)
    private  String address;

    private String contactNumber;

    @Nullable
    private String name;

    @Nullable
    private String lastname;

    public User() {}

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

    public String getEmail() {
        return email;
    }

    public List<Bill> getOtherUsersBills() {
        return otherUsersBills;
    }

    public void setOtherUsersBills(List<Bill> otherUsersBills) {
        this.otherUsersBills = otherUsersBills;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonProperty("usersBills")
    public List<Bill> getUsersBills() {
        return usersBills;
    }

    public void setUsersBills(List<Bill> usersBills) {
        this.usersBills = usersBills;
    }
}
