package com.su.sakuraanimeplugin.plugin.util

import android.util.Log
import com.LJH.mediabox.plugin.Const.host
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import com.su.mediabox.pluginapi.action.ClassifyAction
import com.su.mediabox.pluginapi.action.DetailAction
import com.su.mediabox.pluginapi.data.*
import java.net.URL

object ParseHtmlUtil {

    fun getCoverUrl(cover: String, imageReferer: String): String {
        return when {
            cover.startsWith("//") -> {
                try {
                    "${URL(imageReferer).protocol}:$cover"
                } catch (e: Exception) {
                    e.printStackTrace()
                    cover
                }
            }
            cover.startsWith("/") -> {
                //url不全的情况
                host + cover
            }
            else -> cover
        }
    }

    /**
     * 解析搜索/分类下的元素
     *
     * @param element ul的父元素
     */
    fun parseSearchEm(
        element: Element,
        imageReferer: String
    ): List<BaseData> {
        val videoInfoItemDataList = mutableListOf<BaseData>()
        val results: Elements = element.select("[class=clearfix]").select("[class=details-info-min col-md-12 col-sm-12 col-xs-12 clearfix news-box-txt p-0]")
        Log.d("results数据", results.toString())
        for (i in results.indices) {
            var cover = results[i].select("[class=col-md-3 col-sm-4 col-xs-3 news-box-txt-l clearfix]").select("a").attr("data-original")
            cover = getCoverUrl(cover, imageReferer)
            val title = results[i].select("[class=col-md-3 col-sm-4 col-xs-3 news-box-txt-l clearfix]").select("a").attr("title")
            val url = results[i].select("[class=col-md-3 col-sm-4 col-xs-3 news-box-txt-l clearfix]").select("a").attr("href")
            val episode = results[i].select("[class=col-md-9 col-sm-8 col-xs-9 clearfix pb-0]").select("[class=details-info p-0]").select("[class=info clearfix]").select("[class=col-md-12 col-sm-12 col-xs-12 hidden-xs]")[0].ownText()
            val types = results[i].select("[class=col-md-9 col-sm-8 col-xs-9 clearfix pb-0]").select("[class=details-info p-0]").select("[class=info clearfix]").select("[class=col-md-12 col-sm-12 col-xs-12 text]")[0].select("a")
            val tags = mutableListOf<TagData>()
            for (type in types)
                tags.add(TagData(type.text()).apply {
                    action = ClassifyAction.obtain(
                        type.attr("href"),
                        "", type.ownText()
                    )
                })
            //国家/地区
            val animeArea : Element = results[i].select("[class=col-md-9 col-sm-8 col-xs-9 clearfix pb-0]").select("[class=details-info p-0]").select("[class=info clearfix]").select("[class=col-md-6 col-sm-12 col-xs-4 text hidden-xs]")[0]
            tags.add(TagData(animeArea.ownText()).apply {
                action = ClassifyAction.obtain(
                    "",
                    "",
                    animeArea.ownText()
                )
            })
            //語言/字幕
            val language : Element = results[i].select("[class=col-md-9 col-sm-8 col-xs-9 clearfix pb-0]").select("[class=details-info p-0]").select("[class=info clearfix]").select("[class=col-md-6 col-sm-12 col-xs-6 text hidden-xs]")[0]
            tags.add(TagData(language.ownText()).apply {
                action = ClassifyAction.obtain(
                    "",
                    "",
                    language.ownText()
                )
            })
            //年代
            val yearEm : Element = results[i].select("[class=col-md-9 col-sm-8 col-xs-9 clearfix pb-0]").select("[class=details-info p-0]").select("[class=info clearfix]").select("[class=col-md-6 col-sm-6 col-xs-6 text hidden-xs]")[0]
            tags.add(TagData(yearEm.ownText()).apply {
                action = ClassifyAction.obtain(
                    "",
                    "",
                    yearEm.ownText()
                )
            })
            val describe = results[i].select("[class=col-md-9 col-sm-8 col-xs-9 clearfix pb-0]").select("[class=details-info p-0]").select("[class=info clearfix]").select("[class=col-md-12 col-sm-12 col-xs-12 hidden-xs]")[1].select("span")[1].text()
            val item = MediaInfo2Data(
                title, cover, host + url,
                episode, describe, tags
            )
                .apply {
                    action = DetailAction.obtain(url)
                }
            videoInfoItemDataList.add(item)
        }
        return videoInfoItemDataList
    }

}