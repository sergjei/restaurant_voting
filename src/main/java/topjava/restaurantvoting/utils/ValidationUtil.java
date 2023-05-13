package topjava.restaurantvoting.utils;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import topjava.restaurantvoting.IllegalRequestDataException;
import topjava.restaurantvoting.model.BaseEntity;

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

    public static void assureIdConsistentRest(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        }
        if (entity.getId() != id) {
            throw new IllegalRequestDataException("Meal belongs to another restaurant! " +
                    entity.getClass().getSimpleName() + "(path) must have id: (" + entity.getId() + ")");
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
