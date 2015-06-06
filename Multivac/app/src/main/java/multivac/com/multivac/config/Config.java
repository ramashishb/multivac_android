package multivac.com.multivac.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by manish.patwari on 6/6/15.
 */
public class Config {
    private static final String PACKAGE_NAME = "com.multivac";
    private static SharedPreferences sharedPreferences;
    private static Config instance;

    public static synchronized Config getInstance(){
        if(instance == null)
        {
            instance = new Config();
        }
        return instance;
    }
    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    private static String getKey(String initialKey){
        return "com.multivac." + initialKey;
    }

    public static final String  HOME_ADDRESS = getKey("home_address");
    public static final String  OFFICE_ADDRESS = getKey("office_address");

    public void saveHomeAddress(String address) {
        this.sharedPreferences.edit().putString(HOME_ADDRESS, address).commit();
    }

    public String getHomeAddress() {
        return this.sharedPreferences.getString(HOME_ADDRESS, "");
    }
    public void saveOfficeAddress(String address) {
        this.sharedPreferences.edit().putString(OFFICE_ADDRESS, address).commit();
    }

    public String getOfficeAddress() {
        return this.sharedPreferences.getString(OFFICE_ADDRESS, "");
    }
}
