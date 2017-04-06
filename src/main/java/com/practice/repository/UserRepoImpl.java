package com.practice.repository;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.practice.model.User;

@Repository
public class UserRepoImpl implements UserRepo {

  private static final String KEY="USER";

  private final  RedisTemplate<String, Object> redisTemplate;

  private HashOperations<String, String, Object> hashOps;


  @Autowired
  private UserRepoImpl(final RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate=redisTemplate;
  }

  @PostConstruct
  private void init() {
    hashOps = redisTemplate.opsForHash();
  }

  @Override
  public void saveUser(User user) {
    hashOps.put(KEY, user.getId(), user);
  }

  @Override
  @Cacheable(value = "userCache", condition = "'1'.equals(#userId)")
  public User getUserById(String userId) {
    System.out.println("Entered the  Method");
    User user = (User) hashOps.get(KEY, userId);
    System.out.println("Exiting the method");
    return user;
  }

  @Override
  public User updateUser(User user) {
    if (hashOps.hasKey(KEY, user.getId())) {
      hashOps.put(KEY, user.getId(), user);
      System.out.println("updated the user");
      return (User) hashOps.get(KEY, user.getId());
    } else {
      System.out.println("user doesn't exist");
      return null;
    }
  }


  @Override
  @CacheEvict(value = "userCache", key = "#userId")
  public long deleteUser(String userId) {
    return hashOps.delete(KEY, userId);
  }

  @Override
  public Set<String> getKeys() {
    return hashOps.keys(KEY);
  }

}
