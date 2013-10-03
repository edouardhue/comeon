package comeon.ui.pictures;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comeon.model.Picture;
import comeon.ui.UI;

final class PictureEditPanel extends JPanel {

  private static final Logger LOGGER = LoggerFactory.getLogger(PictureEditPanel.class);

  private static final long serialVersionUID = 1L;

  public PictureEditPanel(final PicturePanels panels) {
    super(new BorderLayout());
    final PictureMetadataPanel metadataPanel = new PictureMetadataPanel(panels);
    this.add(metadataPanel, BorderLayout.WEST);

    final JTextArea templateText = new AliasedTextArea(panels.getPicture().getTemplateText());
    templateText.getDocument().addDocumentListener(new TemplateListener(panels.getPicture()));
    final JScrollPane templatePanel = wrap(templateText);

    final JTextArea renderedTemplate = new AliasedTextArea(panels.getPicture().getRenderedTemplate());
    renderedTemplate.getDocument().addDocumentListener(new RenderedTemplateListener(panels.getPicture()));
    final JScrollPane renderedTemplatePanel = wrap(renderedTemplate);

    final JTabbedPane templatesPanel = new JTabbedPane(SwingConstants.TOP);
    templatesPanel.add(templatePanel, UI.BUNDLE.getString("picture.tab.template"));
    templatesPanel.add(renderedTemplatePanel, UI.BUNDLE.getString("picture.tab.page"));
    templatesPanel.setSelectedComponent(renderedTemplatePanel);

    this.add(templatesPanel, BorderLayout.CENTER);
  }

  private JScrollPane wrap(final JTextArea area) {
    return new JScrollPane(area);
  }

  private abstract class AbstractTemplateListener implements DocumentListener {
    private final Picture picture;

    protected AbstractTemplateListener(final Picture picture) {
      this.picture = picture;
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
      this.update(e);
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
      this.update(e);
    }

    @Override
    public final void changedUpdate(final DocumentEvent e) {
    }

    private void update(final DocumentEvent e) {
      try {
        this.doUpdate(picture, getText(e));
      } catch (final BadLocationException e1) {
        // TODO i18n
        LOGGER.warn("Can't update template text", e1);
      }
    }

    private String getText(final DocumentEvent e) throws BadLocationException {
      return e.getDocument().getText(0, e.getDocument().getLength());
    }

    protected abstract void doUpdate(final Picture picture, final String text);
  }

  private final class TemplateListener extends AbstractTemplateListener {
    private TemplateListener(final Picture picture) {
      super(picture);
    }

    @Override
    protected void doUpdate(final Picture picture, final String text) {
      picture.setTemplateText(text);
    }
  }

  private final class RenderedTemplateListener extends AbstractTemplateListener {
    private RenderedTemplateListener(final Picture picture) {
      super(picture);
    }

    @Override
    protected void doUpdate(final Picture picture, final String text) {
      picture.setRenderedTemplate(text);
    }
  }
}