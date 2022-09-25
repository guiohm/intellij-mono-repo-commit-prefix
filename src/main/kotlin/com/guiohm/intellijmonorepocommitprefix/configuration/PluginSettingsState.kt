package com.guiohm.intellijmonorepocommitprefix.configuration

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "MonorepCommitStoredConfiguration", storages = [Storage("MonorepCommitStoredConfiguration.xml")])
class PluginSettingsState : PersistentStateComponent<PluginSettingsState.PluginState> {

    var pluginState: PluginState = PluginState()

    override fun getState(): PluginState {
        return pluginState
    }

    override fun loadState(state: PluginState) {
        XmlSerializerUtil.copyBean(state, this.pluginState)
    }

    companion object {
        val instance: PluginSettingsState
            get() = ApplicationManager.getApplication().getService(PluginSettingsState::class.java)
    }

    class PluginState {
        var wrapLeft = ""
        var wrapRight = ""
        var defaultPrefix = ""
    }
}
