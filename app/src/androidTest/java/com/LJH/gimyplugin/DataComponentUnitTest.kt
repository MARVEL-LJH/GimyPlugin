package com.LJH.gimyplugin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.su.mediabox.plugin.PluginFactory
import com.su.mediabox.pluginapi.components.*

import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * 组件单元测试示例
 */
@RunWith(AndroidJUnit4::class)
class testGetAnimeShowData {

//    @Test
//    fun testHomeDataComponent() = runBlocking {
//        val factory = PluginFactory()
//        val dataComponent =
//            factory.createComponent(IHomePageDataComponent::class.java)?.apply {
//                val data = getData(1)
//                assert(!data.isNullOrEmpty())
//                data?.forEach {
//                    Log.d("*查看数据", it.toString())
//                }
//            }
//        assertNotNull(dataComponent)
//    }


//    @Test
//    fun testVideoDetailDataComponent() = runBlocking {
//        val factory = PluginFactory()
//        val detailDataComponent =
//            factory.createComponent(IMediaDetailPageDataComponent::class.java)?.apply {
//                val data = getAnimeDetailData("vod/197074.html")
//                assert(data.first.isNotBlank())
//                assert(data.second.isNotBlank())
////                assert(!data.third.isNullOrEmpty())
//
//                Log.d("名称", data.second)
//                Log.d("封面", data.first)
////                Log.d("其他详情数据", "数量:${data.third.size}")
////                data.third.forEach {
////                    Log.d("数据", it.toString())
////                }
//            }
//        assertNotNull(detailDataComponent)
//    }
//}


//    @Test
//    fun testVideoPlayPageDataComponent() = runBlocking {
//        val factory = PluginFactory()
//        val detailDataComponent =
//            factory.createComponent(IVideoPlayPageDataComponent::class.java)?.apply {
//
//            }
//        assertNotNull(detailDataComponent)
//    }
}