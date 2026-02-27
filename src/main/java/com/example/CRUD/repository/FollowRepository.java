package com.example.CRUD.repository;

import com.example.CRUD.entity.Follow;
import com.example.CRUD.entity.FollowStatus;
import com.example.CRUD.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow , Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(Long userId, Long followingId);

    List<Follow> findByFollowerIdAndFollowStatus(long follower_id, FollowStatus followStatus);

    List<Follow> findByFollowingIdAndFollowStatus(long following_id, FollowStatus followStatus);

    boolean existsByFollowerIdAndFollowingId(Long follower_id, Long following_id);

    //pending resquest received
    List<Follow> findByFollowingAndStatus(User following, FollowStatus status);
    // accepted followings (for mutual friends)
    @Query("""
       SELECT f.following FROM Follow f
       WHERE f.follower.id = :userId AND f.status = 'ACCEPTED'
    """)
    List<User> findAcceptedFollowings(Long userId);
    List<Follow> findByFollowingAndFollowStatus(User following, FollowStatus followStatus);

}