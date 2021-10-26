package vn.edu.fpt.traffic_license.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.traffic_license.entities.Province;

import java.util.Set;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Set<Province> findByCityId(Long cityId);
}
