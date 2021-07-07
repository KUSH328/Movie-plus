package org.notabug.lifeuser.movieplus;

import android.content.Context;

import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
* Credits for this class go to grim.
* http://stackoverflow.com/a/35527922/6685944
*/

public final class ConfigHelper {
	private static final String TAG = "ConfigHelper";

	public static String getConfigValue(Context context, String name) {
		Resources resources = context.getResources();

		// Retrieve the config file.
		try {
			InputStream rawResource = resources.openRawResource(R.raw.config);
			Properties properties = new Properties();
			properties.load(rawResource);
			return properties.getProperty(name);
		} catch (Resources.NotFoundException e) {
			Log.e(TAG, "Unable to find the config file: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "Failed to open config file.");
		}

		return null;
	}
}
