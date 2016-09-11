package comeon.ui.media;

import com.google.common.io.Resources;
import comeon.model.Media;
import comeon.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

final class MediaEditPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaEditPanel.class);

    private static final int TEMPLATE_TAB_INDEX = 0;

    private static final int RENDERED_TAB_INDEX = 1;

    private static final long serialVersionUID = 1L;

    private final JTabbedPane templatesPanel;

    public MediaEditPanel(final MediaPanels panels) {
        super(new BorderLayout());
        final MediaMetadataPanel metadataPanel = new MediaMetadataPanel(panels);
        this.add(metadataPanel, BorderLayout.WEST);

        final JTextArea templateText = new AliasedTextArea(panels.getMedia().getTemplateText());
        templateText.getDocument().addDocumentListener(new TemplateListener(panels.getMedia()));
        final JScrollPane templatePanel = wrap(templateText);

        final JTextArea renderedTemplate = new AliasedTextArea(panels.getMedia().getRenderedTemplate());
        renderedTemplate.getDocument().addDocumentListener(new RenderedTemplateListener(panels.getMedia()));
        final JScrollPane renderedTemplatePanel = wrap(renderedTemplate);

        this.templatesPanel = new JTabbedPane(SwingConstants.TOP);
        templatesPanel.insertTab(UI.BUNDLE.getString("media.tab.template"), new ImageIcon(Resources.getResource("comeon/ui/template_small.png")), templatePanel, null, TEMPLATE_TAB_INDEX);
        templatesPanel.insertTab(UI.BUNDLE.getString("media.tab.page"), new ImageIcon(Resources.getResource("comeon/ui/rendered_small.png")), renderedTemplatePanel, null, RENDERED_TAB_INDEX);
        templatesPanel.setSelectedComponent(renderedTemplatePanel);

        panels.getMedia().addPropertyChangeListener(evt -> {
            if ("renderedTemplate".equals(evt.getPropertyName()) && !evt.getNewValue().equals(renderedTemplate.getText())) {
                SwingUtilities.invokeLater(() -> renderedTemplate.setText(evt.getNewValue().toString()));
            }
        });

        this.add(templatesPanel, BorderLayout.CENTER);
    }

    private JScrollPane wrap(final JTextArea area) {
        return new JScrollPane(area);
    }

    private abstract class AbstractTemplateListener implements DocumentListener {
        private final Media media;

        protected AbstractTemplateListener(final Media media) {
            this.media = media;
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
                this.doUpdate(media, getText(e));
            } catch (final BadLocationException e1) {
                LOGGER.warn("Can't update template text", e1);
            }
        }

        private String getText(final DocumentEvent e) throws BadLocationException {
            return e.getDocument().getText(0, e.getDocument().getLength());
        }

        protected abstract void doUpdate(final Media media, final String text);
    }

    private final class TemplateListener extends AbstractTemplateListener {
        private TemplateListener(final Media media) {
            super(media);
        }

        @Override
        protected void doUpdate(final Media media, final String text) {
            media.setTemplateText(text);
        }
    }

    private final class RenderedTemplateListener extends AbstractTemplateListener {
        private RenderedTemplateListener(final Media media) {
            super(media);
        }

        @Override
        protected void doUpdate(final Media media, final String text) {
            media.setRenderedTemplate(text);
        }
    }
}