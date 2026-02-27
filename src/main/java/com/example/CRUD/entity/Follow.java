package com.example.CRUD.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

//here some work for pending follow request
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"follwers_id","following_id"}))//define in the db follow_id & following_id ka combination unique ho
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    FollowStatus followStatus;

    @CurrentTimestamp
    LocalDateTime createdAt ;

}