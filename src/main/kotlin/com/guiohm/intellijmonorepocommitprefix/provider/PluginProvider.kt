package com.guiohm.intellijmonorepocommitprefix.provider

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import com.guiohm.intellijmonorepocommitprefix.services.MyProjectService

class PluginProvider : CommitMessageProvider {

    override fun getCommitMessage(forChangelist: LocalChangeList, project: Project): String? {
        val oldCommitMessage = forChangelist.comment
        val projectService = project.getService(MyProjectService::class.java)
        val newCommitMessage = projectService?.getCommitMessage(oldCommitMessage)

        return if (newCommitMessage.isNullOrBlank()) {
            oldCommitMessage
        } else {
            newCommitMessage
        }
    }
}
