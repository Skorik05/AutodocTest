package com.skorik05.autodoctest.model.utils

fun parseHeader(header : String) : PaginationLinks {
    var currentPage = 1
    var firstPageNum : Int? = null
    var prevPageNum: Int? = null
    var nextPageNum : Int? = null
    var lastPageNum : Int? = null
    var totalPagesNum: Int

    val linkTypes = header.split(", ")

    for (linkType in linkTypes) {
        val linkParams = linkType.split(" ")
        val pageNum = linkParams[0].split("<", ">")[1].split("&page=")[1]
        when(linkParams[1].split('"')[1]) {
            "first" -> firstPageNum = pageNum.toIntOrNull()
            "prev" -> prevPageNum = pageNum.toIntOrNull()
            "next" -> nextPageNum = pageNum.toIntOrNull()
            "last" -> lastPageNum = pageNum.toIntOrNull()
        }

    }
    if (nextPageNum != null) {
        currentPage = nextPageNum -1
    } else if (prevPageNum != null) {
        currentPage = prevPageNum + 1
    }

    if (lastPageNum != null) {
        totalPagesNum = lastPageNum
    } else {
        totalPagesNum = currentPage
    }
    return PaginationLinks(currentPage, firstPageNum, prevPageNum, nextPageNum, lastPageNum, totalPagesNum)
}