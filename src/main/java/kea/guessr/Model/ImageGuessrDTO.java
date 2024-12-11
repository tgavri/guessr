package kea.guessr.Model;

public class ImageGuessrDTO {
    private String url;
    private int year;
    private String desc;

    public ImageGuessrDTO(String url, int year, String desc) {
        this.url = url;
        this.year = year;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public int getYear() {
        return year;
    }

    public String getDesc() {
        return desc;
    }
}
