package vn.edu.fpt.traffic_license.repository.specification;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class PagingOptionDto {

    private int limit = 100;
    private int page = 0;

    public Pageable createPageable(int page, int limit, String sort) {
        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(page, limit);
        }

        Sort sortBy;
        if (sort.charAt(0) == '-') {
            sortBy = Sort.by(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, sort.substring(1))).descending();
        } else {
            sortBy = Sort.by(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, sort));
        }

        return PageRequest.of(page, limit, sortBy);
    }
}
