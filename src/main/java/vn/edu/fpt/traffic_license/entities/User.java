package vn.edu.fpt.traffic_license.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "identification_number", unique = true)
    private String identificationNumber;

    @Column(name = "no_vaccinated")
    private Long noOfVaccinated;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "locked")
    private Boolean locked;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;

    @ManyToOne
    @JoinColumn(name = "district_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private District district;

    @ManyToOne
    @JoinColumn(name = "province_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Province province;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private City city;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Company company;

}
