package topjava.restaurantvoting;

import topjava.restaurantvoting.model.BaseEntity;

public class ValidationUtils {
    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + "must be new (id = null)");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        }
        if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + "must have id of authorize user (" + id + ")");
        }
    }
}
