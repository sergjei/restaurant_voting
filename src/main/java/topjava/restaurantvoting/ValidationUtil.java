package topjava.restaurantvoting;

import topjava.restaurantvoting.model.BaseEntity;
import topjava.restaurantvoting.model.Vote;

import java.time.LocalDate;

public class ValidationUtil {
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

    public static void assureVoteDate(Vote vote) {
        if (vote.getVoteDate() == null) {
            vote.setVoteDate(LocalDate.now());
        }
        if (!vote.getVoteDate().isEqual(LocalDate.now())) {
            throw new IllegalArgumentException(vote + "must have today date:" + LocalDate.now());
        }
    }
}
