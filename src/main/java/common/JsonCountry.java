package common;

public class JsonCountry {
    private String locName;
    private String locLevel;
    private String locUrl;
    private JsonState locList;

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

    public JsonState getLocList() {
        return locList;
    }

    public void setLocList(JsonState locList) {
        this.locList = locList;
    }
}
