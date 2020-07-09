package com.allandequeiroz.random.image.background;

import com.allandequeiroz.random.image.background.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/** Author: Allan de Queiroz */
public class RandomBackgroundManager extends AnAction {

  private ScheduledExecutorService scheduler;

  @Override
  public void actionPerformed(final AnActionEvent e) {
    if (scheduler == null || scheduler.isShutdown()) {
      final PropertiesComponent prop = PropertiesComponent.getInstance();
      scheduler =
          Executors.newScheduledThreadPool(
              1,
              new ThreadFactory() {
                private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

                @Override
                public Thread newThread(final Runnable r) {
                  final Thread thread = defaultFactory.newThread(r);
                  thread.setDaemon(false);
                  return thread;
                }
              });
      scheduler.scheduleWithFixedDelay(
          () -> perform(), 0, getTimeExecution(prop), TimeUnit.SECONDS);
    }
  }

  public void shutdown() {
    if (scheduler != null && !scheduler.isShutdown()) {
      scheduler.shutdownNow();
    }
  }

  public void perform() {
    try {
      final PropertiesComponent prop = PropertiesComponent.getInstance();
      final RandomBackgroundTask task =
          RandomBackgroundTask.Builder.createTask().withProp(prop).build();
      final Project defaultProject = ProjectManager.getInstance().getDefaultProject();
      WriteCommandAction.runWriteCommandAction(defaultProject, task);
    } catch (final Exception ex) {
      NotificationCenter.notify(ex.getMessage());
    }
  }

  private int getTimeExecution(final PropertiesComponent prop) {
    try {
      final String timeExecution = prop.getValue(Settings.TIME_EXECUTION);
      if (StringUtils.isNotEmpty(timeExecution)) {
        return Integer.parseInt(timeExecution);
      }
    } catch (final NumberFormatException e) {
      NotificationCenter.notify("Please, specify a valid integer number as execution time.");
    }
    return Settings.DEFAULT_TIME_EXECUTION;
  }
}
