package common;

public class JsonState {
    private String locName;
    private String locLevel;
    private String locUrl;
    private JsonDistrict LocList;


    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getLocLevel() {
        return locLevel;
    }

    public void setLocLevel(String locLevel) {
        this.locLevel = locLevel;
    }

    public String getLocUrl() {
        return locUrl;
    }

    public void setLocUrl(String locUrl) {
        this.locUrl = locUrl;
    }

    public JsonDistrict getLocList() {
        return LocList;
    }

    public void setLocList(JsonDistrict locList) {
        LocList = locList;
    }
}
