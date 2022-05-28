package com.LJH.mediabox.plugin

import android.util.Log
import com.LJH.util.JsoupUtil
import com.su.mediabox.pluginapi.Constant
import com.su.mediabox.pluginapi.action.ClassifyAction
import com.su.mediabox.pluginapi.components.IMediaClassifyPageDataComponent
import com.su.mediabox.pluginapi.data.BaseData
import com.su.mediabox.pluginapi.data.ClassifyItemData
import com.su.mediabox.pluginapi.util.TextUtil.urlDecode
import com.su.mediabox.pluginapi.util.WebUtilIns
import com.su.sakuraanimeplugin.plugin.util.ParseHtmlUtil
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class MediaClassifyPageDataComponent : IMediaClassifyPageDataComponent {

    override suspend fun getClassifyItemData(): List<ClassifyItemData> {
        val classifyItemDataList = mutableListOf<ClassifyItemData>()
        //示例：使用WebUtil解析动态生成的分类项
        val document = Jsoup.parse(
            WebUtilIns.getRenderedHtmlCode(
                Const.host + "/type/2",//电视剧频道
                userAgentString = Constant.Request.USER_AGENT_ARRAY[12]
            )
        )
        document.getElementsByClass("list-mcat clearfix").select("[class=content-menu clearfix]")
            .select("[class=item  list_type]").select("[class=clearfix]").forEach {
            classifyItemDataList.addAll(ParseHtmlUtil.parseClassifyEm(it))
        }
        Log.d(
            "???",
            document.getElementsByClass("list-mcat clearfix")
                .select("[class=content-menu clearfix]").select("[class=item  list_type]")
                .select("[class=clearfix]").toString()
        )
        return classifyItemDataList
    }

    override suspend fun getClassifyData(
        classifyAction: ClassifyAction,
        page: Int
    ): List<BaseData> {
        val classifyList = mutableListOf<BaseData>()
        //https://gimy.app/cat/2--------3---.html
        var url = classifyAction.url + ""
        if (!url.startsWith(Const.host))
            url = Const.host + url
        url.replace("-----------.html","--------${page}---.html")
        Log.d("获取分类数据", url)
        Log.d("获取分类数据2", Const.host)
        Log.d("获取分类数据3", classifyAction.toString())
        classifyAction.url?.let { Log.d("获取分类数据4", it) }
        val document = JsoupUtil.getDocument(url)
        val containerElements: Elements =
            document.getElementsByClass("container").select("[class=layout-box clearfix]")
                .select("[class=box-video-list]").select("[class=item]").select("[class=clearfix]")
        for (container in containerElements)
            for (target in container.children())
                when (target.className()) {
                    "col-md-2 col-sm-3 col-xs-4" -> {
                        classifyList.addAll(
                            ParseHtmlUtil.parseClassifySearchEm(
                                target,
                                url
                            )
                        )
                    }
                }

        return classifyList
    }

}