package vn.edu.fpt.traffic_license.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "license_no", unique = true)
    private String licenseNo;

    @Column(name = "active")
    private Boolean active;

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

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> users;

}
