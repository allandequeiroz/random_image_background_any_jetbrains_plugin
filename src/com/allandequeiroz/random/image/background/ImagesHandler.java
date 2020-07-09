package com.allandequeiroz.random.image.background;

import org.apache.commons.lang.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
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

  private final MimetypesFileTypeMap typeMap;

  public ImagesHandler() {
    typeMap = new MimetypesFileTypeMap();
  }

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
      NotificationCenter.notify(e.getMessage());
    }
    return Collections.emptyList();
  }

  private boolean isImage(final String file) {
    final String[] parts = typeMap.getContentType(file).split("/");
    return parts.length != 0 && parts[0].equals("image");
  }
}
