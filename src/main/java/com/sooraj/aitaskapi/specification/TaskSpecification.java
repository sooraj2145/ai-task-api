package com.sooraj.aitaskapi.specification;

import com.sooraj.aitaskapi.entity.Task;
import com.sooraj.aitaskapi.entity.TaskPriority;
import com.sooraj.aitaskapi.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null
                 ? null
                 : criteriaBuilder.equal(
                         root.get("status"),
                        status
                );
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) ->
                priority == null
                        ? null
                        : criteriaBuilder.equal(
                                root.get("priority"),
                                priority
                );
    }

    public static Specification<Task> search(String search) {
        return (root, query, criteriaBuilder) -> {

            if(search == null || search.isBlank()) {
                return null;
            }

            String pattern = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            pattern
                    )
            );
        };
    }

    public static Specification<Task> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("user").get("id"),
                        userId
                );
    }
}
