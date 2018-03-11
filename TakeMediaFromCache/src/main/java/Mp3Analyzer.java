import com.mpatric.mp3agic.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Viktoria Naboychenko on 15.02.2018.
 */
public class Mp3Analyzer {
    private Mp3File mp3file;

    public Mp3Analyzer(String fileName) {
        try {
            this.mp3file = new Mp3File(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedTagException e) {
        } catch (InvalidDataException e) {
        }
    }

    public boolean isMp3() {
        if (mp3file == null)
            return false;
        return mp3file.hasId3v1Tag() || mp3file.hasId3v2Tag() || mp3file.hasCustomTag();
    }

    public boolean hasNonCustomTag() {
        if (mp3file == null)
            return false;
        return mp3file.hasId3v1Tag() || mp3file.hasId3v2Tag();
    }

    private String encodeName(String name) {
        try {
            if (name != null)
                name = new String(name.getBytes("windows-1252"), "windows-1251");

        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return name;
    }

    public String getFileName() {
        String artist = null;
        String title = null;

        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            artist = id3v2Tag.getArtist();
            title = id3v2Tag.getTitle();
        } else if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            artist = id3v1Tag.getArtist();
            title = id3v1Tag.getTitle();
        }

        artist = encodeName(artist);
        title = encodeName(title);

        if (artist == null || title == null)
            return null;

        return artist + " - " + title + ".mp3";
    }

}
