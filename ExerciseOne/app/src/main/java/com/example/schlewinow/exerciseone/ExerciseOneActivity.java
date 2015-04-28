package com.example.schlewinow.exerciseone;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.*;
import android.widget.*;
import android.webkit.*;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExerciseOneActivity extends ActionBarActivity {

    /**
     * Field to get user input from.
     */
    private EditText inputField = null;

    /**
     * Field to output response from server.
     */
    private TextView outputField = null;

    /**
     * Outputs web page as web page.
     */
    private WebView webOutput = null;

    /**
     * Outputs image.
     */
    private ImageView imageOutput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_one);

        this.inputField = (EditText)this.findViewById(R.id.inputField);
        this.outputField = (TextView)this.findViewById(R.id.outputField);
        this.webOutput = (WebView)this.findViewById(R.id.webOutputField);
        this.imageOutput = (ImageView)this.findViewById(R.id.imageOutputField);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_one, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Used in every click handler.
     */
    private String clickHandlerBasis()
    {
        String userInput = this.inputField.getText().toString();

        // Small debug output.
        Toast toast = Toast.makeText(getApplicationContext(), "Connecting to " + userInput, Toast.LENGTH_LONG);
        toast.show();

        this.webOutput.loadUrl("about:blank");
        this.outputField.setText("...");
        this.imageOutput.setImageBitmap(null);

        return userInput;
    }

    /**
     * Click callback for connect-button.
     * @param view Necessary for callback.
     */
    public void clickHandlerText(View view)
    {
        String userInput = this.clickHandlerBasis();

        // Thread does not work, yet why?
        //Thread networkThread = new Thread(new NetworkRunnable());
        //networkThread.run();

        AsyncTask networkTask = new NetworkRequestTask(this.outputField);
        String[] inputs = {userInput};
        networkTask.execute(inputs);
    }

    /**
     * Click callback for web view-button.
     * @param view Necessary for callback.
     */
    public void clickHandlerWeb(View view)
    {
        String userInput = this.clickHandlerBasis();

        AsyncTask networkTask = new NetworkRequestTaskWeb(this.webOutput, this.outputField);
        String[] inputs = {userInput};
        networkTask.execute(inputs);
    }

    /**
     * Click callback for web view-button.
     * @param view Necessary for callback.
     */
    public void clickHandlerImage(View view)
    {
        String userInput = this.clickHandlerBasis();

        AsyncTask networkTask = new NetworkRequestTaskImage(this.imageOutput, this.outputField);
        String[] inputs = {userInput};
        networkTask.execute(inputs);
    }

    /**
     * Network request implementation as thread.
     */
    class NetworkRunnable implements Runnable
    {
        public void run()
        {
            String output = "...!";

            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(new HttpGet("http://lmgtfy.com/?q=android+ansync+task"));
                StatusLine status = response.getStatusLine();

                if (status.getStatusCode() == HttpStatus.SC_OK)
                {
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    response.getEntity().writeTo(outStream);
                    String responseAsString = outStream.toString();
                    System.out.println("Response string: " + responseAsString);

                    output = responseAsString;
                }
                else
                {
                    //Well, this didn't work.
                    response.getEntity().getContent().close();
                    throw new IOException(status.getReasonPhrase());
                }
            }
            catch (IOException iox)
            {
                Toast exToast = Toast.makeText(getApplicationContext(), iox.toString(), Toast.LENGTH_LONG);
                exToast.show();
            }

            ExerciseOneActivity.this.outputField.post(new OutputPostRunnable(outputField, output));
        }
    }

    /**
     * Helper to get network response from network thread to main thread.
     */
    class OutputPostRunnable implements Runnable
    {
        /**
         * The text view to output the string to.
         */
        private TextView tv = null;

        /**
         * The string to print.
         */
        private String output = "";

        public OutputPostRunnable(TextView tv, String output)
        {
            this.tv = tv;
            this.output = output;
        }

        /**
         * Run implementation. Using UI-elements is only possible here due to Android constraints.
         */
        public void run()
        {
            this.tv.setText(this.output);
        }
    }
}
