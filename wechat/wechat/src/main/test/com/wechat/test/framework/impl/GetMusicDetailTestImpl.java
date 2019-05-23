package com.wechat.test.framework.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wechat.music.api.MusicApi;
import com.wechat.music.api.MusicApiFactory;
import com.wechat.music.api.MusicProvider;
import com.wechat.music.model.Song;
import com.wechat.test.framework.AbsMusicTestCase;
import com.wechat.test.framework.mark.GetMusicDetailTest;
import com.wechat.test.util.TestUtils;

public class GetMusicDetailTestImpl extends AbsMusicTestCase implements GetMusicDetailTest {
    private MusicApi api;
    private String searchQuery;

    @Override	
    public void init(Object[] args) {
        MusicProvider provider = (MusicProvider) args[0];
        api = MusicApiFactory.create(provider);
        searchQuery = (String) args[1];
    }

    @Override
    public void runTest() throws IOException {
        List<? extends Song> songs = api.searchMusicSync(searchQuery, 0, false);
        ArrayList<String> musicIds = new ArrayList<>();
        for (Song song : songs) {
            musicIds.add(song.getSongId());
        }
        List<? extends Song> songDetails = api.getSongDetailInfoByIdsSync(true, musicIds.toArray(new String[]{}));
        assertEquals(songDetails.size(), songs.size());
        Song detailSong0 = songDetails.get(0);
        println("detailSong0: " + detailSong0);
        String picUrl = detailSong0.getPicUrl();
        println("picUrl: " + picUrl);
        assertEquals(200, TestUtils.testDownload(picUrl));
        // assertTrue(detailSong0.getLyric().getLyric().length() > 0);
    }
}
