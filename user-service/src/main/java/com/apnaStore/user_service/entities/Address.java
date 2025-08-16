package com.apnaStore.user_service.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String name;

    private String address;

    private String city;

    private String state;

    private String country;

    private Integer pinCode;

    private String phoneNum;

    private Boolean isDefault = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
