package mpdproject.gcu.me.org.assignmenttest1;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPullParserManager {

    List<Incident> incidents;
    List<Roadwork> roadworks;

    private Incident incident;
    private Roadwork roadwork;

    private String text;

    public XMLPullParserManager(){
        incidents = new ArrayList<Incident>();
        roadworks = new ArrayList<Roadwork>();
    }

    public List<Incident> getIncidents(){
        return incidents;
    }

    public List<Roadwork> getRoadworks(){
        return roadworks;
    }



    public List<Incident> parseIncidents(InputStream stream){

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try{
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(stream, null);

            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagname = parser.getName();
                switch(eventType){

                    case XmlPullParser.START_TAG:

                        if(tagname.equalsIgnoreCase("item")){
                            incident = new Incident();
                        }
                        break;

                    case XmlPullParser.TEXT:

                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if(tagname.equalsIgnoreCase("item")){
                            incidents.add(incident);
                        }else if(tagname.equalsIgnoreCase("title")){
                            incident.set_incidentName(text);
                        }else if(tagname.equalsIgnoreCase("description")){
                            incident.set_incidentDescription(text);
                        }else if(tagname.equalsIgnoreCase("georss:point")){
                            incident.set_incidentLocation(text);
                        }else if(tagname.equalsIgnoreCase("pubDate")){
                            incident.set_incidentDate(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return incidents;
    }

    public List<Roadwork> parseRoadworks(InputStream stream){

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try{
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(stream, null);

            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagname = parser.getName();
                switch(eventType){

                    case XmlPullParser.START_TAG:

                        if(tagname.equalsIgnoreCase("item")){
                            roadwork = new Roadwork();
                        }
                        break;

                    case XmlPullParser.TEXT:

                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if(tagname.equalsIgnoreCase("item")){
                            roadworks.add(roadwork);
                        }else if(tagname.equalsIgnoreCase("title")){
                            roadwork.set_roadworkName(text);
                        }else if(tagname.equalsIgnoreCase("description")){
                            roadwork.set_roadworkDescription(text);
                        }else if(tagname.equalsIgnoreCase("georss:point")){
                            roadwork.set_roadworkLocation(text);
                        }else if(tagname.equalsIgnoreCase("pubDate")){
                            roadwork.set_roadworkDate(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return roadworks;
    }

}
