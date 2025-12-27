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
            newUser.getLogin(),
            newUser.getPhoneNumber(),
            newUser.getPassword()));
  }

  public UserDto authenticate(String email, String password) {
    User user = userDao.findByEmailAndPassword(email, password);
    if (user != null) {
      return new UserDto(
          user.getName(), user.getPhoneNumber(), user.getLogin(), user.getPassword());
    }
    return null;
  }
}
