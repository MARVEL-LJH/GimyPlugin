package com.LJH.mediabox.plugin

import android.util.Log
import com.su.mediabox.pluginapi.components.IVideoPlayPageDataComponent
import com.su.mediabox.pluginapi.data.VideoPlayMedia
import com.su.mediabox.pluginapi.util.WebUtilIns
import com.LJH.mediabox.plugin.Const.host
import com.LJH.mediabox.plugin.Const.ua
import com.LJH.util.JsoupUtil
import com.su.mediabox.pluginapi.Constant
import com.su.mediabox.pluginapi.util.AppUtil
import com.su.mediabox.pluginapi.util.TextUtil.urlDecode
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.io.File

class VideoPlayPageDataComponent : IVideoPlayPageDataComponent {
    override suspend fun getVideoPlayMedia(episodeUrl: String): VideoPlayMedia {
        val url = host + episodeUrl
        val document = JsoupUtil.getDocument(url)

        //解析链接
        val videoUrl = withContext(Dispatchers.Main) {
            val iframeUrl = withTimeoutOrNull(10 * 1000) {
                WebUtilIns.interceptResource(
                    url, "(.*)\\.m3u8(.*)",
                    userAgentString = ua
                )
            } ?: ""
            async {
                when {
                    iframeUrl.isBlank() -> iframeUrl
                    iframeUrl.contains("jcplayer") -> iframeUrl.substringAfter("url=")
                        .substringBefore("&").urlDecode()
                    else -> {}
                }
            }
        }


        //剧集名
        val name = withContext(Dispatchers.Default) {
            async {
                document.getElementsByClass("mac_history_set2").attr("data-playname")
            }
        }
        return VideoPlayMedia(name.await(), videoUrl.await() as String)
    }

}