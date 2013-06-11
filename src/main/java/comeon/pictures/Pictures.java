package comeon.pictures;

import java.util.List;

import comeon.model.Picture;
import comeon.model.User;

public interface Pictures {

  public abstract Pictures readFiles(User user);

  public abstract List<Picture> getPictures();

}