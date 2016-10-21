package org.nlogo.app.tools

import java.awt.Frame
import java.awt.event.ActionEvent
import javax.swing.{ AbstractAction, Action }

import org.nlogo.api.PreviewCommands
import org.nlogo.core.Model
import org.nlogo.swing.UserAction._
import org.nlogo.window.{ GraphicsPreviewInterface, PreviewCommandsEditorInterface }
import org.nlogo.workspace.{ AbstractWorkspaceScala, WorkspaceFactory }


object PreviewCommandsEditor {
  val title = "Preview Commands Editor"
  class EditPreviewCommands(
    previewCommandsEditor: => PreviewCommandsEditorInterface,
    workspace:             AbstractWorkspaceScala,
    f:                     () => Model) extends AbstractAction(title) {
      putValue(ActionCategoryKey, ToolsCategory)
      putValue(ActionGroupKey,    ToolsDialogsGroup)
      putValue(Action.ACCELERATOR_KEY, KeyBindings.keystroke('P', withMenu = true, withShift = true))

      override def actionPerformed(actionEvent: ActionEvent): Unit = {
        val model = f()

        workspace.previewCommands = previewCommandsEditor.getPreviewCommands(model, workspace.getModelPath)
      }
    }
}

  import PreviewCommandsEditor._
class PreviewCommandsEditor(
  owner: Frame,
  workspaceFactory: WorkspaceFactory,
  graphicsPreview: GraphicsPreviewInterface)
  extends PreviewCommandsEditorInterface {
    val title = PreviewCommandsEditor.title
  def getPreviewCommands(model: Model, modelPath: String): PreviewCommands = {
    val dialog = new PreviewCommandsDialog(
      owner, title, model, modelPath,
      workspaceFactory, graphicsPreview)
    dialog.setVisible(true)
    val previewCommands = dialog.previewCommands
    previewCommands
  }
}
