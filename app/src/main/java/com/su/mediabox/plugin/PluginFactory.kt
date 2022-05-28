package com.su.mediabox.plugin

import com.LJH.mediabox.plugin.Const
import com.su.mediabox.pluginapi.components.*
import com.su.mediabox.pluginapi.IPluginFactory
import com.LJH.mediabox.plugin.*
import com.LJH.mediabox.plugin.VideoPlayPageDataComponent

/**
 * 每个插件必须实现本类
 *
 * 注意包和类名都要相同，且必须提供公开的无参数构造方法
 */
class PluginFactory : IPluginFactory() {

    override val host: String = Const.host

    override fun <T : IBasePageDataComponent> createComponent(clazz: Class<T>) = when (clazz) {
        IHomePageDataComponent::class.java -> CustomHomePageDataComponent()
        IMediaDetailPageDataComponent::class.java -> CustomMediaDetailPageDataComponent()
        IMediaSearchPageDataComponent::class.java -> CustomMediaSearchPageDataComponent()
        IVideoPlayPageDataComponent::class.java -> VideoPlayPageDataComponent()
        IMediaClassifyPageDataComponent::class.java -> MediaClassifyPageDataComponent()
        else -> null
    } as? T

}