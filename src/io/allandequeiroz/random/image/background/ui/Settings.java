package io.allandequeiroz.random.image.background.ui;

import io.allandequeiroz.random.image.background.ScheduledExecutorServiceHandler;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
public class Settings implements Configurable {

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
   public String getHelpTopic() {
      return null;
   }

   @Nullable
   @Override
   public JComponent createComponent() {
      chooser.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            String current = imageFolder.getText();
            if (!current.isEmpty()) {
               fc.setCurrentDirectory(new File(current));
            }
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.showOpenDialog(rootPanel);

            File file = fc.getSelectedFile();
            String path = file == null
                  ? ""
                  : file.getAbsolutePath();
            imageFolder.setText(path);
         }
      });
      return rootPanel;
   }

   @Override
   public boolean isModified() {
      PropertiesComponent prop = PropertiesComponent.getInstance();
      String storedFolder = prop.getValue(FOLDER);
      String storedTimeExecution = prop.getValue(TIME_EXECUTION);
      int storedOpacity = getStoredOpacity(prop);
      boolean storedDisabledOption = prop.getBoolean(DISABLED);

      String uiFolder = imageFolder.getText();
      String uiTimeExecution = timeExecution.getText();
      int uiOpacity = opacity.getValue();
      boolean isDisabled = disabled.isSelected();

      if (storedFolder == null) {
         storedFolder = "";
      }
      if (storedTimeExecution == null) {
         storedTimeExecution = "";
      }

      return !storedFolder.equals(uiFolder) || !storedTimeExecution.equals(uiTimeExecution) || storedOpacity != uiOpacity || storedDisabledOption != isDisabled;
   }

   public static int getStoredOpacity(PropertiesComponent prop) {
      String storedOpacity = prop.getValue(OPACITY);
      try {
         return Integer.valueOf(storedOpacity);
      } catch (NumberFormatException e) {
         //No problem here, we have no previous value
      }
      return 50;
   }

   @Override
   public void apply() throws ConfigurationException {
      PropertiesComponent prop = PropertiesComponent.getInstance();
      prop.setValue(FOLDER, imageFolder.getText());

      String timeExecutionValue = timeExecution.getText();
      prop.setValue(TIME_EXECUTION, timeExecutionValue);

      int opcity = opacity.getValue();
      prop.setValue(OPACITY, String.valueOf(opcity));

      boolean isDisabled = disabled.isSelected();
      prop.setValue(DISABLED, isDisabled);

      ScheduledExecutorServiceHandler.shutdownExecution();

      if(isDisabled) {
         ActionManager.getInstance().getAction("clearBackgroundImage").actionPerformed(null);
      }else {
         ActionManager.getInstance().getAction("randomBackgroundImage").actionPerformed(null);
      }
   }

   @Override
   public void reset() {
      PropertiesComponent prop = PropertiesComponent.getInstance();
      int storedOpacity = getStoredOpacity(prop);
      opacity.setValue(storedOpacity);
      imageFolder.setText(prop.getValue(FOLDER));
      timeExecution.setText(prop.getValue(TIME_EXECUTION));
      disabled.setSelected(prop.getBoolean(DISABLED));
   }

   @Override
   public void disposeUIResources() {
   }
}
