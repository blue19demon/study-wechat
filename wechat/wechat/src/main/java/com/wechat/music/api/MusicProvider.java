package com.wechat.music.api;

import com.wechat.music.provider.baidu.BaiduMusicApi;
import com.wechat.music.provider.kugou.KugouMusicApi;
import com.wechat.music.provider.kuwo.KuwoMusicApi;
import com.wechat.music.provider.migu.MiguMusicApi;
import com.wechat.music.provider.netease.NeteaseMusicApi;
import com.wechat.music.provider.qq.QQMusicApi;
import com.wechat.music.provider.weibo.WeiboMusicApi;
import com.wechat.music.provider.xiami.XiamiMusicApi;
import com.wechat.music.provider.yiting.YitingMusicApi;

/**
 * Created by haohua on 2018/2/11.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum MusicProvider {
    Netease("网易云音乐", NeteaseMusicApi.class),
    QQ("QQ音乐", QQMusicApi.class),
    Kugou("酷狗音乐", KugouMusicApi.class),
    Xiami("虾米音乐", XiamiMusicApi.class),
    Baidu("百度音乐", BaiduMusicApi.class),
    Migu("咪咕音乐", MiguMusicApi.class),
    Yiting("一听音乐", YitingMusicApi.class),
    Kuwo("酷我音乐", KuwoMusicApi.class),
    Weibo("微博音乐", WeiboMusicApi.class),;

    private final Class<? extends MusicApi> musicApiClass;
    private final String providerName;

    MusicProvider(String providerName, Class<? extends MusicApi> musicApiClass) {
        this.providerName = providerName;
        this.musicApiClass = musicApiClass;
    }

    public Class<? extends MusicApi> getMusicApiClass() {
        return musicApiClass;
    }

    @Override
    public String toString() {
        return providerName;
    }
}
