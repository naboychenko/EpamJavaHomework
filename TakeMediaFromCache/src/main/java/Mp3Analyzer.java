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
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch(UnsupportedTagException e){
            //System.out.println(e.getMessage());
        }
        catch (InvalidDataException e){
            //System.out.println(e.getMessage());
        }
    }
    public boolean isMp3(){
        if(mp3file == null)
            return false;
        return mp3file.hasId3v1Tag() || mp3file.hasId3v2Tag() || mp3file.hasCustomTag();
    }

    public String getFileName(){
        String fileName;
        String artist = null;
        String title = null;

        try {
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                artist = id3v2Tag.getArtist();
                if (artist != null) {
                    artist = new String(artist.getBytes("windows-1252"), "windows-1251");
                }
                title = id3v2Tag.getTitle();
                if (title != null) {
                    title = new String(title.getBytes("windows-1252"), "windows-1251");
                }
            } else if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                artist = id3v1Tag.getArtist();
                title = id3v1Tag.getTitle();
                if (artist != null) {
                    artist = new String(artist.getBytes("windows-1252"), "windows-1251");
                }
                if (title != null) {
                    title = new String(title.getBytes("windows-1252"), "windows-1251");
                }
            }
        } catch (UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        artist = artist == null ? "UnknownArtist" : artist;
        title = title == null ? "UnknownTitle" : title;
        fileName = artist + " - " + title + ".mp3";

        return fileName;
    }

}
