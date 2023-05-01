package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import topjava.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT v FROM Vote v WHERE v.voteDate>=:startDate AND v.voteDate<=:endDate")
    List<Vote> getByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT v FROM Vote v WHERE v.user.id =:userId AND v.voteDate>=:startDate AND v.voteDate<=:endDate")
    List<Vote> getByDateAndUser(@Param("userId") Integer userId,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id =:restId AND v.voteDate>=:startDate AND v.voteDate<=:endDate")
    List<Vote> getByDateAndRestaurant(@Param("restId") Integer restId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
