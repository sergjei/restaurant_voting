package topjava.restaurantvoting.utils;

import topjava.restaurantvoting.IllegalRequestDataException;
import topjava.restaurantvoting.model.BaseEntity;

public class ValidationUtil {
    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + "must be new (id = null)");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        }
        if (entity.getId() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + "must have id of authorize user (" + id + ")");
        }
    }

}
