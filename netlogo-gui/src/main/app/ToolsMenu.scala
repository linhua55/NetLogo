// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.app

/** note that multiple instances of this class may exist as there are now multiple frames that
 each have their own menu bar and menus ev 8/25/05 */

import javax.swing.{ Action, JMenuItem }

import org.nlogo.core.{ AgentKind, I18N }
import org.nlogo.swing.UserAction
import org.nlogo.window.GUIWorkspace

class ToolsMenu(app: App, modelSaver: ModelSaver)
  extends org.nlogo.swing.Menu(I18N.gui.get("menu.tools"))
  with UserAction.Menu {
  implicit val i18nName = I18N.Prefix("menu.tools")

  setMnemonic('T')

  // this will need to be refined to take groups into account
  def offerAction(action: Action): Unit = {
    add(new JMenuItem(action), 0)
  }
}
