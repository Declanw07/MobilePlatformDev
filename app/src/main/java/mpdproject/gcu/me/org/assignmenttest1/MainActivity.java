//
//
// Starter code for the Mobile Platform Development Assignment
// Seesion 2017/2018
//
//

package mpdproject.gcu.me.org.assignmenttest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private String _incidents="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String _roadworks="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String _planned_roadworks="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private TextView urlInput;
    private Button startButton;
    private String result = "";

    LinkedList<Item> itemList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //LinkedList<Item> itemList = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlInput = (TextView)findViewById(R.id.urlInput);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);



    } // End of onCreate

    public void onClick(View aview)
    {
        startProgress();
    }

    private LinkedList<Item> parseData(String data){
        Item item = null;
        LinkedList<Item> itemList = null;
        boolean found = false;
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(new StringReader(data));
            int eventType = pullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                if(eventType == XmlPullParser.START_TAG){
                    if(pullParser.getName().equalsIgnoreCase("channel")){
                        itemList = new LinkedList<Item>();
                    }
                    else if(pullParser.getName().equalsIgnoreCase("item")){
                        Log.e("XMLParser", "Found item");
                        item = new Item();
                        found = true;
                    }
                    else if(pullParser.getName().equalsIgnoreCase("title") && found){
                        String temp = pullParser.nextText();
                        Log.e("XMLParser", temp);
                        item.set_itemName(temp);
                    }
                    else if(pullParser.getName().equalsIgnoreCase("description") && found){
                        String temp = pullParser.nextText();
                        Log.e("XMLParser", temp);
                        item.set_itemDescription(temp);
                    }
                    else if(pullParser.getName().equalsIgnoreCase("pubDate") && found){
                        String temp = pullParser.nextText();
                        Log.e("XMLParser", temp);
                        item.set_itemDate(temp);
                    }
                }
                else if(eventType == XmlPullParser.END_TAG){
                    if(pullParser.getName().equalsIgnoreCase("item")){
                        Log.e("XMLParser", "Item is" + item.toString());
                        itemList.add(item);
                    }
                    else if(pullParser.getName().equalsIgnoreCase("channel")){
                        int size;
                        size = itemList.size();
                        Log.e("XMLParser", "Amount of incidents: " + size);
                    }
                }
                eventType = pullParser.next();
            }
        }
        catch(XmlPullParserException ael){
            Log.e("XMLParser", "Parsing error" + ael.toString());
        }
        catch(IOException ael){
            Log.e("XMLParser", "IO Error during parsing");
        }

        Log.e("XMLParser", "End of Document");

        return itemList;
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(_incidents)).start();
    } //


    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
    private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            itemList = parseData(result);

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    urlInput.setText(result);
                }
            });
        }

    }

}
