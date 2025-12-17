package by.michael;

import by.michael.dao.PathDao;

import java.util.Optional;

public class PathService {
  private PathDao pathDao = new PathDao();

  public Optional<PathDto> getPath(String lyfestyle) {
    return pathDao
        .findPathByLifestyle(lyfestyle)
        .map(pathEntity -> new PathDto(pathEntity.getPath(), pathEntity.getImageUrl()));
  }
}
