package com.wechat.test.provider;

import com.wechat.music.api.MusicProvider;
import com.wechat.test.framework.MusicTestClassByProvider;
import com.wechat.test.framework.SupportedTestCaseBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class KuwoMusicTest extends MusicTestClassByProvider {
    public KuwoMusicTest() {
        super(MusicProvider.Kuwo);
    }

    @Override
    protected void addSupportedTestCase(SupportedTestCaseBuilder builder) {
        builder.iCanSearchMusicPleaseTestMeWithQuery("Beyond");
    }
}
