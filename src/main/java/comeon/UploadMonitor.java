package comeon;

import in.yuvi.http.fluent.ProgressListener;

public interface UploadMonitor {
  void setBatchSize(int size);
  void uploadStarting();
  ProgressListener itemStarting(int index, long length, String name);
  void itemDone(int index);
  void uploadDone();
}