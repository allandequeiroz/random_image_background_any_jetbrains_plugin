package io.allandequeiroz.random.image.background;

import io.allandequeiroz.random.image.background.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.concurrent.TimeUnit;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
public class RandomBackground extends AnAction {

   public RandomBackground() {
      super("Random Background Image");
      actionPerformed(null);
   }

   @Override
   public void actionPerformed(AnActionEvent evt) {
      try {
         PropertiesComponent prop = PropertiesComponent.getInstance();
         RandomBackgroundTask task = RandomBackgroundTask.Builder.createTask().withProp(prop).build();
         int timeExecution = getTimeExecution(prop);

         if (timeExecution != -1) {
            ScheduledExecutorServiceHandler scheduler = ScheduledExecutorServiceHandler.
                  Builder.
                  createScheduler().withTask(task).
                  withInitialDelay(0).
                  withPeriod(timeExecution).
                  withTimeUnit(TimeUnit.SECONDS).
                  build();

            scheduler.scheduleAtFixedRate();
         } else {
            task.run();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private int getTimeExecution(PropertiesComponent prop) {
      try {
         String timeExecution = prop.getValue(Settings.TIME_EXECUTION);
         if (timeExecution.isEmpty()) {
            return -1;
         }
         return Integer.valueOf(timeExecution);
      } catch (NumberFormatException e) {
         NotificationCenter.notice("Please, specify a valid integer number as execution time.");
      }
      return -1;
   }

}
