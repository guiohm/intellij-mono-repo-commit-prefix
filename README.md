# Intellij plugin: Git (Mono-repo) Commit Prefix

![Build](https://github.com/guiohm/intellij-mono-repo-commit-prefix/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)


<!-- Plugin description -->
This plugin extract a typical JIRA task id (`ABCD-1234`) from the current git branch, and allows using a default prefix 
if nothing is found in the branch name.

I'm using this to automate changelog creation and filtering based on the prefix associated with each project/app,
even when commits were not associated with a particular Jira task.

## Features
* Automatically change the commit prefix if switching branches
* If intellij is suggesting a (previous) commit message, only the prefix will be updated
* Set a default prefix per project (applied when no task id is found in branch name)
* Wrap the prefix as you like

<!-- Plugin description end -->
## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Mono Repo Commit Prefix"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/guiohm/intellij-mono-repo-commit-prefix/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the existing [Idea-commit-prefix plugin](https://github.com/thomasrepnik/idea-commit-prefix-plugin).

