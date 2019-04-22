package com.wechat.music.provider.weibo;

import com.wechat.music.model.Artist;
import com.wechat.music.model.BaseBean;

@SuppressWarnings("SpellCheckingInspection")
class WeiboArtist extends BaseBean implements Artist {
    public String name;

    public String id;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }
}
