package mpdproject.gcu.me.org.assignmenttest1;

public class Item {

    private String _itemName;
    private String _itemDescription;
    private String _itemDate;

    public Item(){
        _itemName = "";
        _itemDescription = "";
        _itemDate = "";
    }

    public Item(String title, String description, String date){
        _itemName = title;
        _itemDescription = description;
        _itemDate = date;
    }

    public String get_itemName() {
        return _itemName;
    }

    public void set_itemName(String itemName) {
        this._itemName = itemName;
    }

    public String get_itemDescription() {
        return _itemDescription;
    }

    public void set_itemDescription(String itemDescription) {
        this._itemDescription = itemDescription;
    }

    public String get_itemDate() {
        return _itemDate;
    }

    public void set_itemDate(String itemDate) {
        this._itemDate = itemDate;
    }

    @Override
    public String toString(){

        return _itemName + "\n" + _itemDescription + "\n" + _itemDate;

    }

}
