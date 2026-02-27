//package com.example.CRUD.service;
//
//
//import com.example.CRUD.dto.UserResponseDto;
//import com.example.CRUD.entity.Follow;
//import com.example.CRUD.entity.FollowStatus;
//import com.example.CRUD.entity.User;
//import com.example.CRUD.repository.FollowRepository;
//import com.example.CRUD.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class FollowService {
//
//
//    @Autowired
//    FollowRepository followRepository;
//    @Autowired
//    UserRepository userRepository;
//
//    public List<User> getFollowers(String username)
//    {
//        User user = userRepository.findByUsername(username);
//        List<Follow> followers = followRepository.findByFollowingIdAndFollowStatus(user.getId() , FollowStatus.FOLLOWING) ;
//
//        List<User> users = new ArrayList<>() ;
//
//        for(Follow follower : followers)
//        {
//            users.add(follower.getFollower());
////        }
//
//        return users;
//
//    }
//
//    public List<User> getFollowing(String username)
//    {
//        User user = userRepository.findByUsername(username);
//        List<Follow> followers = followRepository.findByFollowerIdAndFollowStatus(user.getId() , FollowStatus.FOLLOWING) ;
//
//        List<User> users = new ArrayList<>() ;
//
//        for(Follow follower : followers)
//        {
//            users.add(follower.getFollowing());
//        }
//
//        return users;
//    }
//
//    public void toggleFollow(long userId , String username){
//        User follower = userRepository.findByUsername(username);
//        User following = userRepository.findById(userId).get();
//
//        Optional<Follow> isfollow = followRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId());
//        if(isfollow.isPresent()){
//            followRepository.delete(isfollow.get());
//            return ;
//        }
//        Follow follow = new Follow();
//        follow.setFollowing(following);
//        follow.setFollower(follower);
//        follow.setFollowStatus(FollowStatus.PENDING);
//        followRepository.save(follow) ;
//
//    }
//    //Get Pending Requests
//    public List<UserResponseDto> getPendingFollowers(User currentUser) {
//        return followRepository
//                .findByFollowingAndStatus(currentUser, FollowStatus.PENDING)
//                .stream()
//                .map(f -> UserResponseDto.from(f.getFollower()))
//                .toList();
//    }
//    //Accept Request
//    public void acceptRequest(Long followId, User currentUser) {
//
//        if (currentUser == null)
//            throw new RuntimeException("Unauthorized");
//
//        Follow follow = followRepository.findById(followId).orElseThrow();
//
//        if (!Objects.equals(follow.getFollowing().getId(), currentUser.getId()))
//            throw new RuntimeException("Not allowed");
//
//        follow.getFollowStatus(FollowStatus.FOLLOWING);
//        followRepository.save(follow);
//    }
//    //Reject Request
//    public void rejectRequest(Long followId, User currentUser) {
//        Follow follow = followRepository.findById(followId).orElseThrow();
//        if (!Objects.equals(follow.getFollowing().getId(), currentUser.getId()))
////        if (!follow.getFollowing().getId().equals(currentUser.getId()))
//            throw new RuntimeException("Not allowed");
//
//        follow.setStatus(FollowStatus.REJECTED);
//        followRepository.save(follow);
//    }
//
//    //Mutual Friends
//    public List<UserResponseDto> getMutualFriends(Long targetUserId, User currentUser) {
//
//        List<User> myFollowing =
//                followRepository.findAcceptedFollowings(currentUser.getId());
//
//        List<User> targetFollowing =
//                followRepository.findAcceptedFollowings(targetUserId);
//
//        Set<Long> ids = targetFollowing.stream()
//                .map(User::getId)
//                .collect(Collectors.toSet());
//
//        return myFollowing.stream()
//                .filter(u -> ids.contains(u.getId()))
//                .map(UserResponseDto::from)
//                .toList();
//    }
//
//}


package com.example.CRUD.service;

import com.example.CRUD.dto.UserResponseDto;
import com.example.CRUD.entity.Follow;
import com.example.CRUD.entity.FollowStatus;
import com.example.CRUD.entity.User;
import com.example.CRUD.repository.FollowRepository;
import com.example.CRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    // ================================
    // GET FOLLOWERS
    // ================================
    public List<User> getFollowers(String username) {
        User user = userRepository.findByUsername(username);

        List<Follow> followers =
                followRepository.findByFollowingIdAndFollowStatus(
                        user.getId(), FollowStatus.FOLLOWING);

        List<User> users = new ArrayList<>();
        for (Follow f : followers) {
            users.add(f.getFollower());
        }
        return users;
    }

    // ================================
    // GET FOLLOWING
    // ================================
    public List<User> getFollowing(String username) {
        User user = userRepository.findByUsername(username);

        List<Follow> following =
                followRepository.findByFollowerIdAndFollowStatus(
                        user.getId(), FollowStatus.FOLLOWING);

        List<User> users = new ArrayList<>();
        for (Follow f : following) {
            users.add(f.getFollowing());
        }
        return users;
    }

    // ================================
    // FOLLOW / UNFOLLOW
    // ================================
    public void toggleFollow(long targetUserId, String username) {

        User follower = userRepository.findByUsername(username);
        User following = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Follow> existing =
                followRepository.findByFollowerIdAndFollowingId(
                        follower.getId(), following.getId());

        // Unfollow
        if (existing.isPresent()) {
            followRepository.delete(existing.get());
            return;
        }

        // New follow request
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        follow.setFollowStatus(FollowStatus.PENDING);

        followRepository.save(follow);
    }

    // ================================
    // PENDING REQUESTS
    // ================================
    public List<UserResponseDto> getPendingFollowers(User currentUser) {
        return followRepository
                .findByFollowingAndFollowStatus(currentUser, FollowStatus.PENDING)
                .stream()
                .map(f -> UserResponseDto.from(f.getFollower()))
                .toList();
    }

    // ================================
    // ACCEPT REQUEST
    // ================================
    public void acceptRequest(Long followId, User currentUser) {

        if (currentUser == null)
            throw new RuntimeException("Unauthorized");

        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!Objects.equals(follow.getFollowing().getId(), currentUser.getId()))
            throw new RuntimeException("Not allowed");

        follow.setFollowStatus(FollowStatus.FOLLOWING);
        followRepository.save(follow);
    }

    // ================================
    // REJECT REQUEST
    // ================================
    public void rejectRequest(Long followId, User currentUser) {

        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!Objects.equals(follow.getFollowing().getId(), currentUser.getId()))
            throw new RuntimeException("Not allowed");

        follow.setFollowStatus(FollowStatus.REJECTED);
        followRepository.save(follow);
    }

    // ================================
    // MUTUAL FRIENDS
    // ================================
    public List<UserResponseDto> getMutualFriends(Long targetUserId, User currentUser) {

        List<User> myFollowing =
                followRepository.findAcceptedFollowings(currentUser.getId());

        List<User> targetFollowing =
                followRepository.findAcceptedFollowings(targetUserId);

        Set<Long> ids = targetFollowing.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        return myFollowing.stream()
                .filter(u -> ids.contains(u.getId()))
                .map(UserResponseDto::from)
                .toList();
    }
}