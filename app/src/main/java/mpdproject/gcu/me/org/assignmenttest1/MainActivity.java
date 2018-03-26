//
//
// Starter code for the Mobile Platform Development Assignment
// Seesion 2017/2018
//
//

package mpdproject.gcu.me.org.assignmenttest1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private String _incidents="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String _roadworks="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String _planned_roadworks="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private TextView urlInput;
    private Button fetchDataButton, incidentButton, roadworkButton, plannedRoadworkButton;

    //private String result = "";
    private View mainView;



    private int currentIncidentIndex = 0;
    private int currentRoadworksIndex = 0;
    private int currentPlannedRoadworksIndex = 0;

    LinkedList<Item> itemList = null;
    LinkedList<Item> incidentList = null;
    LinkedList<Item> roadworkList = null;
    LinkedList<Item> plannedRoadworkList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mainView = findViewById(R.id.mainView);
        urlInput = (TextView)findViewById(R.id.urlInput);
        fetchDataButton = (Button)findViewById(R.id.fetchData);
        fetchDataButton.setOnClickListener(this);
        incidentButton = (Button)findViewById(R.id.fetchIncidentButton);
        incidentButton.setOnClickListener(this);
        roadworkButton = (Button)findViewById(R.id.fetchRoadworks);
        roadworkButton.setOnClickListener(this);
        plannedRoadworkButton = (Button)findViewById(R.id.fetchPlannedRoadworks);
        plannedRoadworkButton.setOnClickListener(this);

        //startButton.setOnClickListener(this);
        mainView.setBackgroundColor(getResources().getColor(R.color.silver, null));


    } // End of onCreate



    public void onClick(View v)
    {

        switch(v.getId()){

            case R.id.fetchData:
                loadData dataLoader = new loadData();
                dataLoader.execute();
                Log.e("ASYNC: ", "lul");

            case R.id.fetchIncidentButton:
                if(incidentList != null && currentIncidentIndex != incidentList.size()) {
                    urlInput.setText(incidentList.get(currentIncidentIndex).toString());
                    currentIncidentIndex++;
                }
                break;

            case R.id.fetchRoadworks:
                if(roadworkList != null && currentRoadworksIndex != roadworkList.size()) {
                    urlInput.setText(roadworkList.get(currentRoadworksIndex).toString());
                    currentRoadworksIndex++;
                }
                break;

            case R.id.fetchPlannedRoadworks:
                if(plannedRoadworkList != null && currentPlannedRoadworksIndex != plannedRoadworkList.size()) {
                    urlInput.setText(plannedRoadworkList.get(currentPlannedRoadworksIndex).toString());
                    currentPlannedRoadworksIndex++;
                }
                break;

        }
    }

    private LinkedList<Item> parseData(String data){
        Item item = null;
        LinkedList<Item> itemList = null;
        boolean found = false;

        Log.e("XMLParser:", "Starting Parsing");

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
                        //Log.e("XMLParser", "Item is" + item.toString());
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

    // datatype determines what dataset we are going to parse, 0 is incidents,
    // 1 is roadworks, 2 is planned roadworks.
    public void startProgress(int dataType)
    {
        // Run network access on a separate thread;
        switch(dataType) {

            case 0:
                new Thread(new Task(_incidents, 0)).start();
                break;

            case 1:
                new Thread(new Task(_roadworks, 1)).start();
                break;

            case 2:
                new Thread(new Task(_planned_roadworks, 2)).start();
                break;

        }

    }

    private class loadData extends AsyncTask<Void, Void, Void>{

        public loadData(){
            super();
        }

        protected Void doInBackground(Void... params){
            Log.e("ASYNC: ","doinbackground");

            new Task(_incidents, 0).run();
            new Task(_roadworks, 1).run();
            new Task(_planned_roadworks, 2).run();

            return null;
        }

        @Override
        protected void onPostExecute(Void result){

        }

    }


    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
    private String url;
    private String result = "";
    private Integer dataType;

        public Task(String aurl, Integer dataSet)
        {
            url = aurl;
            dataType = dataSet;
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

            switch(dataType){

                case 0:
                    incidentList = (LinkedList<Item>)itemList.clone();
                    Log.e("Incident List: ", "Cloning List");
                    break;

                case 1:
                    roadworkList = (LinkedList<Item>)itemList.clone();
                    Log.e("Roadworks List: ", "Cloning List");
                    break;

                case 2:
                    plannedRoadworkList = (LinkedList<Item>)itemList.clone();
                    Log.e("Planned Roadworks List", " Cloning List");
                    break;
                }


            //MainActivity.this.runOnUiThread(new Runnable()
            //{
            //    public void run() {
            //        Log.d("UI thread", "I am the UI thread");

            //    }
            //});
        }

    }

}
