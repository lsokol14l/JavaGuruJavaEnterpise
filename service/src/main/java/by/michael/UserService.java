package by.michael;

import by.michael.Entity.User;
import by.michael.dao.UserDao;

import java.util.List;
import java.util.Optional;

public class UserService {
  UserDao userDao = new UserDao();

  public Optional<UserDto> getUser(Long id) {
    return userDao.findUserById(id).map(user -> new UserDto(user.getId(), user.getName()));
  }

  public List<UserDto> getAllUsers() {
    List<User> allUsers = userDao.findAllUsers();
    return allUsers.stream().map(user -> new UserDto(user.getId(), user.getName())).toList();
  }

  public void updateUser(UserDto user) {
    userDao.updateUser(new User(user.getId(), user.getName()));
  }

  public void deleteUser(Long id) {
    userDao.deleteUser(id);
  }
}
