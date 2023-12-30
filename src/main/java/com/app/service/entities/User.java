package com.app.service.entities;

import com.app.service.enums.UserRole;
import com.app.service.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Builder
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"clientId", "numberId"})})
public class User {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clientId;

    @NotNull
    @Column(name = "numberId", nullable = false, unique = true)
    private String numberId;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "lastName", length = 60, nullable = false)
    private String lastName;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "email", length = 60, nullable = false, unique = false)
    private String email;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "phone", length = 0, nullable = false)
    private String phone;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "address", length = 60, nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "photo")
    private String photoPath;

    @PrePersist
    void prePersist() {
        if (userStatus == null) {
            userStatus = UserStatus.ACTIVE;
        }

        if (dob == null) {
            dob = Date.valueOf(LocalDate.now());
        }
    }

    public User() {
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
