package comeon.core;

import java.io.File;
import java.io.IOException;

import comeon.model.Media;
import comeon.model.User;

public final class AudioReader extends AbstractMediaReader {

  public AudioReader(MediaUploadBatch mediaUploadBatch, File file, User user) {
    super(mediaUploadBatch, file, user);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected Media buildMedia() throws MediaReaderException, IOException {
    // TODO Auto-generated method stub
    return null;
  }

}
