package com.github.sergjei.restaurant_voting.utils;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import com.github.sergjei.restaurant_voting.model.BaseEntity;
import com.github.sergjei.restaurant_voting.utils.exception.IllegalRequestDataException;

public class ValidationUtil {
    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id = null)");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        }
        if (entity.getId() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must have id of authorize user (" + id + ")");
        }
    }

    public static void assureIdConsistentRest(Integer entityId, int id) {
        if (entityId == null) throw new IllegalRequestDataException("Error! Menu item must belong to restaurant!");
        if (entityId != id) {
            throw new IllegalRequestDataException("Menu item belongs to another restaurant! Restaurant(path) must have id: (" + entityId + ")");
        }
    }

    public static void assureIdConsistentDef(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        }
        if (entity.getId() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must have id: (" + id + ")");
        }
    }

    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}
