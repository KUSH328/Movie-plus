package org.notabug.lifeuser.movieplus.activity;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.notabug.lifeuser.movieplus.R;

import java.util.Locale;

/**
* This class contains some basic functionality that would
* otherwise be duplicated in multiple activities.
*/
public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    /**
    * Creates the toolbar.
    */
    protected void setNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
    * Creates a home button in the toolbar.
    */
    protected void setBackButtons() {
		// Add back button to the activity.
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}
	}

	/**
    * Returns the language that is used by the phone.
	* Usage: this is only meant to be used at the end of the API url.
    * Otherwise an ampersand needs to be added manually at the end
    * and the possibility that an empty string can be returned 
    * (which will interfere with the manual ampersand) must be 
    * taken into account.
	*/
	public static String getLanguageParameter() {
		if(!Locale.getDefault().getLanguage().equals("en")) {
			return "&language=" + Locale.getDefault().getLanguage();
		}
		return "";
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

		// Back button
		if (id == android.R.id.home) {
			this.finish();
			return true;
		}

        return super.onOptionsItemSelected(item);
    }

}
