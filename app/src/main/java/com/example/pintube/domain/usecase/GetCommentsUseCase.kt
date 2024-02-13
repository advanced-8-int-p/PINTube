package com.example.pintube.domain.usecase

import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localCommentRepository: LocalCommentRepository,
) {
    suspend operator fun invoke(id: String): List<LocalCommentEntity>? {
        var comments = localCommentRepository.findComment(id)

        if (comments.isNullOrEmpty()) {
            apiRepository.getComments(id)?.forEach { item ->
                localCommentRepository.saveComment(item)
            }

            comments = localCommentRepository.findComment(id)
        }
        return comments
    }
}