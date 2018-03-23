package mpdproject.gcu.me.org.assignmenttest1;

public class Item {

    private String _itemName;
    private String _itemDescription;
    private String _itemLocation;
    private String _itemDate;

    public String get_itemName() {
        return _itemName;
    }

    public void set_itemName(String _itemName) {
        this._itemName = _itemName;
    }

    public String get_itemDescription() {
        return _itemDescription;
    }

    public void set_itemDescription(String _itemDescription) {
        this._itemDescription = _itemDescription;
    }

    public String get_itemLocation() {
        return _itemLocation;
    }

    public void set_itemLocation(String _itemLocation) {
        this._itemLocation = _itemLocation;
    }

    public String get_itemDate() {
        return _itemDate;
    }

    public void set_itemDate(String _itemDate) {
        this._itemDate = _itemDate;
    }

    @Override
    public String toString(){

        return _itemName + "\n" + _itemDescription + "\n" + _itemLocation + "\n" + _itemDate;

    }

}
