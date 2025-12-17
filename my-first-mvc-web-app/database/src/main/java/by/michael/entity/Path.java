package by.michael.entity;

public class Path {
  private String path;
  private String imageUrl;

  public Path() {}

  public Path(String path, String imageUrl) {
    this.path = path;
    this.imageUrl = imageUrl;
  }

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
}
