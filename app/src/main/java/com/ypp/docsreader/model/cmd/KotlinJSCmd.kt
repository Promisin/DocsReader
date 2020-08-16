package com.ypp.docsreader.model.cmd

class KotlinJSCmd : JSCmd() {
    override val hideOthers: String
        get() = "javascript:function hideAside() {" +
                "document.getElementsByTagName('aside')[0].style.display='none';" +
                "document.getElementsByTagName('header')[0].style.display='none';" +
                "document.getElementsByTagName('footer')[0].style.display='none';" +
                "document.querySelector('.docs-nav-new').style.display='none';" +
                "document.querySelector('.search-popup').style.display='none';" +
                "document.querySelector('.page-link-to-github').style.display='none';}" +
                "javascript:hideAside();"

    override val resize: String
        get() = "javascript:function resize() {" +
                "var parent = document.getElementsByTagName('head')[0];" +
                "var style = document.createElement('style');" +
                "style.type = 'text/css';" +
                "style.innerHTML = '.page-content {width: 100%;}';" +
                "parent.appendChild(style);}" +
                "javascript:resize();"
}