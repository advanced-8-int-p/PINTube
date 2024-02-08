package com.example.pintube.data.repository

import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.ContentsDAO
import com.example.pintube.data.local.dao.SearchDAO
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val contentsDAO: ContentsDAO,
    private val commentDAO: CommentDAO,
    private val channelDAO: ChannelDAO,
    private val searchDAO: SearchDAO,
) {

}