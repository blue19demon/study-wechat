package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class YitingMusicTest extends MusicTestClassByProvider {
    public YitingMusicTest() {
        super(MusicProvider.Yiting);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        String query = "Beyond";
        builder.iCanSearchMusicPleaseTestMeWithQuery(query);
        builder.iCanGetMusicDetailPleaseTestMeWithQuery(query);
    }
}
