package com.redis.redi2read.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash
@Builder
public class Role {
    @Id
    private String id;
    private String name;
}