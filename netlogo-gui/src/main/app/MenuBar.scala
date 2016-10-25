// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.app

import javax.swing.{ Action, JMenu, JMenuBar }

import org.nlogo.core.I18N
import org.nlogo.editor.EditorMenu
import org.nlogo.swing.{ TabsMenu, UserAction },
  UserAction.{ ActionCategoryKey, FileCategory, HelpCategory, TabsCategory, ToolsCategory }

class MenuBar(val fileMenu: FileMenu,
  editMenu:             EditMenu,
  isApplicationWide:    Boolean)
  extends JMenuBar
  with EditorMenu
  with UserAction.Menu {

  val tabsMenu = new TabsMenu(I18N.gui.get("menu.tabs"))
  val toolsMenu = new ToolsMenu

  add(fileMenu)
  add(editMenu)
  add(toolsMenu)
  add(new ZoomMenu)
  add(tabsMenu)

  private var helpMenu = Option.empty[HelpMenu]

  private var categoryMenus: Map[String, UserAction.Menu] = Map(
    FileCategory  -> fileMenu,
    ToolsCategory -> toolsMenu,
    TabsCategory  -> tabsMenu
  )

  override def setHelpMenu(newHelpMenu: JMenu): Unit = {
    newHelpMenu match {
      case hm: HelpMenu =>
        helpMenu = Some(hm)
        categoryMenus = categoryMenus + (HelpCategory -> hm)
      case _ =>
    }
    if (isApplicationWide) {
      try super.setHelpMenu(newHelpMenu)
      catch{
        // if not implemented in this VM (e.g. 1.8 on Mac as of right now),
        // then oh well - ST 6/23/03, 8/6/03 - RG 10/21/16
        case e: Error => org.nlogo.api.Exceptions.ignore(e)
      }
    }
  }

  //TODO: This method was an attempted refactor that didn't work out. It should be removed
  def editActions(actionGroups: Seq[Seq[Action]]): Unit = {
  }


  override def helpActions(actions: Seq[Action]): Unit = {
    helpMenu.foreach(_.addEditorActions(actions))
  }


  def offerAction(action: javax.swing.Action): Unit = {
    val categoryKey = action.getValue(ActionCategoryKey) match {
      case s: String => s
      case _ => ""
    }
    categoryMenus.get(categoryKey).foreach(_.offerAction(action))
  }

  def revokeAction(action: Action): Unit = {
    val categoryKey = action.getValue(ActionCategoryKey) match {
      case s: String => s
      case _ => ""
    }
    categoryMenus.get(categoryKey).foreach(_.revokeAction(action))
  }
}

