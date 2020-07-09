package com.allandequeiroz.random.image.background;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

/** Author: Allan de Queiroz */
public class ClearBackground extends AnAction {

  @Override
  public void actionPerformed(final AnActionEvent e) {
    final PropertiesComponent prop = PropertiesComponent.getInstance();
    prop.setValue(IdeBackgroundUtil.EDITOR_PROP, null);
    prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
    IdeBackgroundUtil.repaintAllWindows();
  }
}
