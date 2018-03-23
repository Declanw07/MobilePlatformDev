package mpdproject.gcu.me.org.assignmenttest1;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPullParserManager {

    List<Item> incidents;
    List<Item> roadworks;
    List<Item> planned_roadworks;

    private Item incident, roadwork, planned_roadwork;

    private String text;

    public XMLPullParserManager() {
        incidents = new ArrayList<Item>();
        roadworks = new ArrayList<Item>();
        planned_roadworks = new ArrayList<Item>();
    }

    public List<Item> getIncidents() {
        return incidents;
    }

    public List<Item> getRoadworks() {
        return roadworks;
    }

    public List<Item> getPlanned_roadworks() {
        return planned_roadworks;
    }


    // dataType is used to determine if the data being parsed is incidents(0), roadworks(1) or planned roadworks(2).
    public List<Item> parseItems(InputStream stream, Integer dataTypeID) {

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(stream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagname = parser.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG:

                        if (tagname.equalsIgnoreCase("item")) {
                            if (dataTypeID == 0) {
                                incident = new Item();
                            } else if (dataTypeID == 1) {
                                roadwork = new Item();
                            } else if (dataTypeID == 2) {
                                planned_roadwork = new Item();
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:

                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (tagname.equalsIgnoreCase("item")) {
                            if (dataTypeID == 0) {
                                incidents.add(incident);
                            } else if (dataTypeID == 1) {
                                roadworks.add(roadwork);
                            } else if (dataTypeID == 2) {
                                planned_roadworks.add(planned_roadwork);
                            }
                        } else if (tagname.equalsIgnoreCase("title")) {
                            if (dataTypeID == 0) {
                                incident.set_itemName(text);
                            } else if (dataTypeID == 1) {
                                roadwork.set_itemName(text);
                            } else if (dataTypeID == 2) {
                                planned_roadwork.set_itemName(text);
                            }
                        } else if (tagname.equalsIgnoreCase("description")) {
                            if (dataTypeID == 0) {
                                incident.set_itemDescription(text);
                            } else if (dataTypeID == 1) {
                                roadwork.set_itemDescription(text);
                            } else if (dataTypeID == 2) {
                                planned_roadwork.set_itemDescription(text);
                            }
                        } else if (tagname.equalsIgnoreCase("georss:point")) {
                            if (dataTypeID == 0) {
                                incident.set_itemLocation(text);
                            } else if (dataTypeID == 1) {
                                roadwork.set_itemLocation(text);
                            } else if (dataTypeID == 2) {
                                planned_roadwork.set_itemLocation(text);
                            }
                        } else if (tagname.equalsIgnoreCase("pubDate")) {
                            if (dataTypeID == 0) {
                                incident.set_itemDate(text);
                            } else if (dataTypeID == 1) {
                                roadwork.set_itemDate(text);
                            } else if (dataTypeID == 2) {
                                planned_roadwork.set_itemDate(text);
                            }
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Returns incidents if dataTypeID is 0, roadworks if it is 1, planned roadworks if it is 2. (dataTypeID is set at parsing call, specific to dataset being parsed)
        List<Item> tempList = new ArrayList<Item>();

        if(dataTypeID == 0){
            tempList = incidents;
        } else if (dataTypeID == 1){
            tempList = roadworks;
        } else if (dataTypeID == 2){
            tempList = planned_roadworks;
        }

        return tempList;
    }
}
