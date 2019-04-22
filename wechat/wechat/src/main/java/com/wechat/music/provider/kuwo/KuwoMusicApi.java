package com.wechat.music.provider.kuwo;

import java.io.IOException;
import java.util.List;

import com.wechat.music.api.MusicApi;
import com.wechat.music.model.Album;
import com.wechat.music.model.MusicLink;
import com.wechat.music.model.Song;

/**
 * Created by haohua on 2018/2/23.
 */
@SuppressWarnings("SpellCheckingInspection")
public class KuwoMusicApi implements MusicApi {
    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws IOException {
        List<KuwoSong> result = new KuwoSearchMusicRequest(keyword, page).requestSync();
        if (needLink) {
            for (KuwoSong song : result) {
                KuwoSongLink link = new KuwoGetMusicLinkRequest(song.getSongId()).requestSync();
                song.setMusicLink(link);
            }
        }
        return result;
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) throws IOException {
        return null;
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        return null;
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws IOException {
        return null;
    }
}
