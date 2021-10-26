package vn.edu.fpt.traffic_license.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.traffic_license.entities.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
}