package com.LJH.mediabox.plugin

import android.net.Uri
import android.util.Log
import com.su.mediabox.pluginapi.components.IMediaSearchPageDataComponent
import com.su.mediabox.pluginapi.data.BaseData
import com.LJH.mediabox.plugin.Const.host
import com.LJH.util.JsoupUtil
import com.su.mediabox.pluginapi.action.ClassifyAction
import com.su.mediabox.pluginapi.components.IMediaClassifyPageDataComponent
import com.su.mediabox.pluginapi.data.ClassifyItemData
import com.su.mediabox.pluginapi.util.WebUtilIns
import com.su.sakuraanimeplugin.plugin.util.ParseHtmlUtil
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class MediaClassifyPageDataComponent : IMediaClassifyPageDataComponent {

    override suspend fun getClassifyItemData(): List<ClassifyItemData> {
        val classifyItemDataList = mutableListOf<ClassifyItemData>()
        val document = Jsoup.parse(WebUtilIns.getRenderedHtmlCode(Const.host + "/jcplayer/?url=https://new.iskcd.com/20220425/UWlCfaUo/index.m3u8&jctype=normal&next=//gimy.app/eps/197074-1-2.html"))
        Log.d("QAQ",System.currentTimeMillis().toString())
        document.getElementById("search-list")?.getElementsByTag("li")?.forEach{
        Log.d("分类元素",it.toString())
        classifyItemDataList.addAll(ParseHtmlUtil.parseClassifyEm(it))
        }
        return classifyItemDataList
    }

    override suspend fun getClassifyData(
        classifyAction: ClassifyAction,
        page: Int
    ): List<BaseData> {
        TODO("Not yet implemented")
    }
//
//    lifecycleScope.launch(Dispatchers.Main){
//        val blob = WebUtiIns.interceptBlob()
//
//    }

}