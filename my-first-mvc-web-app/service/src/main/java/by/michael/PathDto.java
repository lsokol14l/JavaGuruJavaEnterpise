package by.michael;

import by.michael.dao.PathDao;

public class PathDto {
  private String path;
  private String imageUrl;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public PathDto(String path, String imageUrl) {
    this.path = path;
    this.imageUrl = imageUrl;
  }
}
