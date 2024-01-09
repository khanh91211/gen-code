package com.fw.common.util;

import com.fw.common.model.Constants;
import com.fw.common.model.enumeration.SortOrderEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class SearchUtil {
    private SearchUtil() {
    }

    public static Pageable getPageableFromParam(Integer page, Integer size, String sort, SortOrderEnum order) {
        Sort sortRequest;
        if (sort != null) {
            sortRequest = Sort.by(order == SortOrderEnum.ASC ? Direction.ASC : Direction.DESC, sort);
        } else {
            sortRequest = Sort.unsorted();
        }
        if (size == null || size > Constants.DEFAULT_PAGE_SIZE_MAX) {
            size = Constants.DEFAULT_PAGE_SIZE_MAX;
        }
        return PageRequest.of(page, size, sortRequest);
    }

    public static <T> Specification<T> like(String fieldName, String value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.like(cb.lower(root.get(fieldName)), "%" + value.trim().toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> likeUpperCase(String fieldName, String value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.like(root.get(fieldName), "%" + value.trim().toUpperCase() + "%");
            }
            return cb.conjunction();
        };
    }

    public static <R, F> Specification<R> in(String fieldName, List<F> filterList) {
        return (root, query, cb) -> {
            if (filterList != null && !filterList.isEmpty()) {
                if (filterList.size() > 1) {
                    In<F> inClause = cb.in(root.get(fieldName));
                    filterList.forEach(inClause::value);
                    return inClause;
                } else {
                    return cb.equal(root.<Comparable>get(fieldName), filterList.get(0));
                }
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> eq(String fieldName, Object value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.equal(root.<Comparable>get(fieldName), value);
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> notEq(String fieldName, Object value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.notEqual(root.<Comparable>get(fieldName), value);
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> gt(String fieldName, Comparable value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.greaterThan(root.<Comparable>get(fieldName), value);
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> ge(String fieldName, Comparable value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.greaterThanOrEqualTo(root.<Comparable>get(fieldName), value);
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> lt(String fieldName, Comparable value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.lessThan(root.<Comparable>get(fieldName), value);
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> le(String fieldName, Comparable value) {
        return (root, query, cb) -> {
            if (value != null) {
                return cb.lessThanOrEqualTo(root.<Comparable>get(fieldName), value);
            }
            return cb.conjunction();
        };
    }

    /**
     * @author khanhtd
     */
    public static <T> Specification<T> bw(String fieldName, Comparable valueFirst, Comparable valueSecond) {
        return (root, query, cb) -> {
            if (valueFirst != null || valueSecond != null) {
                return cb.between(root.<Comparable>get(fieldName), valueFirst, valueSecond);
            }
            return cb.conjunction();
        };
    }

    public static <T> Specification<T> isNull(String fieldName) {
        return (root, query, cb) -> cb.isNull(root.<Comparable>get(fieldName));
    }

    public static <T> Specification<T> or(Map<String, String> mapPredicate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : mapPredicate.entrySet()) {
                if (entry.getValue() != null) {
                    predicates.add(cb.equal(root.<Comparable>get(entry.getKey()), entry.getValue()));
                }
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> newSpecification() {
        return (root, query, cb) -> cb.conjunction();
    }
}