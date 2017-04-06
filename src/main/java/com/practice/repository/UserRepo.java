package com.practice.repository;

import java.util.Set;

import com.practice.model.User;

public interface UserRepo {

  public abstract void saveUser(User user);

  public abstract User getUserById(String userId);

  public abstract User updateUser(User user);

  public abstract long deleteUser(String userId);

  public abstract Set<String> getKeys();

}
