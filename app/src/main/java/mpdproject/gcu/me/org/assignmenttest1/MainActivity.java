//
// Declan Wylie Mobile Platform Development Coursework
// Matric: S1429200     University Username: DWYLIE202
//

package mpdproject.gcu.me.org.assignmenttest1;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    // URLs for XML datasets, instantiation of textview, buttons and the edittext elements.
    private String _incidents="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String _roadworks="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String _planned_roadworks="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private TextView urlInput;
    private Button fetchDataButton, incidentButton, roadworkButton, plannedRoadworkButton, searchButton;
    private EditText searchTextBox;

    private View mainView;

    // Instantiation of lists of classes for incidents, roadworks and planned roadworks, itemList and searchlist are regularly cleared
    // and used for temporary storage when parsing an XML dataset or temporary storage for searches performed by the user.
    //LinkedList<Item> itemList = null;
    LinkedList<Item> incidentList = null;
    LinkedList<Item> roadworkList = null;
    LinkedList<Item> plannedRoadworkList = null;
    LinkedList<Item> searchList = new LinkedList<Item>();

    // Called on creation or running of the Application.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Finding UI elements by ID and setting onclick listeners for each button.
        mainView = findViewById(R.id.mainView);
        searchTextBox = (EditText)findViewById(R.id.editText);
        urlInput = (TextView)findViewById(R.id.urlInput);
        fetchDataButton = (Button)findViewById(R.id.fetchData);
        fetchDataButton.setOnClickListener(this);
        incidentButton = (Button)findViewById(R.id.fetchIncidentButton);
        incidentButton.setOnClickListener(this);
        roadworkButton = (Button)findViewById(R.id.fetchRoadworks);
        roadworkButton.setOnClickListener(this);
        plannedRoadworkButton = (Button)findViewById(R.id.fetchPlannedRoadworks);
        plannedRoadworkButton.setOnClickListener(this);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        urlInput.setText("Fetching traffic issues, hold on...");

        //mainView.setBackgroundColor(getResources().getColor(R.color.silver, null));

        // Load and parse the XML datasets.
        new loadData().execute();

        // Create timer and fetch/parse XML datasets every 2 minutes to have accurate data.
        Timer timer = new Timer();
        long delay = 0;
        long interval = 1 * 120000;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                new loadData().execute();
            }
        };
        // Run parsing at 2 minute intervals.
        timer.scheduleAtFixedRate(task, delay, interval);

    }



    // Handles what happens when a button is clicked
    public void onClick(View v)
    {
        // Check which button was clicked by referencing the ID.
        switch(v.getId()){

            // If the fetch data button is pressed then fetch the XML data from the traffic scotland website.
            case R.id.fetchData:
                new loadData().execute();
                break;

            // If the fetch incident button is pressed then show all the Items within the incident list.
            // Make sure to check incidentList != null as we cannot display an empty list.
            case R.id.fetchIncidentButton:
                if(incidentList != null) {
                    urlInput.setText("");
                    for (int i = 0; i < incidentList.size(); i++){
                        addItemToSearchListText(incidentList.get(i), true);
                    }
                }
                break;

            // Same as fetchIncidentButton but for roadworks.
            case R.id.fetchRoadworks:
                if(roadworkList != null) {
                    urlInput.setText("");
                    for (int i = 0; i < roadworkList.size(); i++){
                        addItemToSearchListText(roadworkList.get(i), false);
                    }
                }
                break;

            // Same as above.
            case R.id.fetchPlannedRoadworks:
                if(plannedRoadworkList != null) {
                    urlInput.setText("");
                    for (int i = 0; i < plannedRoadworkList.size(); i++){
                        addItemToSearchListText(plannedRoadworkList.get(i), false);
                    }
                }
                break;

            // If the search button was pressed then...
            case R.id.searchButton:

                // Clear the searchList to avoid previous search results also showing.
                LinkedList<Item> searchList = new LinkedList<Item>();

                // Check that we have the XML datasets parsed and stored into lists.
                if(incidentList != null && roadworkList != null && plannedRoadworkList != null){
                    // For every incident in incident list...
                    for(int i = 0; i < incidentList.size(); i++){
                        // Check if it has the searched for text, if it does add it to the searchlist.
                        if(incidentList.get(i).toString().contains(searchTextBox.getText())){
                            searchList.add(incidentList.get(i));
                        }
                    }
                    // Same as above for roadworks list.
                    for(int i = 0; i < roadworkList.size(); i++){
                        if(roadworkList.get(i).toString().contains(searchTextBox.getText())) {
                            searchList.add(roadworkList.get(i));
                        }
                    }
                    // Same as above for planned roadworks list.
                    for(int i = 0; i < plannedRoadworkList.size(); i++){
                        if(plannedRoadworkList.get(i).toString().contains(searchTextBox.getText())){
                            searchList.add(plannedRoadworkList.get(i));
                        }
                    }
                }
                // Clear the text field so we can display all the search results.
                urlInput.setText("");
                for(int i = 0; i < searchList.size(); i++){
                    // Method for adding an item class to the text view appropriately.
                    addItemToSearchListText(searchList.get(i), false);

                }
        }
    }

    // Method to polish RSS feed artifacts such as <br /> within descriptions and then display them in text view additively.
    private void addItemToSearchListText(Item item, Boolean showDate){

        // Add location to text view.
        urlInput.append("Location: " + item.get_itemName());

        // Code to "tidy up" RSS feed artifacts and properly display text neatly, this is done by replacing certain character sequences.
        // Multiple temporary strings must be made as strings in java are immutable and cannot change the memory representation of the string.
        String newDesc = item.get_itemDescription().replace("<br />", "\n");
        String newDesc2 = newDesc.replace("Traffic Management:", "\n" + "Traffic Management: ");
        String newDesc3 = newDesc2.replace("Diversion Information:", "\n" + "Diversion Information: ");
        String newDesc4 = newDesc3.replace("Works:", "Works: ");

        if(!showDate) {
            urlInput.append("\n" + newDesc4);
        }
        // As start and end dates are parsed within the description tag on roadworks and planned roadworks but not incidents
        // A seperate string for date must be displayed only for incidents, this is done via a boolean as a parameter for this method
        // Letting us know if an incident is being displayed.
        if(showDate){
            urlInput.append("\n" + "Description: " + newDesc4);
            urlInput.append("\n");
            urlInput.append("Date: " + item.get_itemDate());
        }
        // Add appropriate spacing between items.
        urlInput.append("\n" + "\n" + "\n");

    }

    // Parsing of XML data.
    private LinkedList<Item> parseData(String data){

        Item item = null;
        LinkedList<Item> itemList = new LinkedList<Item>();
        boolean found = false;

        Log.e("XMLParser:", "Starting Parsing");

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(new StringReader(data));
            int eventType = pullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                // Set item's name to whatever is in title tag, same for items description in description tag and date in pubdate tag.
                // Also performing check to make sure an item is found to avoid crashes when parsing specific datasets.
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

        // Returning the parsed list of item classes.
        return itemList;
    }

    // datatype determines what dataset we are going to parse, 0 is incidents,
    // 1 is roadworks, 2 is planned roadworks.
    // THIS METHOD IS DEPRECATED ONLY USE FOR TESTING.
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

    // Used for asynchronous procesing of XML datasets.
    private class loadData extends AsyncTask<Void, Void, Void>{

        public loadData(){
            super();
        }

        protected Void doInBackground(Void... params){
            Log.e("ASYNC: ","running Asynchronous tasks...");
            // Creating tasks for each data set with appropriate datatype parameter asynchronously.
            new Task(_incidents, 0).run();
            new Task(_roadworks, 1).run();
            new Task(_planned_roadworks, 2).run();

            //urlInput.setText("Fetching traffic incidents, hold on...");

            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            Log.e("ASYNC: ", "Post execution, Task complete.");
            urlInput.setText("Successfully loaded traffic issues.");

        }

    }


    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
    private String url;
    private String result = "";
    private Integer dataType;

    private LinkedList<Item> tempList = new LinkedList<Item>();

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


            //Log.e("MyTag","in run");

            try
            {
                //Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("JAVAERROR: ", "ioexception");
            }
            // Parse data to temporary list.
            tempList = parseData(result);

            // Clone temporary list to permanent list depending on data set being parsed, 0 = incident list, 1 = roadworks list, 2 = planned roadworks list.
            switch(dataType){

                case 0:
                    incidentList = (LinkedList<Item>)tempList.clone();
                    Log.e("Incident List: ", "Cloning List");
                    break;

                case 1:
                    roadworkList = (LinkedList<Item>)tempList.clone();
                    Log.e("Roadworks List: ", "Cloning List");
                    break;

                case 2:
                    plannedRoadworkList = (LinkedList<Item>)tempList.clone();
                    Log.e("Planned Roadworks List", " Cloning List");
                    break;
                }
            }
    }
}
