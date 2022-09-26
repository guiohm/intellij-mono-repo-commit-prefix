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
        val branchName = getCurrentBranchName()

        return createCommitMessage(branchName, oldCommitMessage, config.wrapLeft, config.wrapRight, config.defaultPrefix)
    }
    
    private fun getCurrentBranchName(): String {
        val repositoryManager = getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch
        repositoryManager.dispose()
        return branch?.name ?: ""
    }

    // kept public for ease of testing
    fun createCommitMessage(
        branchName: String, oldCommitMessage: String?, wrapLeft: String, wrapRight: String, defaultPrefix: String): String? {

        val match = branchNamePattern.matcher(branchName)
        val newPrefix = if (match.find()) match.group(0) else defaultPrefix

        if (newPrefix.isBlank())
            return oldCommitMessage
        
        val wRight = wrapRight.ifBlank { ": " }

        if (oldCommitMessage.isNullOrBlank())
            return wrapLeft + newPrefix + wRight

        // If there is already a commit message with a matching prefix only replace the prefix
        val matcher = prefixPattern.matcher(oldCommitMessage)
        if (matcher != null && matcher.find() &&
            oldCommitMessage.substring(0, matcher.start()).trim() == wrapLeft &&
            oldCommitMessage.substring(matcher.end(), matcher.end() + wRight.length) == wRight
        ) {
            val start: String = oldCommitMessage.substring(0, matcher.start())
            val end: String = oldCommitMessage.substring(matcher.end() + wRight.length)
            return start + newPrefix + wRight + end
        }

        // handle custom prefix (nothing to do in this case)
        if (oldCommitMessage.startsWith(wrapLeft + newPrefix + wRight)) {
            return oldCommitMessage
        }

        return wrapLeft + newPrefix + wRight + oldCommitMessage
    }

}
