// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.editor

import java.awt.event._
import javax.swing.Action
import javax.swing.text._
import javax.swing.text.DefaultEditorKit.{CutAction, CopyAction, PasteAction, InsertContentAction}

import RichDocument._

object Actions {

  val commentToggleAction = new CommentToggleAction()
  val shiftLeftAction = new ShiftLeftAction()
  val shiftRightAction = new ShiftRightAction()
  val tabKeyAction = new TabKeyAction()
  val shiftTabKeyAction = new ShiftTabKeyAction()
  val CUT_ACTION = new CutAction()
  val COPY_ACTION = new CopyAction()
  val PASTE_ACTION = new PasteAction()
  val DELETE_ACTION = new InsertContentAction(){ putValue(Action.ACTION_COMMAND_KEY, "")  }

  /// default editor kit actions
  private val actionMap = new DefaultEditorKit().getActions.map{ a => (a.getValue(Action.NAME), a) }.toMap
  def getDefaultEditorKitAction(name:String) = actionMap(name)
  val SELECT_ALL_ACTION = getDefaultEditorKitAction(DefaultEditorKit.selectAllAction)

  def setEnabled(enabled:Boolean){
    List(commentToggleAction,shiftLeftAction,shiftRightAction).foreach(_.setEnabled(enabled))
  }

  class TabKeyAction extends MyTextAction("tab-key", _.indentSelection() )
  class ShiftTabKeyAction extends MyTextAction("shift-tab-key", e => { e.shiftLeft(); e.shiftLeft() })
  class CommentToggleAction extends MyTextAction("toggle-comment", _.toggleComment())
  def quickHelpAction(colorizer: Colorizer, i18n: String => String) =
    new MyTextAction(i18n("tabs.code.rightclick.quickhelp"),
      e => e.getHelpTarget(e.getSelectionStart).foreach(t => colorizer.doHelp(e, t)))
  def mouseQuickHelpAction(colorizer: Colorizer, i18n: String => String) =
    new MyTextAction(i18n("tabs.code.rightclick.quickhelp"),
      e => e.getHelpTarget(e.getMousePos).foreach(t => colorizer.doHelp(e, t)))
  class MyTextAction(name:String, f: EditorArea => Unit) extends TextAction(name) {
    override def actionPerformed(e:ActionEvent){
      val component = getTextComponent(e)
      if(component.isInstanceOf[EditorArea]) f(component.asInstanceOf[EditorArea])
    }
  }

  abstract class DocumentAction(name: String) extends TextAction(name) {
    override def actionPerformed(e: ActionEvent): Unit = {
      val component = getTextComponent(e)
      try {
        perform(component, component.getDocument, e)
      } catch {
        case ex: BadLocationException => throw new IllegalStateException(ex)
      }
    }

    def perform(component: JTextComponent, document: Document, e: ActionEvent): Unit
  }

  class ShiftLeftAction extends DocumentAction("shift-line-left") {
    override def perform(component: JTextComponent, document: Document, e: ActionEvent): Unit = {
      val (startLine, endLine) =
        document.selectionLineRange(component.getSelectionStart, component.getSelectionEnd)

      for {
        lineNum <- startLine to endLine
      } {
        val lineStart = document.lineToStartOffset(lineNum)
        if (lineStart != -1) {
          val text = document.getText(lineStart, 1)
          if (text.length > 0 && text.charAt(0) == ' ') {
            document.remove(lineStart, 1)
          }
        }
      }
    }
  }

  class ShiftRightAction extends DocumentAction("shift-line-right") {
    override def perform(component: JTextComponent, document: Document, e: ActionEvent): Unit = {
      val (startLine, endLine) =
        document.selectionLineRange(component.getSelectionStart, component.getSelectionEnd)
      document.insertBeforeLinesInRange(startLine, endLine, " ")
    }
  }
}
