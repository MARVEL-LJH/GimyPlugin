package com.LJH.mediabox.plugin

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import com.su.mediabox.pluginapi.components.IMediaDetailPageDataComponent
import com.su.mediabox.pluginapi.action.ClassifyAction
import com.su.mediabox.pluginapi.action.PlayAction
import com.su.mediabox.pluginapi.data.*
import com.su.mediabox.pluginapi.util.TextUtil.urlEncode
import com.su.mediabox.pluginapi.util.UIUtil.dp
import com.LJH.util.JsoupUtil
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class CustomMediaDetailPageDataComponent : IMediaDetailPageDataComponent {
    override suspend fun getAnimeDetailData(
        partUrl: String
    ): Triple<String, String, List<BaseData>> {
        var cover = ""
        var title = ""
        var desc = ""
        var score = -1F
        var upState = ""
        val url = Const.host + partUrl
        val document = JsoupUtil.getDocument(url)
        val tags = mutableListOf<TagData>()

        val details = mutableListOf<BaseData>()

        //番剧头部信息
        val meta: Elements = document.select("meta")
        cover = meta.select("[property=og:image]").attr("content")
        val detailsinfop0: Elements = document.getElementsByClass("details-info p-0")
        title = detailsinfop0.select("h1").text()
        Log.d("番剧标题", title)
        //动漫介绍
        desc = meta.select("[name=description]").attr("content")
        Log.d("动漫介绍", title)

        //其他信息，如标签、地区等
        //类别
        val typeElements = detailsinfop0.select("[class=info clearfix]")
            .select("[class=col-md-6 col-sm-6 col-xs-6 text hidden-xs]").select("a")
        tags.add(TagData(typeElements.text()).apply {
            action = ClassifyAction.obtain(
                typeElements.attr("href"),
                "",
                typeElements.text()
            )
        })

        //国家/地区
        val animeArea: Element =
            document.getElementsByClass("details-info p-0").select("[class=info clearfix]")
                .select("[class=col-md-6 col-sm-6 col-xs-4 text hidden-xs]")[0]
        tags.add(TagData(animeArea.ownText()).apply {
            action = ClassifyAction.obtain(
                "",
                "",
                animeArea.ownText()
            )
        })

        //年代
        val yearEm: Element =
            document.getElementsByClass("details-info p-0").select("[class=info clearfix]")
                .select("[class=col-md-6 col-sm-6 col-xs-12 text hidden-xs]")[2]
        tags.add(TagData(yearEm.ownText()).apply {
            action = ClassifyAction.obtain(
                "",
                "",
                yearEm.ownText()
            )
        })

        //更新状况
        val row: Element =
            document.getElementsByClass("row")[2].select("[class=col-md-8-fix col-sm-12 box-main-content]")[0]
        for (em in row.children()) {
            when (em.className()) {
                //when遍历12个线路
                "playlist-mobile playlist layout-box clearfix" -> {
                    val Playline: Element =
                        em.select("li").select("a")[0]
                    upState = em.select("[class=clearfix fade in active]").select("li").select("a").last()!!
                        .text()
                    Log.d("更新状况", upState)

                    details.add(
                        SimpleTextData(
                            Playline.text()
                                    + " 更新至($upState)"
                        ).apply {
                            fontSize = 16F
                            fontColor = Color.WHITE
                        }
                    )
                    //播放列表
                    val Playlist: Element =
                        em.select("[class=clearfix fade in active]")[0]
                    details.add(
                        EpisodeListData(
                            parseEpisodes(
                                Playlist
                            )
                        )
                    )
                }
            }
        }
        return Triple(cover, title, mutableListOf<BaseData>().apply {
            add(Cover1Data(cover, score = score).apply {
                layoutConfig =
                    BaseData.LayoutConfig(
                        itemSpacing = 12.dp,
                        listLeftEdge = 12.dp,
                        listRightEdge = 12.dp
                    )
            })
            add(
                SimpleTextData(title).apply {
                    fontColor = Color.WHITE
                    fontSize = 20F
                    gravity = Gravity.CENTER
                    fontStyle = 1
                }
            )
            add(TagFlowData(tags))
            add(
                LongTextData(desc.addDouBanSearch(title)).apply {
                    fontColor = Color.WHITE
                }
            )
            addAll(details)
        })
    }

    private fun parseEpisodes(element: Element): List<EpisodeData> {
        val episodeList = mutableListOf<EpisodeData>()
        val elements: Elements = element.select("li")
        for (k in elements.indices) {
            val episodeUrl = elements[k].select("a").attr("href")
            episodeList.add(
                EpisodeData(elements[k].select("a").text(), episodeUrl).apply {
                    action = PlayAction.obtain(episodeUrl)
                }
            )
        }
        return episodeList
    }


    private fun String.addDouBanSearch(name: String) =
        this + "\n🎞 豆瓣评分 https://m.douban.com/search/?query=${name.urlEncode()}"
}