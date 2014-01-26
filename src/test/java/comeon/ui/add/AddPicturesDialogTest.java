package comeon.ui.add;


public final class AddPicturesDialogTest {

  public static void main(String[] args) {
//    final JFrame frame = new JFrame("Dialog test");
    final AddPicturesDialog dialog = new AddPicturesDialog();
    final int result = dialog.showDialog();
    System.out.println(result);
  }

}
