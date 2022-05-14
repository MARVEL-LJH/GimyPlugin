package com.LJH.mediabox.plugin

import android.net.Uri
import com.su.mediabox.pluginapi.components.IMediaSearchPageDataComponent
import com.su.mediabox.pluginapi.data.BaseData
import com.LJH.mediabox.plugin.Const.host
import com.LJH.util.JsoupUtil
import com.su.sakuraanimeplugin.plugin.util.ParseHtmlUtil
import org.jsoup.select.Elements

class CustomMediaSearchPageDataComponent : IMediaSearchPageDataComponent {

    override suspend fun getSearchData(keyWord: String, page: Int): List<BaseData> {
        val searchResultList = mutableListOf<BaseData>()
        val url = "${host}/search/${Uri.encode(keyWord, ":/-![].,%?&=")}----------$page---.html"
        val document = JsoupUtil.getDocument(url)
        val lpic: Elements = document.getElementsByClass("col-md-9 col-sm-12 box-main-content")
            .select("[class=layout-box clearfix]")
        searchResultList.addAll(ParseHtmlUtil.parseSearchEm(lpic[0], url))
        return searchResultList
    }

}