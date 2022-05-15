package com.LJH.mediabox.plugin

import com.su.mediabox.pluginapi.components.IVideoPlayPageDataComponent
import com.su.mediabox.pluginapi.data.VideoPlayMedia
import com.su.mediabox.pluginapi.util.WebUtilIns
import com.LJH.mediabox.plugin.Const.host
import com.LJH.mediabox.plugin.Const.ua
import com.LJH.util.JsoupUtil
import kotlinx.coroutines.*

class VideoPlayPageDataComponent : IVideoPlayPageDataComponent {

    override suspend fun getVideoPlayMedia(episodeUrl: String): VideoPlayMedia {
        val url = host + episodeUrl
        val document = JsoupUtil.getDocument(url)

        //解析链接
        val videoUrl = withContext(Dispatchers.Main) {

            async {
                withTimeoutOrNull(10 * 1000) {
                    WebUtilIns.interceptResource(
                        url, "(.*)\\.m3u8(.*)",
                        userAgentString = ua
                    )
                } ?: ""
            }
        }

        //剧集名
        val name = withContext(Dispatchers.Default) {
            async {
                document.select("[class=nav nav-tabs]").select("span").first()?.let {
                    it.text().replace(": ", "")
                } ?: ""
            }
        }

        return VideoPlayMedia(name.await(), videoUrl.await())
    }

}