package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class KugouMusicTest extends MusicTestClassByProvider {
    public KugouMusicTest() {
        super(MusicProvider.Kugou);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        builder.iCanSearchMusicPleaseTestMeWithQuery("Beyond");
    }
}
