package comeon.model;

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

  private final String templateText;

  private final TemplateKind kind;

  public Template(final String name, final String description, final Path file, final Charset charset,
      final String templateText, final TemplateKind kind) {
    super();
    this.name = name;
    this.description = description;
    this.file = file;
    this.charset = charset;
    this.templateText = templateText;
    this.kind = kind;
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

  public static String read(final Path path, final Charset charset) throws IOException {
    try (final BufferedReader reader = Files.newBufferedReader(path, charset)) {
      final StringBuilder buffer = new StringBuilder((int) Files.size(path));
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }
      return buffer.toString();
    }
  }
}
