package com.allandequeiroz.random.image.background.ui;

import com.allandequeiroz.random.image.background.NotificationCenter;
import com.allandequeiroz.random.image.background.RandomBackgroundManager;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginStateListener;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectManagerImpl;
import com.intellij.openapi.startup.StartupActivity;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

/** Author: Allan de Queiroz */
public class Settings extends ProjectManagerImpl
    implements Configurable, StartupActivity, DumbAware {

  public static final int DEFAULT_TIME_EXECUTION = 300;

  public static final String FOLDER = "BackgroundImagesFolder";
  public static final String TIME_EXECUTION = "BackgroundImagesTimeExecution";
  public static final String OPACITY = "BackgroundImagesOpacity";
  public static final String DISABLED = "BackgroundImagesDisabled";

  private JTextField imageFolder;
  private JPanel rootPanel;
  private JButton chooser;
  private JTextField timeExecution;
  private JSlider opacity;
  private JCheckBox disabled;

  @Nls
  @Override
  public String getDisplayName() {
    return "Background Image";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    chooser.addActionListener(
        e -> {
          final JFileChooser fc = new JFileChooser();
          final String current = imageFolder.getText();
          if (!current.isEmpty()) {
            fc.setCurrentDirectory(new File(current));
          }
          fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          fc.showOpenDialog(rootPanel);

          final File file = fc.getSelectedFile();
          final String path = file == null ? StringUtils.EMPTY : file.getAbsolutePath();
          imageFolder.setText(path);
        });
    return rootPanel;
  }

  @Override
  public boolean isModified() {
    final PropertiesComponent prop = PropertiesComponent.getInstance();
    final String storedFolder =
        Optional.ofNullable(prop.getValue(FOLDER)).orElse(StringUtils.EMPTY);
    final String storedTimeExecution =
        Optional.ofNullable(prop.getValue(TIME_EXECUTION)).orElse(StringUtils.EMPTY);
    final int storedOpacity = getStoredOpacity(prop);
    final boolean storedDisabledOption = prop.getBoolean(DISABLED);

    final String uiFolder = imageFolder.getText();
    final String uiTimeExecution = timeExecution.getText();
    final int uiOpacity = opacity.getValue();
    final boolean isDisabled = disabled.isSelected();

    return !storedFolder.equals(uiFolder)
        || !storedTimeExecution.equals(uiTimeExecution)
        || storedOpacity != uiOpacity
        || storedDisabledOption != isDisabled;
  }

  public static int getStoredOpacity(final PropertiesComponent prop) {
    return Integer.parseInt(Optional.ofNullable(prop.getValue(OPACITY)).orElse("50"));
  }

  @Override
  public void runActivity(@NotNull final Project project) {
    final PropertiesComponent prop = PropertiesComponent.getInstance();
    if (!prop.getBoolean(DISABLED)) {
      ActionManager.getInstance().getAction("randomBackgroundImage").actionPerformed(null);
    }

    com.intellij.ide.plugins.PluginInstaller.addStateListener(
        new PluginStateListener() {
          @Override
          public void install(@NotNull final IdeaPluginDescriptor ideaPluginDescriptor) {
            NotificationCenter.notify("Plugin installation notification");
          }

          @Override
          public void uninstall(@NotNull final IdeaPluginDescriptor ideaPluginDescriptor) {
            disable();
          }
        });
  }

  @Override
  public void apply() {
    final PropertiesComponent prop = PropertiesComponent.getInstance();
    prop.setValue(FOLDER, imageFolder.getText());
    prop.setValue(TIME_EXECUTION, timeExecution.getText());
    prop.setValue(OPACITY, String.valueOf(this.opacity.getValue()));
    prop.setValue(DISABLED, disabled.isSelected());

    if (disabled.isSelected()) {
      disable();
    } else {
      enable();
    }
  }

  private void enable() {
    ActionManager.getInstance().getAction("randomBackgroundImage").actionPerformed(null);
  }

  private void disable() {
    ((RandomBackgroundManager) ActionManager.getInstance().getAction("randomBackgroundImage"))
        .shutdown();
    ActionManager.getInstance().getAction("clearBackgroundImage").actionPerformed(null);
  }

  @Override
  public void reset() {
    final PropertiesComponent prop = PropertiesComponent.getInstance();
    final int storedOpacity = getStoredOpacity(prop);
    opacity.setValue(storedOpacity);
    imageFolder.setText(prop.getValue(FOLDER));
    timeExecution.setText(prop.getValue(TIME_EXECUTION));
    disabled.setSelected(prop.getBoolean(DISABLED));
  }

  @Override
  public void disposeUIResources() {}
}
