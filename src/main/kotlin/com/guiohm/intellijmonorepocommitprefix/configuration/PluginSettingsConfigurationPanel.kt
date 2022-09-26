package com.guiohm.intellijmonorepocommitprefix.configuration

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.util.whenTextChanged
import com.intellij.openapi.ui.ComponentValidator
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.Font
import java.util.function.Supplier
import java.util.regex.Pattern
import javax.swing.JComponent
import javax.swing.JPanel


class PluginSettingsConfigurationPanel : Disposable {
    private val wrapPattern = Pattern.compile(".*[a-zA-Z0-9].*")
    private val emptyExampleText = "no prefix"
    private val exampleMessages = arrayOf(
        "apparently i did something…",
        "Done, to whoever merges this, good luck.",
        "Please forgive me",
        "Merging ‘WIP: Do Not Merge This Branch’ Into Master",
        "We’ll figure it out on Monday",
        "pr is failing but merging anyways, because I am an admin"
    )

    var defaultPrefixField: JBTextField = JBTextField(12)
    var wrapLeftField: JBTextField = JBTextField(5)
    var wrapRightField: JBTextField = JBTextField(5)
    private var example: JBLabel= JBLabel(emptyExampleText, UIUtil.ComponentStyle.SMALL, UIUtil.FontColor.BRIGHTER)
    var mainPanel: JPanel

    init {
        defaultPrefixField.document.whenTextChanged(this) {
            val op: Any? = if (defaultPrefixField.text.length > 12) "warning" else null
            defaultPrefixField.putClientProperty("JComponent.outline", op)
            defaultPrefixField.toolTipText = if (op == null) null else "You probably should not put a prefix thing long over here..."
            updateExample()
        }

        val notes = JBLabel(
            "<html><body>This is used when no JIRA task has been detected in current branch name." +
                "<br>Leave blank if you don't need this feature</body></html>",
            UIUtil.ComponentStyle.SMALL, UIUtil.FontColor.BRIGHTER)
        notes.border = JBUI.Borders.emptyLeft(3)

        example.font = Font.getFont(Font.MONOSPACED)

        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Wrap left"), addWrapValidator(wrapLeftField), 1, false)
            .addLabeledComponent(JBLabel("Wrap right"), addWrapValidator(wrapRightField), 1, false)
            .addSeparator(4)
            .addLabeledComponent(JBLabel("Default prefix"), defaultPrefixField, 4, false)
            .addComponentToRightColumn(notes)
            .addVerticalGap(12)
            .addLabeledComponent(JBLabel("Example commit message"), example, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    private fun addWrapValidator(field: JBTextField): JBTextField {
        ComponentValidator(this).withValidator(object: Supplier<ValidationInfo?> {
            override fun get(): ValidationInfo? {
                if (wrapPattern.matcher(field.text).matches()) {
                    return ValidationInfo("Only non-alphanumerical characters allowed", field)
                }
                updateExample()
                return null
            }
        }).installOn(field).andRegisterOnDocumentListener(field)
        return field
    }

    fun getPreferredFocusedComponent(): JComponent {
        return defaultPrefixField
    }

    private fun updateExample() {
        val prefix = defaultPrefixField.text.ifEmpty { "ABCD-1234" }
        val wrapRight = wrapRightField.text.ifEmpty { ": " }
        example.text = wrapLeftField.text + prefix + wrapRight + getExampleCommitMessage()
    }

    private fun getExampleCommitMessage(): String {
        return exampleMessages.random()
    }

    override fun dispose() {
        // used to trigger disposal of ComponentValidator. Probably not the proper way
    }
}


