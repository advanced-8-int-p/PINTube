package com.example.pintube.ui.home

sealed interface SealedMulti {

//    enum class Type {
//        MULTI_POPULAR,
//        MULTI_CATEGORY,
//        MULTI_VIDEO,
//        MULTI_HEADER,
//        MULTI_LOADING,
//    }

    data object Type {
        const val popular = 1
        const val category = 2
        const val video = 3
        const val header = 4
        const val loading = 5
    }

    val viewType: Int

    data object Header : SealedMulti {
        override val viewType: Int = Type.header
    }

    data class Popular(
        val videoAdapter: PopularVideoAdapter
    ) : SealedMulti {
        override val viewType: Int = Type.popular
    }

    data class Category(
        val categoryAdapter: CategoryAdapter,
    ) : SealedMulti {
        override val viewType: Int = Type.category
    }

    data class Video(
        val videoItemData: VideoItemData,
    ) : SealedMulti {
        override val viewType: Int = Type.video
    }

    data object Loading : SealedMulti {
        override val viewType: Int = Type.loading
    }

}
