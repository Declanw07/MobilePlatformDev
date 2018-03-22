package mpdproject.gcu.me.org.assignmenttest1;

public class Incident {

    private String _incidentName;
    private String _incidentDescription;
    private String _incidentLocation;
    private String _incidentDate;

    public String get_incidentName() {
        return _incidentName;
    }

    public void set_incidentName(String _incidentName) {
        this._incidentName = _incidentName;
    }

    public String get_incidentDescription() {
        return _incidentDescription;
    }

    public void set_incidentDescription(String _incidentDescription) {
        this._incidentDescription = _incidentDescription;
    }

    public String get_incidentLocation() {
        return _incidentLocation;
    }

    public void set_incidentLocation(String _incidentLocation) {
        this._incidentLocation = _incidentLocation;
    }

    public String get_incidentDate() {
        return _incidentDate;
    }

    public void set_incidentDate(String _incidentDate) {
        this._incidentDate = _incidentDate;
    }

    @Override
    public String toString(){

        return _incidentName + "\n" + _incidentDescription + "\n" + _incidentLocation + "\n" + _incidentDate;

    }

}
