package io.allandequeiroz.random.image.background;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
public class ClearBackground extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, null);
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
        IdeBackgroundUtil.repaintAllWindows();
    }
}
