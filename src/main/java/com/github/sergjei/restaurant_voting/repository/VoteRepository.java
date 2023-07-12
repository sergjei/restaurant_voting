package com.github.sergjei.restaurant_voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.sergjei.restaurant_voting.model.Vote;
import com.github.sergjei.restaurant_voting.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v WHERE v.user.id IN :userId AND v.voteDate>=:startDate AND v.voteDate<=:endDate")
    List<Vote> getByDateAndUser(@Param("userId") List<Integer> userId,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.github.sergjei.restaurant_voting.to.RestaurantTo(r.id,r.name,r.address,r.email," +
            "(SELECT COUNT(v) FROM Vote v WHERE v.restaurant.id = r.id AND v.voteDate>=:startDate AND v.voteDate<=:endDate)) " +
            "AS r_vote_count FROM Restaurant r")
    List<RestaurantTo> getVoteCountByRestaurant(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);
}
