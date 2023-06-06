package com.khusinov.hamrohtaxi.models

data class SearchResponse(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Post>
)