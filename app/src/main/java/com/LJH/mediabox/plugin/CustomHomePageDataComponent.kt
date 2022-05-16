package com.LJH.mediabox.plugin

import android.graphics.Typeface
import android.util.Log
import com.su.mediabox.pluginapi.action.DetailAction
import com.su.mediabox.pluginapi.components.IHomePageDataComponent
import com.su.mediabox.pluginapi.data.*
import com.su.mediabox.pluginapi.util.UIUtil.dp
import com.LJH.mediabox.plugin.Const.host
import com.LJH.util.JsoupUtil


class CustomHomePageDataComponent : IHomePageDataComponent {

    override suspend fun getData(page: Int): List<BaseData>? {
        if (page != 1)
            return null
        val url = host
        val doc = JsoupUtil.getDocument(url)
        val data = mutableListOf<BaseData>()


        //3.横幅
        doc.getElementsByClass("hidden-xs")[1]?.apply {
            val bannerItems = mutableListOf<BannerData.BannerItemData>()
            for (em in children()) {
                em.select("li").forEach { bannerItem ->
                    val nameEm = bannerItem.select("img").attr("alt")
                    val videoUrl = bannerItem.getElementsByTag("a").first()?.attr("href")
                    val bannerImage = bannerItem.getElementsByTag("a").select("img").attr("src")
                    if (bannerImage.isNotBlank()) {
                        Log.d("添加横幅项", "封面：$bannerImage 链接：$videoUrl")
                        bannerItems.add(
                            BannerData.BannerItemData(
                                bannerImage, nameEm ?: "", ""
                            ).apply {
                                if (!videoUrl.isNullOrBlank())
                                    action = DetailAction.obtain(videoUrl)
                            }
                        )
                    }
                }
            }
            if (bannerItems.isNotEmpty())
                data.add(BannerData(bannerItems, 6.dp).apply {
                    paddingTop = 0
                    paddingBottom = 6.dp
                })
        }

        //4.各类推荐
        val types = doc.getElementsByClass("row")[1]?: return null
        for (em in types.children()) {
            Log.d("元素", em.className())
            when (em.className()) {
                //遍历row取出关键数据layout-box clearfix表格里的数据
                "layout-box clearfix" -> {
                    //分类box-title
                    Log.d("元素b", em.className())
                        val typeName = em.select("[class=box-title]").select("h3").text()
                        if (!typeName.isNullOrBlank()) {
                            data.add(SimpleTextData(typeName).apply {
                                fontSize = 18F
                                fontStyle = Typeface.BOLD
                            })
                            Log.d("视频分类", typeName)
                        }
                    for (video in em.select("[class=col-md-2 col-sm-3 col-xs-4]")) {
                        Log.d("元素c", em.className())
                        video.select("a")?.first()?.apply {
                            val name = attr("title")
                            val videoUrl = attr("href")
                            val coverUrl = attr("data-original")
                            val episode = video.select("[class=note text-bg-r]").first()?.text()

                            if (!name.isNullOrBlank() && !videoUrl.isNullOrBlank() && !coverUrl.isNullOrBlank()) {
                                data.add(
                                    MediaInfo1Data(name, coverUrl, videoUrl, episode ?: "")
                                        .apply {
                                            action = DetailAction.obtain(videoUrl)
                                        })
                                Log.d("添加视频", "($name) ($videoUrl) ($coverUrl) ($episode)")
                            }
                        }
                    }
                }
            }
        }
        return data
    }
}