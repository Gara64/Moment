package fr.paul.moment.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by paul on 06/12/17.
 */

public class MomentJSON {

    @SerializedName("title")
    public String title;
    @SerializedName("date")
    public String date;
    @SerializedName("description")
    public String description;
    @SerializedName("images")
    public String[] images;

    /*public ArrayList<Image> images;

    static public class Image {
        @SerializedName("path")
        public String path;
    }*/
}
