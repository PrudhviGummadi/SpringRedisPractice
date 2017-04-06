package com.practice.user.repo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.practice.model.User;
import com.practice.repository.UserRepo;
import com.pratice.configuration.RedisConfiguration;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RedisConfiguration.class })
@ComponentScan
public class UserRepoIT {

  @Autowired
  private UserRepo userRepo;

  private User user = new User();

  private User user2 = new User();

  @Before
  public void setup() {
    user.setEmail("test@email.com");
    user.setId("1");
    user.setName("test user");
    user.setPhone("123-456-7890");

    user2.setEmail("test2@email.com");
    user2.setId("2");
    user2.setName("test user2");
    user2.setPhone("223-456-7890");

    userRepo.saveUser(user);
    userRepo.saveUser(user2);
  }

  @Test
  public void testSaveUserInRedis() {

    System.out.println("Saved the User with Id:" + user.getId() + " in redis");
  }

  @Test
  public void testGetUser() {
    System.out.println("Fetching the User with Id:" + user.getId() + " in redis");

    user2 = userRepo.getUserById(user2.getId());
    user2 = userRepo.getUserById(user2.getId());
    user = userRepo.getUserById(user.getId());
    simulateSlowService();
    user = userRepo.getUserById(user.getId());
    if (user != null) {
      System.out.println(user);
    } else {
      System.out.println("user doesn't exist");
    }

    Assert.assertNotNull(user);
    Assert.assertTrue("123-456-7890".equals(user.getPhone()));
    Assert.assertTrue("test@email.com".equals(user.getEmail()));

  }

  @Test
  public void testUpdateUser() {

    System.out.println("Updating the User with Id:" + user.getId() + " in redis");

    userRepo.saveUser(user);

    user.setName("test updated user");

    user = userRepo.updateUser(user);

    if (user != null) {
      System.out.println("Updated the User with id: " + user.getId());
    } else {
      System.out.println("user doesn't exist");
    }

    Assert.assertNotNull(user);
    Assert.assertNotNull(user.getName());
    Assert.assertFalse("test user".equals(user.getName()));
    Assert.assertTrue("123-456-7890".equals(user.getPhone()));
    Assert.assertTrue("test@email.com".equals(user.getEmail()));

  }

  private void simulateSlowService() {
    System.out.println("Inside slow service method");
    try {
      Thread.sleep(5 * 1000);
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
    System.out.println("Exiting slow service method");
  }

  @After
  public void destroy() {
    if (user != null) {
      userRepo.deleteUser(user.getId());
    }
    if (user2 != null) {
      userRepo.deleteUser(user2.getId());
    }

  }

}
