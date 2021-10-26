package vn.edu.fpt.traffic_license.repository.specification;

import lombok.Getter;
import lombok.Setter;
import vn.edu.fpt.traffic_license.constants.SearchOperation;

@Getter
@Setter
public class SearchCriteria {

    private String key;
    private Object value;
    private SearchOperation operation;
    private String joinColumn;
    
    public SearchCriteria (String key, Object value, SearchOperation operation) {
		this.key = key;
		this.value = value;
		this.operation = operation;
	}
}
