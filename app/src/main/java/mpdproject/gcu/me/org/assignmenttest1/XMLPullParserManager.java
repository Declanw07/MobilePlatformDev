package mpdproject.gcu.me.org.assignmenttest1;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPullParserManager {

    List<Incident> incidents;

    private Incident incident;
    private String text;

    public XMLPullParserManager(){
        incidents = new ArrayList<Incident>();
    }

    public List<Incident> getIncidents(){
        return incidents;
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

                        if(tagname.equalsIgnoreCase("item"))

                }

            }
        }

    }

}
