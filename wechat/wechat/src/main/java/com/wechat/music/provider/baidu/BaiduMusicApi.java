package com.wechat.music.provider.baidu;

import java.io.IOException;
import java.util.List;

import com.wechat.music.api.MusicApi;
import com.wechat.music.model.Album;
import com.wechat.music.model.MusicLink;
import com.wechat.music.model.Song;
import com.wechat.music.util.SongUtils;

@SuppressWarnings("SpellCheckingInspection")
public class BaiduMusicApi implements MusicApi {
    @Override
    public List<? extends Song> searchMusicSync(String keyword, int page, boolean needLink) throws IOException {
        List<? extends Song> result = new BaiduSearchMusicRequest(keyword, page).requestSync();
        if (needLink) {
            String[] songIds = SongUtils.getSongIdsFromSongList(result);
            List<BaiduSong> details = new BaiduGetSongDetailsRequest(songIds).requestSync();
            result = details;
        }
        return result;
    }

    @Override
    public MusicLink getMusicLinkByIdSync(String musicId) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Song> getSongDetailInfoByIdsSync(boolean needLyric, String... musicIds) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Album getAlbumInfoByIdSync(String albumId, boolean needLink) throws IOException {
        BaiduAlbum album = new BaiduGetAlbumInfoRequest(albumId).requestSync();
        return album;
    }
}
