package com.allandequeiroz.random.image.background;

import com.allandequeiroz.random.image.background.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import org.apache.commons.lang.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

/** Author: Allan de Queiroz */
class RandomBackgroundTask implements Runnable {

  private final PropertiesComponent prop;
  private final ImagesHandler imagesHandler;

  private RandomBackgroundTask(final Builder builder) {
    this.prop = builder.prop;
    imagesHandler = new ImagesHandler();
  }

  @Override
  public void run() {
    if (!prop.getBoolean(Settings.DISABLED)) {
      final String folder = prop.getValue(Settings.FOLDER);
      if (StringUtils.isEmpty(folder)) {
        NotificationCenter.notify("Image folder not set");
        return;
      }
      if (Files.notExists(Paths.get(folder))) {
        NotificationCenter.notify("Image folder doesn't exists");
        return;
      }
      final String image = imagesHandler.getRandomImage(folder);
      if (StringUtils.isEmpty(image)) {
        NotificationCenter.notify("No image found");
        return;
      }
      final String storedOpacity = prop.getValue(Settings.OPACITY);
      final String ideaBackgroundImage = String.format("%s,%s", image, storedOpacity);
      prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
      prop.setValue(IdeBackgroundUtil.EDITOR_PROP, ideaBackgroundImage);
      IdeBackgroundUtil.repaintAllWindows();
    }
  }

  public static final class Builder {
    private PropertiesComponent prop;

    private Builder() {}

    public static Builder createTask() {
      return new Builder();
    }

    public Builder withProp(final PropertiesComponent prop) {
      this.prop = prop;
      return this;
    }

    public RandomBackgroundTask build() {
      return new RandomBackgroundTask(this);
    }
  }
}
