package com.trace.learning.plugin.impl;

import com.trace.learning.plugin.impl.jvm.JvmPlugin;
import com.trace.learning.plugin.impl.link.LinkPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jiangpeng
 * @date 2019/12/9 0009
 */
public class PluginFactory {
    public static List<IPlugin> pluginGroup = new ArrayList<>();

    static {
        //链路监控
        pluginGroup.add(new LinkPlugin());
        //Jvm监控
        pluginGroup.add(new JvmPlugin());
    }
}
