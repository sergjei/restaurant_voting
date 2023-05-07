package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Query("SELECT v FROM Vote v WHERE v.user.id =:userId AND v.voteDate = CURRENT_DATE()")
    Vote getTodayByUser(@Param("userId") Integer userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id IN :userIds AND v.restaurant.id IN :restIds AND v.voteDate>=:startDate AND v.voteDate<=:endDate")
    List<Vote> getCustom(@Param("userIds") List<Integer> userIds,
                         @Param("restIds") List<Integer> restIds,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);


    @Query("SELECT v FROM Vote v WHERE v.voteDate = CURRENT_DATE()")
    List<Vote> getAllByToday();

    @Query("SELECT new topjava.restaurantvoting.to.RestaurantTo(r.id,r.name,r.email,r.address," +
            "(SELECT COUNT(v) FROM Vote v WHERE v.restaurant.id = r.id AND v.voteDate>=:startDate AND v.voteDate<=:endDate)) " +
            "AS r_vote_count FROM Restaurant r")
    List<RestaurantTo> getVoteCountByRestaurant(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

}
