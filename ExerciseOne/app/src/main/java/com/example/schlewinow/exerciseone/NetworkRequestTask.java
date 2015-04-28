package com.example.schlewinow.exerciseone;

import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Network request implementation as task.
 * Because 'Android might not enjoy if you do Networking on the main thread'.
 */
public class NetworkRequestTask extends AsyncTask<String, Integer, String>
{
    /**
     * Output medium to user.
     */
    private TextView view = null;

    /**
     * Constructor needs output medium.
     * @param view Reference to TextView to be used as output medium.
     */
    public NetworkRequestTask(TextView view)
    {
        this.view = view;
    }

    /**
     * Get request from server (if possible).
     * @param inputs The URL to pass.
     * @return The response of the http request as string.
     */
    public String doInBackground(String ... inputs)
    {
        String output;
        String input;

        // Check for given URL.
        if(inputs == null || inputs.length == 0)
        {
            return "Enter a URL first!";
        }
        else
        {
            input = inputs[0];
        }

        // To make live simple (not the best approach, yet it will do for now).
        if(!input.startsWith("http://"))
        {
            input = "http://" + input;
        }

        try
        {
            // nice link still: "http://lmgtfy.com/?q=android+async+task"
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(new HttpGet(input));
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
        catch (IllegalArgumentException iax)
        {
            return "Please enter valid URL.";
        }
        catch (IOException iox)
        {
            return "Server not responding.";
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            return "Unknown error occured.";
        }

        return output;
    }

    /**
     * Update UI once done.
     * @param result The string to be printed using the UI.
     */
    public void onPostExecute(String result)
    {
        this.view.setText(result);
    }
}