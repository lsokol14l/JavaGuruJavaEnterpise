package by.michael;

import by.michael.dao.UserDao;
import by.michael.dto.UserDto;
import by.michael.entity.User;

public class UserService {
  UserDao userDao = new UserDao();

  public void createNewUser(UserDto newUser) {
    userDao.createNewUser(
        new User(
            newUser.getName(),
            newUser.getAge(),
            newUser.getPhoneNumber(),
            newUser.getLogin(),
            newUser.getPassword()));
  }

//  public boolean login(String login, String password) {
//    return userDao.
//  }
}
