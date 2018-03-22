package mpdproject.gcu.me.org.assignmenttest1;

public class Roadwork {

    private String _roadworkName;
    private String _roadworkDescription;
    private String _roadworkLocation;
    private String _roadworkDate;

    public String get_roadworkName() {
        return _roadworkName;
    }

    public void set_roadworkName(String _roadworkName) {
        this._roadworkName = _roadworkName;
    }

    public String get_roadworkDescription() {
        return _roadworkDescription;
    }

    public void set_roadworkDescription(String _roadworkDescription) {
        this._roadworkDescription = _roadworkDescription;
    }

    public String get_roadworkLocation() {
        return _roadworkLocation;
    }

    public void set_roadworkLocation(String _roadworkLocation) {
        this._roadworkLocation = _roadworkLocation;
    }

    public String get_roadworkDate() {
        return _roadworkDate;
    }

    public void set_roadworkDate(String _roadworkDate) {
        this._roadworkDate = _roadworkDate;
    }

    @Override
    public String toString(){

        return _roadworkName + "\n" + _roadworkDescription + "\n" + _roadworkLocation + "\n" + _roadworkDate;

    }
}
