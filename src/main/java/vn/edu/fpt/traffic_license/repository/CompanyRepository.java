package vn.edu.fpt.traffic_license.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.traffic_license.entities.Company;

import java.util.Set;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    Company findByLicenseNo(String licenseNo);
    Set<Company> findByWardIdAndProvinceIdAndCityId(Long wardId, Long provinceId, Long cityId);
}
