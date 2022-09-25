package com.guiohm.intellijmonorepocommitprefix.services

import com.guiohm.intellijmonorepocommitprefix.configuration.PluginSettingsState
import com.intellij.openapi.project.Project
import git4idea.GitUtil.getRepositoryManager
import java.util.regex.Pattern

class MyProjectService(private val project: Project) {

    private val branchNamePattern = Pattern.compile("(?<=\\/)*([A-Z0-9]+-[0-9]+)")
    private val prefixPattern = Pattern.compile("[A-Z0-9]+-[0-9]+")

    fun getCommitMessage(oldCommitMessage: String? = null): String? {

        val config = PluginSettingsState.instance.state
        val repositoryManager = getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch

        val jiraPrefixRegex = branch?.let { branchNamePattern.matcher(it.name) }
        val newPrefix = jiraPrefixRegex?.let{when(it.find()) {
            true -> it.group(0)
            false -> config.defaultPrefix
        }}

        repositoryManager.dispose()

        if (newPrefix.isNullOrBlank())
            return oldCommitMessage

        return createCommitMessage(config, oldCommitMessage, newPrefix)
    }

    private fun createCommitMessage(config: PluginSettingsState.PluginState, oldCommitMessage: String?, newPrefix: String): String? {
        if (oldCommitMessage.isNullOrBlank())
            return config.wrapLeft + newPrefix + config.wrapRight

        // If there is already a commit message with a matching prefix only replace the prefix
        val matcher = prefixPattern.matcher(oldCommitMessage)
        if (matcher != null && matcher.find() &&
            oldCommitMessage.substring(0, matcher.start()).trim() == config.wrapLeft &&
            oldCommitMessage.substring(matcher.end(), matcher.end() + config.wrapRight.length) == config.wrapRight
        ) {
            val start: String = oldCommitMessage.substring(0, matcher.start())
            val end: String = oldCommitMessage.substring(matcher.end() + config.wrapRight.length)
            return start + newPrefix + config.wrapRight + end
        }

        // handle custom prefix (nothing to do in this case)
        if (oldCommitMessage.startsWith(config.wrapLeft + newPrefix + config.wrapRight)) {
            return oldCommitMessage
        }

        return config.wrapLeft + newPrefix + config.wrapRight + oldCommitMessage
    }
}
