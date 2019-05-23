package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class BaiduMusicTest extends MusicTestClassByProvider {
    public BaiduMusicTest() {
        super(MusicProvider.Baidu);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        String query = "陈奕迅";
        builder.iCanSearchMusicPleaseTestMeWithQuery(query);
        builder.iCanGetAlbumInfoPleaseTestMeWithQuery(query);
    }
}
