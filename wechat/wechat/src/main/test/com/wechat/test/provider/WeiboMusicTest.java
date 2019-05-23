package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class WeiboMusicTest extends MusicTestClassByProvider {
    public WeiboMusicTest() {
        super(MusicProvider.Weibo);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        String query = "陈绮贞";
        builder.iCanSearchMusicPleaseTestMeWithQuery(query);
    }
}
