package com.ypp.docsreader.model.cmd

class JSCmdFactory {
    companion object {
        fun getCmdByType(type: String): JSCmd {
            return when (type) {
                "kotlin" -> KotlinJSCmd()
                else -> EmptyJSCmd()
            }
        }
    }
}