package com.guiohm.intellijmonorepocommitprefix

import com.guiohm.intellijmonorepocommitprefix.services.MyProjectService
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class MyPluginTest : BasePlatformTestCase() {

    fun testBranchNameKeepLastCommit() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-42-please_fix_me", "yikes", "[", "] ", "DEF")
        assertEquals("[ABC-42] yikes", msg)
    }
    fun testBranchNameKeepLastCommitButRemoveItsId() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-42-please_fix_me", "[ABC-1024] yikes", "[", "] ", "DEF")
        assertEquals("[ABC-42] yikes", msg)
    }
    fun testBranchNameTaskIdMiddle() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("bug/ABC-42-please_fix_me", "yikes", "", "", "DEF")
        assertEquals("ABC-42: yikes", msg)
    }
    fun testBranchNameMultiTaskIds() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-42-followup_CDE-73", "KDE-144: I broke it", "", "", "DEF")
        assertEquals("ABC-42: I broke it", msg)
    }
    fun testBranchNameTaskIdAtTheEnd() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-followup_CDE-73", "KDE-144: I broke it", "", "", "DEF")
        assertEquals("CDE-73: I broke it", msg)
    }
    fun testUseDefaultPrefixNoTaskId() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("master", "yikes", "[", "] ", "DEF")
        assertEquals("[DEF] yikes", msg)
    }
    fun testDefaultPrefixTaskIdLastCommit() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-please_fix_me", "KDE-144: I broke it", "", "", "DEF")
        assertEquals("DEF: I broke it", msg)
    }
    fun testUseLastCommit() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-please_fix_me", "KDE-144: I broke it", "", "", "")
        assertEquals("KDE-144: I broke it", msg)
    }
    fun testNoDefaultFromBranchName() {
        val svc = MyProjectService(project)
        val msg = svc.createCommitMessage("ABC-42please_fix_me", "KDE-144: I broke it", "", "", "")
        assertEquals("ABC-42: I broke it", msg)
    }
}
