package com.ypp.docsreader.model.cmd

class EmptyJSCmd : JSCmd() {
    override val hideOthers: String
        get() = ""
    override val resize: String
        get() = ""
}