// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.editor

import javax.swing.{ JViewport, SwingUtilities }
import javax.swing.text.{ Document, EditorKit, JTextComponent }

trait AbstractEditorArea extends JTextComponent {
  def configuration: EditorConfiguration

  def enableBracketMatcher(enable: Boolean): Unit
  def getEditorKit(): EditorKit
  def setEditorKit(kit: EditorKit): Unit
  def getEditorKitForContentType(contentType: String): EditorKit

  def getText(start: Int, end: Int): String

  def setIndenter(i: Indenter): Unit

  def setSelection(s: Boolean): Unit

  def offsetToLine(doc: Document, line: Int): Int
  def lineToStartOffset(doc: Document, line: Int): Int
  def lineToEndOffset(doc: Document, line: Int): Int

  def containingViewport: Option[JViewport] = {
    SwingUtilities.getAncestorOfClass(classOf[JViewport], this) match {
      case j: JViewport => Some(j)
      case _ => None
    }
  }
}
