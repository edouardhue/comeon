package comeon.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Template {
    private final String name;

    private final String description;

    private final Path file;

    private final Charset charset;

    private String templateText;

    private final TemplateKind kind;

    public Template(final String name, final String description, final Path file, final Charset charset,
                    final TemplateKind kind) throws IOException {
        this.name = name;
        this.description = description;
        this.file = file;
        this.charset = charset;
        this.kind = kind;
        this.load();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Path getFile() {
        return file;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getTemplateText() {
        return templateText;
    }

    public TemplateKind getKind() {
        return kind;
    }

    public void load() throws IOException {
        try (final BufferedReader reader = Files.newBufferedReader(file, charset)) {
            final StringBuilder buffer = new StringBuilder((int) Files.size(file));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append('\n');
            }
            this.templateText = buffer.toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("file", file)
                .append("charset", charset)
                .append("kind", kind)
                .toString();
    }
}
