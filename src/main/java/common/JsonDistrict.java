package common;

public class JsonDistrict {
    private String LocName;
    private String LocLevel;
    private String LocUrl;
    private JsonSubDistrict LocList;

    public JsonSubDistrict getLocList() {
        return LocList;
    }

    public void setLocList(JsonSubDistrict locList) {
        LocList = locList;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String locName) {
        this.LocName = locName;
    }

    public String getLocLevel() {
        return LocLevel;
    }

    public void setLocLevel(String locLevel) {
        this.LocLevel = locLevel;
    }

    public String getLocUrl() {
        return LocUrl;
    }

    public void setLocUrl(String locUrl) {
        this.LocUrl = locUrl;
    }

}
