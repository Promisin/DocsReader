package com.ypp.docsreader.model

class MultiListItem {
    var level: Int = 0

    private var index: Int = 0

    var pid: Int = -1

    var isChecked: Boolean = level == 0

    var itemText: String = ""

    var children: ArrayList<MultiListItem> = arrayListOf<MultiListItem>()

    var target: String = ""

    constructor()

    constructor(level: Int) {
        this.level = level
    }

    constructor(level: Int, pid: Int) {
        this.level = level
        this.pid = pid
    }

    constructor(level: Int, pid: Int, isChecked: Boolean, itemText: String) {
        this.level = level
        this.pid = pid
        this.isChecked = isChecked
        this.itemText = itemText
    }

    constructor(level: Int, pid: Int, isChecked: Boolean, itemText: String, target: String) {
        this.level = level
        this.pid = pid
        this.isChecked = isChecked
        this.itemText = itemText
        this.target = target
    }


}