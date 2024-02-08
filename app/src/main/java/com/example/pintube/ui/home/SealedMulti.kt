package com.example.pintube.ui.home

/** 1 */
const val MULTI_POPULAR = 1

/** 2 */
const val MULTI_CATEGORY = 2


const val MULTI_HEADER = 0

sealed interface SealedMulti {

    val viewType: Int
//    val holder: RecyclerView.ViewHolder  //TODO: (후순위) 실드에 뷰홀더 통합시킬 수 있을까
data object Header: SealedMulti {
    override val viewType: Int
        get() = MULTI_HEADER

}

    data class Popular(
        val videoAdapter: PopularVideoAdapter
    ) : SealedMulti {
        override val viewType: Int = MULTI_POPULAR
//        override val holder: RecyclerView.ViewHolder = object : RecyclerView.ViewHolder() {}
    }

    data class Category(
        val categoryAdapter: CategoryAdapter,
        val videoAdapter: CategoryVideoAdapter,
    ) : SealedMulti {
        override val viewType: Int = MULTI_CATEGORY
    }

}
