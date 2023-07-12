package com.github.sergjei.restaurant_voting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.sergjei.restaurant_voting.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Cacheable(cacheNames = "users")
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    @Override
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id =:id")
    Optional<User> findById(@Param("id") Integer id);

    @Override
    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAll();

    @Modifying
    @Transactional
    @CacheEvict(value = "users", allEntries = true, condition = "!#p0.isNew()", beforeInvocation = true)
    User save(User user);

    @Modifying
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    void deleteById(Integer id);
}
