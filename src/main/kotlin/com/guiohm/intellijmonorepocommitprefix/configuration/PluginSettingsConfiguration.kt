package com.guiohm.intellijmonorepocommitprefix.configuration

import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

class PluginSettingsConfiguration : SearchableConfigurable {

    private lateinit var pluginSettingsConfigurationPanel: PluginSettingsConfigurationPanel
    private val pluginSettingsState
        get() = PluginSettingsState.instance.state

    override fun createComponent(): JComponent? {
        pluginSettingsConfigurationPanel = PluginSettingsConfigurationPanel()
        return pluginSettingsConfigurationPanel.mainPanel
    }

    override fun isModified(): Boolean {
        return pluginSettingsConfigurationPanel.defaultPrefixField.text != pluginSettingsState.defaultPrefix
                || pluginSettingsConfigurationPanel.wrapLeftField.text != pluginSettingsState.wrapLeft
                || pluginSettingsConfigurationPanel.wrapRightField.text != pluginSettingsState.wrapRight
    }

    override fun apply() {
        pluginSettingsState.defaultPrefix = pluginSettingsConfigurationPanel.defaultPrefixField.text
        pluginSettingsState.wrapLeft = pluginSettingsConfigurationPanel.wrapLeftField.text
        pluginSettingsState.wrapRight = pluginSettingsConfigurationPanel.wrapRightField.text
    }

    override fun disposeUIResources() {
        pluginSettingsConfigurationPanel.dispose()
    }

    override fun getDisplayName(): String {
        return "Mono repo/Jira commit prefix"
    }

    override fun getId(): String {
        return "preferences.intellijmonorepocommitprefix"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return pluginSettingsConfigurationPanel.getPreferredFocusedComponent()
    }

    override fun reset() {
        pluginSettingsConfigurationPanel.defaultPrefixField.text = pluginSettingsState.defaultPrefix
        pluginSettingsConfigurationPanel.wrapLeftField.text = pluginSettingsState.wrapLeft
        pluginSettingsConfigurationPanel.wrapRightField.text = pluginSettingsState.wrapRight
    }
}
