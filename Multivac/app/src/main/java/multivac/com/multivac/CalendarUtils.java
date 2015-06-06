package multivac.com.multivac;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by shashwat on 06/06/15.
 */
public class CalendarUtils {
    /////////////////////////////////////////////////////////
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CAL_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int CAL_PROJECTION_ID_INDEX = 0;
    private static final int CAL_PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int CAL_PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int CAL_PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    ////////////////////////////////////////////////////////

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,                           // 0
            CalendarContract.Events.TITLE,                  // 1
            CalendarContract.Events.DTSTART,         // 2
            CalendarContract.Events.DTEND,                  // 3
            CalendarContract.Events.DURATION
    };

    // The indices for the projection array above.
    private static final int EVE_PROJECTION_ID_INDEX = 0;
    private static final int EVE_PROJECTION_TITLE_INDEX = 1;
    private static final int EVE_PROJECTION_DTSTART_INDEX = 2;
    private static final int EVE_PROJECTION_DTEND_INDEX = 3;
    private static final int EVE_PROJECTION_DURATION_INDEX = 4;

    //////////////////////////////////////////////////////////

    public static class EventResult {
        public final String title;
        public final long startTime;
        public final long endTime;
        public final long duration;

        public EventResult(String title, long startTime, long endTime, long duration) {
            this.duration = duration;
            this.endTime = endTime;
            this.startTime = startTime;
            this.title = title;
        }
    }


    Context mContext;

    public CalendarUtils(Context mContext) {
        this.mContext = mContext;
    }

    public EventResult timeToFirstMeeting(String userEmailId, long timeMs) {
// Run query
        ContentResolver cr = mContext.getContentResolver();

        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        String[] selectionArgs = new String[]{userEmailId, "com.google", userEmailId};
// Submit the query and get a Cursor object back.
        Cursor cur = cr.query(uri, CAL_PROJECTION, selection, selectionArgs, null);

        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            long calID = cur.getLong(CAL_PROJECTION_ID_INDEX);
            String displayName = cur.getString(CAL_PROJECTION_DISPLAY_NAME_INDEX);
            String accountName = cur.getString(CAL_PROJECTION_ACCOUNT_NAME_INDEX);
            String ownerName = cur.getString(CAL_PROJECTION_OWNER_ACCOUNT_INDEX);

            Uri eventsUri = CalendarContract.Events.CONTENT_URI;
            String eventSelection = "((" + CalendarContract.Events.CALENDAR_ID + "= ?) AND ("
                    + CalendarContract.Events.DTSTART + " > ?))";
            String[] argValues = new String[]{"" + calID, "" + timeMs};

            Cursor eventCur = cr.query(eventsUri, EVENT_PROJECTION, eventSelection, argValues, null);
            while (eventCur.moveToNext()) {
                String title = cur.getString(EVE_PROJECTION_TITLE_INDEX);
                long startTime = cur.getLong(EVE_PROJECTION_DTSTART_INDEX);
                long endTime = cur.getLong(EVE_PROJECTION_DTEND_INDEX);
                long duration = cur.getLong(EVE_PROJECTION_DURATION_INDEX);

                return new EventResult(title, startTime, endTime, duration);
            }
        }

        return null;
    }
}
