package com.skorik05.autodoctest.model.utils

data class PaginationLinks (
        val currentPage : Int? = null,
        val firstPageNum : Int? = null,
        val prevPageNum : Int? = null,
        val nextPageNum : Int? = null,
        val lastPageNum : Int? = null,
        val totalPagesNum : Int
)