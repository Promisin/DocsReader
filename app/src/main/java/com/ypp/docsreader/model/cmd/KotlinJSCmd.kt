package com.ypp.docsreader.model.cmd

class KotlinJSCmd : JSCmd() {
    override val hideOthers: String
        get() = "javascript:function hideAside() {" +
                "document.getElementsByTagName('aside')[0].style.display='none';" +
                "document.getElementsByTagName('header')[0].style.display='none';" +
                "document.getElementsByTagName('footer')[0].style.display='none';" +
                "document.querySelector('.docs-nav-new').style.display='none';" +
                "document.querySelector('.search-popup').style.display='none';" +
                "document.querySelector('.page-link-to-github').style.display='none';}"

    override val executeHide: String
        get() = "javascript:hideAside();"
}