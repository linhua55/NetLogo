// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.window

import javax.swing.{Action, KeyStroke}
import javax.swing.text.TextAction

import org.nlogo.editor.EditorArea

class CodeEditor(rows: Int, columns: Int,
                 font: java.awt.Font,
                 enableFocusTraversalKeys: Boolean,
                 listener: java.awt.event.TextListener,
                 colorizer: org.nlogo.editor.Colorizer,
                 enableHighlightCurrentLine: Boolean = false,
                 actionMap: Map[KeyStroke, TextAction] = EditorArea.emptyMap,
                 actions: Seq[Action] = EditorArea.emptySeq) extends
  EditorArea(rows, columns,
    font, enableFocusTraversalKeys, listener, colorizer, enableHighlightCurrentLine, actionMap, actions)
