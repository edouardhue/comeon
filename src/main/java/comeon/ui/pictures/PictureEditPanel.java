package comeon.ui.pictures;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PictureEditPanel extends JPanel {
  private static final Logger LOGGER = LoggerFactory.getLogger(PictureEditPanel.class);

  private static final long serialVersionUID = 1L;

  public PictureEditPanel(final PicturePanels panels) {
    super(new BorderLayout());
    this.add(new PictureMetadataPanel(panels), BorderLayout.WEST);

    final JTextArea templateText = new AliasedTextArea(panels.getPicture().getTemplateText());
    templateText.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(final DocumentEvent e) {
        try {
          panels.getPicture().setTemplateText(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          LOGGER.warn("Can't update template text", e1);
        }
      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        try {
          panels.getPicture().setTemplateText(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          LOGGER.warn("Can't update template text", e1);
        }
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {

      }
    });

    final JTextArea renderedTemplate = new AliasedTextArea(panels.getPicture().getRenderedTemplate());
    renderedTemplate.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(final DocumentEvent e) {
        try {
          panels.getPicture().setRenderedTemplate(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          LOGGER.warn("Can't update rendered template", e1);
        }
      }

      @Override
      public void insertUpdate(final DocumentEvent e) {
        try {
          panels.getPicture().setRenderedTemplate(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (final BadLocationException e1) {
          LOGGER.warn("Can't update rendered template", e1);
        }
      }

      @Override
      public void changedUpdate(final DocumentEvent e) {

      }
    });

    final JSplitPane templatesPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(templateText),
        new JScrollPane(renderedTemplate));
    this.add(templatesPanel, BorderLayout.CENTER);
  }
}