// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.ide

import java.awt.event.{ ActionEvent, KeyEvent }
import javax.swing.Action.ACCELERATOR_KEY
import javax.swing.text.{ Document, JTextComponent }

import org.nlogo.core.I18N
import org.nlogo.editor.RichDocument._
import org.nlogo.swing.UserAction,
  UserAction.{ ActionCategoryKey, ActionGroupKey, EditCategory, EditFormatGroup, KeyBindings },
    KeyBindings.keystroke

object ShiftActions {
  class Right extends DocumentAction(I18N.gui.get("menu.edit.shiftRight")) with FocusedOnlyAction {
    putValue(ACCELERATOR_KEY, keystroke(KeyEvent.VK_CLOSE_BRACKET, withMenu = true))
    putValue(ActionGroupKey,    EditFormatGroup)
    putValue(ActionCategoryKey, EditCategory)

    override def perform(component: JTextComponent, document: Document, e: ActionEvent): Unit = {
      val (startLine, endLine) =
        document.selectionLineRange(component.getSelectionStart, component.getSelectionEnd)
      document.insertBeforeLinesInRange(startLine, endLine, " ")
    }
  }

  class Left extends DocumentAction(I18N.gui.get("menu.edit.shiftLeft")) with FocusedOnlyAction {
    putValue(ACCELERATOR_KEY,   keystroke(KeyEvent.VK_OPEN_BRACKET, withMenu = true))
    putValue(ActionGroupKey,    EditFormatGroup)
    putValue(ActionCategoryKey, EditCategory)

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

  class LeftTab extends DocumentAction(I18N.gui.get("menu.edit.shiftTab")) with FocusedOnlyAction {
    putValue(ACCELERATOR_KEY,   keystroke(KeyEvent.VK_TAB, withShift = true))

    override def perform(component: JTextComponent, document: Document, e: ActionEvent): Unit = {
      val (startLine, endLine) =
        document.selectionLineRange(component.getSelectionStart, component.getSelectionEnd)
      for {
        lineNum <- startLine to endLine
      } {
        val lineStart = document.lineToStartOffset(lineNum)
        if (lineStart != -1) {
          val text = document.getText(lineStart, 2)
          text.length match {
            case 0 =>
            case 1 if text.charAt(0) == ' ' => document.remove(lineStart, 1)
            case _ =>
              if (text.charAt(0) == ' ' && text.charAt(1) == ' ')
                document.remove(lineStart, 2)
              else if (text.charAt(0) == ' ')
                document.remove(lineStart, 1)
          }
        }
      }
    }
  }
}
