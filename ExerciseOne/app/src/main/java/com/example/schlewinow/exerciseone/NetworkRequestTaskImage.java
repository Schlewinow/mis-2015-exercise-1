package com.example.schlewinow.exerciseone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Used to show the content in the web view.
 */
public class NetworkRequestTaskImage extends AsyncTask<String, Integer, String>
{
    /**
     * Output medium to user.
     */
    private ImageView viewImage = null;

    /**
     * Used for error output.
     */
    private TextView viewText = null;

    /**
     * If an error occurs, output message to TextView instead of WebView.
     */
    private boolean showInImageView = true;

    /**
     *
     */
    private Bitmap image = null;

    /**
     * Constructor (obviously enough!?).
     * @param viewImage Output medium for image.
     * @param viewText Output medium for error messages.
     */
    public NetworkRequestTaskImage(ImageView viewImage, TextView viewText)
    {
        this.viewImage = viewImage;
        this.viewText = viewText;
    }

    /**
     * Do some URL checks here.
     * @param inputs The URL to pass.
     * @return The response of the http request as string.
     */
    protected String doInBackground(String... inputs)
    {
        String input;
        this.showInImageView = true;

        // Check for given URL.
        if(inputs == null || inputs.length == 0)
        {
            this.showInImageView = false;
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

                // Additional image loading code.
                InputStream in = new java.net.URL(input).openStream();
                this.image = BitmapFactory.decodeStream(in);
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
            this.showInImageView = false;
            return "Please enter valid URL.";
        }
        catch (IOException iox)
        {
            this.showInImageView = false;
            return "Server not responding.";
        }
        catch (Exception ex)
        {
            this.showInImageView = false;
            System.out.println(ex.toString());
            return "Unknown error occured.";
        }

        // Actually the only true difference to the version of the NetworkRequestTask.
        return input;
    }

    /**
     * Update UI once done, this time the web view.
     * @param result The string to be printed using the UI.
     */
    public void onPostExecute(String result)
    {
        if(this.showInImageView)
        {
            if(this.image != null)
            {
                this.viewText.setText("...");
                this.viewImage.setImageBitmap(this.image);
            }
            else
            {
                this.viewText.setText("Given URL is no image!");
            }
        }
        else
        {
            this.viewText.setText(result);
        }
    }
}

