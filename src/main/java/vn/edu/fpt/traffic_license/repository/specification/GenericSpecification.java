package vn.edu.fpt.traffic_license.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.fpt.traffic_license.constants.SearchOperation;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {
    private static final long serialVersionUID = 1900581010229669687L;

    private transient List<SearchCriteria> list;

    public GenericSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //add add criteria to predicates
        for (SearchCriteria criteria : list) {
        	Path<Object> path;
        	if (criteria.getJoinColumn() != null) {
        		Join<Object, Object> join = root.join(criteria.getJoinColumn());
        		path = join.get(criteria.getKey());
        	} else {
        		path = root.get(criteria.getKey());
        	}
        	
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                if (criteria.getValue() instanceof Date) {
            		predicates.add(builder.greaterThan(path.as(Date.class), (Date) criteria.getValue()));
            	} else {
            		predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            	}
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                if (criteria.getValue() instanceof Date) {
            		predicates.add(builder.lessThan(path.as(Date.class), (Date) criteria.getValue()));
            	} else {
            		predicates.add(builder.lessThan(path.as(String.class), criteria.getValue().toString()));
            	}
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
            	if (criteria.getValue() instanceof Date) {
            		predicates.add(builder.greaterThanOrEqualTo(path.as(Date.class), (Date) criteria.getValue()));
            	} else {
            		predicates.add(builder.greaterThanOrEqualTo(path.as(String.class), criteria.getValue().toString()));
            	}
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                if (criteria.getValue() instanceof Date) {
            		predicates.add(builder.lessThanOrEqualTo(path.as(Date.class), (Date) criteria.getValue()));
            	} else {
            		predicates.add(builder.lessThanOrEqualTo(path.as(String.class), criteria.getValue().toString()));
            	}
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(builder.notEqual(path, criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(path, criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(builder.like(builder.lower(path.as(String.class)),"%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(builder.like(builder.lower(path.as(String.class)),criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
            	In<Object> inClause = builder.in(path);
            	List<Object> inParams = (List<Object>) criteria.getValue();
            	for (Object param : inParams) {
            		inClause.value(param);
            	}
            	predicates.add(inClause);
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
