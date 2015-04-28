package mmbuw.com.brokenproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.view.*;

public class BrokenActivity extends Activity {

    /**
     * Necessary, since used in AnotherBrokenActivity.
     */
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    /**
     * EditText that was initially not properly set (null).
     */
    private EditText auntEdith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broken);

        // Fixed that line so that the proper reference is set.
        auntEdith = (EditText)findViewById(R.id.edittext);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.broken, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used as onClick method within user interface.
     * @param view Necessary parameter, exception when missing.
     */
    public void brokenFunction(View view)
    {
        //I was once, perhaps, possibly a functioning function
        if (auntEdith.getText().toString().equals("Timmy"))
        {
            System.out.println("Timmy fixed a bug!");
        }

        System.out.println("If this appears in your console, you fixed a bug.");
        Intent intent = new Intent(this,AnotherBrokenActivity.class);
        String message = "This string will be passed to the new activity";

        // Message has to be added to intent.
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }
}
