// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.swing

// TODO i18n lot of work needed here...

import java.awt.event.ActionEvent
import javax.swing.{ Action, AbstractAction, JTabbedPane }
import UserAction._

object TabsMenu {
  def tabAction(tabs: JTabbedPane, index: Int): Action =
    new AbstractAction(tabs.getTitleAt(index)) {
      putValue(ActionCategoryKey, TabsCategory)
      putValue(ActionRankKey,     Double.box(index))
      putValue(Action.ACCELERATOR_KEY, KeyBindings.keystrokeChar(('1' + index).toChar, withMenu = true))
      override def actionPerformed(e: ActionEvent) {
        tabs.setSelectedIndex(index)
      }
    }

  def tabActions(tabs: JTabbedPane): Seq[Action] =
    for (i <- 0 until tabs.getTabCount) yield tabAction(tabs, i)
}

class TabsMenu(name: String, initialActions: Seq[Action]) extends Menu(name) {
  setMnemonic('A')

  initialActions.foreach(offerAction)

  def this(name: String) =
    this(name, Seq())

  def this(name: String, tabs: JTabbedPane) =
    this(name, TabsMenu.tabActions(tabs))
}
