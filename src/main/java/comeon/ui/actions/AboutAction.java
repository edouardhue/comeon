package comeon.ui.actions;

import comeon.ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public final class AboutAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private final Image icon;

    public AboutAction() {
        super("about");
        icon = UI.ICON_IMAGES.get(UI.ICON_IMAGES.size() - 1);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            final JTextArea messageArea = new JTextArea(UI.BUNDLE.getString("about.message"));
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);
            final JScrollPane messageScrollArea = new JScrollPane(messageArea);
            messageScrollArea.setPreferredSize(new Dimension(380, 240));
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), new Object[]{new JLabel(UI.BUNDLE.getString("comeon")),
                    messageScrollArea}, UI.BUNDLE.getString("action.about.title"), JOptionPane.PLAIN_MESSAGE, new ImageIcon(
                    icon));
        });
    }

}
