package comeon.ui.actions;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import comeon.ui.UI;

public final class AboutAction extends BaseAction {

  private static final long serialVersionUID = 1L;
  
  private final Image icon;

  public AboutAction(final UI ui) {
    super("about", ui);
    final List<Image> icons = ui.getIconImages();
    icon = icons.get(icons.size() - 1);
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        // TODO i18n
        final JTextArea messageArea = new JTextArea(UI.BUNDLE.getString("about.message"));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        final JScrollPane messageScrollArea = new JScrollPane(messageArea);
        messageScrollArea.setPreferredSize(new Dimension(380, 240));
        JOptionPane.showMessageDialog(ui, new Object[] {
            new JLabel("ComeOn!"),
            messageScrollArea
          },
          UI.BUNDLE.getString("action.about.title"), JOptionPane.PLAIN_MESSAGE, new ImageIcon(icon));
      }
    });
  }

}
