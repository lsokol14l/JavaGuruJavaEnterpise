package by.michael.dao;

import by.michael.entity.Path;

import java.util.Optional;

public class PathDao {
  public Optional<Path> findPathByLifestyle(String lifestyle) {
    if (lifestyle == null) return Optional.empty();

    Path lifeCycle = new Path();

    switch (lifestyle) {
      case "discipline", "hardworking" -> {
        lifeCycle.setPath("light");
        lifeCycle.setImageUrl(
            "images/" + lifestyle + ".png");
      }
      default -> {
        lifeCycle.setPath("dark");
        lifeCycle.setImageUrl(
            "images/" + lifestyle + ".png");
      }
    }

    return Optional.of(lifeCycle);
  }
}
