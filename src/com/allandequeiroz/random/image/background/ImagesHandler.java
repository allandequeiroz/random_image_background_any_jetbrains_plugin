package com.allandequeiroz.random.image.background;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/** Author: Allan de Queiroz */
class ImagesHandler {

  public String getRandomImage(final String folder) {
    if (StringUtils.isEmpty(folder)) {
      return StringUtils.EMPTY;
    }

    final List<String> images = collectImages(folder);
    return Optional.ofNullable(images.get(ThreadLocalRandom.current().nextInt(images.size())))
        .orElse(StringUtils.EMPTY);
  }

  private List<String> collectImages(final String rootFolder) {
    if (Files.notExists(Paths.get(rootFolder))) {
      return Collections.emptyList();
    }

    try {
      return Files.walk(Paths.get(rootFolder))
          .map(Path::toString)
          .filter(this::isImage)
          .collect(Collectors.toList());
    } catch (final IOException e) {
      NotificationCenter.error(e.getMessage(), e);
    }
    return Collections.emptyList();
  }

  private boolean isImage(final String filePath) {
    try {
      final File file = new File(filePath);
      final URLConnection connection = file.toURI().toURL().openConnection();
      return connection.getContentType().startsWith("image");
    } catch (final IOException e) {
      NotificationCenter.error(e.getMessage(), e);
    }
    return false;
  }
}
