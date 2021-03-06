package com.qp.app_new.utils;

/**
 * Created by long on 2017/8/23.
 */

import android.text.TextUtils;
import android.util.Log;

import com.qp.app_new.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Gh0st on 2016/6/7 007.
 * https://github.com/ZhaoKaiQiang/KLog
 */
public class LogUtil {
    private static final String LINE_SEPARATOR = System.getProperty ("line.separator");
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int ASSERT = 7;
    private static final int JSON = 8;
    private static final int XML = 9;
    private static final int JSON_INDENT = 4;
    private static final String TAG = "Play28_LOG";

    public static void v (String msg) {
        log (VERBOSE, null, msg);
    }

    public static void v (String tag, String msg) {
        log (VERBOSE, tag, msg);
    }

    public static void d (String msg) {
        log (DEBUG, null, msg);
    }

    public static void d (String tag, String msg) {
        log (DEBUG, tag, msg);
    }

    public static void i (Object... msg) {
        StringBuilder sb = new StringBuilder ();
        for (Object obj : msg) {
            sb.append (obj + ",");
        }
        log (INFO, null, sb.substring (0, Math.max (0, sb.length () - 1)));
    }

    public static void w (String msg) {
        log (WARN, null, msg);
    }

    public static void w (String tag, String msg) {
        log (WARN, tag, msg);
    }

    public static void e (String msg) {
        log (ERROR, null, msg);
    }

    public static void e (String tag, String msg) {
        log (ERROR, tag, msg);
    }

    public static void a (String msg) {
        log (ASSERT, null, msg);
    }

    public static void a (String tag, String msg) {
        log (ASSERT, tag, msg);
    }

    public static void json (String json) {
        log (JSON, null, json);
    }

    public static void json (String tag, String json) {
        log (JSON, tag, json);
    }

    public static void xml (String xml) {
        log (XML, null, xml);
    }

    public static void xml (String tag, String xml) {
        log (XML, tag, xml);
    }

    private static boolean isPrintLog = true;
    private static int LOG_MAXLENGTH = 1000;

    public static void el (String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length ();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e (tagName + i, msg.substring (start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e (tagName + i, msg.substring (start, strLength));
                    break;
                }
            }
        }
    }

    private static void log (int logType, String tagStr, Object objects) {
        try {
            String[] contents = wrapperContent (tagStr, objects);

            String tag = contents[0];
            String msg = contents[1];
            String headString = contents[2];
            if (BuildConfig.DEBUG) {
                switch (logType) {
                    case VERBOSE:
                    case DEBUG:
                    case INFO:
                    case WARN:
                    case ERROR:
                    case ASSERT:
                        printDefault (logType, tag, headString);
                        printDefault (logType, tag, msg);
                        break;
                    case JSON:
                        printJson (tag, msg, headString);
                        break;
                    case XML:
                        printXml (tag, msg, headString);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void printDefault (int type, String tag, String msg) {
        if (TextUtils.isEmpty (tag)) {
            tag = TAG;
        }

//        int index = 0;
//        int maxLength = 4000;
//        int countOfSub = msg.length() / maxLength;

        if (msg.length () > 4000) {
            for (int i = 0; i < msg.length (); i += 4000) {
                if (i + 4000 < msg.length ())
                    printSub (type, tag, msg.substring (i, i + 4000));
//                Log.i("rescounter" + i, xml.substring(i, i + 4000));
                else
                    printSub (type, tag, msg.substring (i, msg.length ()));
            }
        } else
            printSub (type, tag, msg);
    }

//        if (countOfSub > 0) {  // The log is so long
//            for (int i = 0; i < countOfSub; i++) {
//                String sub = msg.substring(index, index + maxLength);
//                printSub(type, tag, sub);
//                index += maxLength;
//            }
//            //printSub(type, msg.substring(index, msg.length()));
//        } else {
//            printSub(type, tag, msg);
//        }

//    }

    private static void printSub (int type, String tag, String sub) {
        if (tag == null) {
            tag = TAG;
        }
        switch (type) {
            case VERBOSE:
                Log.v (tag, sub);
                break;
            case DEBUG:
                Log.d (tag, sub);
                break;
            case INFO:
                Log.i (tag, sub);
                break;
            case WARN:
                Log.w (tag, sub);
                break;
            case ERROR:
                Log.e (tag, sub);
                break;
            case ASSERT:
                Log.wtf (tag, sub);
                break;
        }
    }

    private static void printJson (String tag, String json, String headString) {
        if (TextUtils.isEmpty (json)) {
            d ("Empty/Null json content");
            return;
        }
        if (TextUtils.isEmpty (tag)) {
            tag = TAG;
        }
        String message;

        try {
            if (json.startsWith ("{")) {
                JSONObject jsonObject = new JSONObject (json);
                message = jsonObject.toString (JSON_INDENT);
            } else if (json.startsWith ("[")) {
                JSONArray jsonArray = new JSONArray (json);
                message = jsonArray.toString (JSON_INDENT);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }

        printLine (tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split (LINE_SEPARATOR);
        for (String line : lines) {
            Log.d (tag, "|" + line);
        }
        printLine (tag, false);
    }

    private static void printXml (String tag, String xml, String headString) {
        if (TextUtils.isEmpty (tag)) {
            tag = TAG;
        }
        if (xml != null) {
            try {
                Source xmlInput = new StreamSource (new StringReader (xml));
                StreamResult xmlOutput = new StreamResult (new StringWriter ());
                Transformer transformer = TransformerFactory.newInstance ().newTransformer ();
                transformer.setOutputProperty (OutputKeys.INDENT, "yes");
                transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform (xmlInput, xmlOutput);
                xml = xmlOutput.getWriter ().toString ().replaceFirst (">", ">\n");
            } catch (Exception e) {
                e.printStackTrace ();
            }
            xml = headString + "\n" + xml;
        } else {
            xml = headString + "Log with null object";
        }

        printLine (tag, true);
        String[] lines = xml.split (LINE_SEPARATOR);
        for (String line : lines) {
            if (!TextUtils.isEmpty (line)) {
                Log.d (tag, "|" + line);
            }
        }
        printLine (tag, false);
    }

    public static String[] wrapperContent (String tag, Object... objects) {
        return wrapperContent (6, tag, objects);
    }

    public static String[] wrapperContent (int level, String tag, Object... objects) {
        if (TextUtils.isEmpty (tag)) {
            tag = TAG;
        }
        StackTraceElement[] stackTrace = Thread.currentThread ().getStackTrace ();
        StackTraceElement targetElement = stackTrace[level];
//        String className = targetElement.getClassName();
        String fileName = targetElement.getFileName ();
//        String[] classNameInfo = className.split("\\.");
//        if (classNameInfo.length > 0) {
//            className = classNameInfo[classNameInfo.length - 1] + ".java";
//        }
        String methodName = targetElement.getMethodName ();
        int lineNumber = targetElement.getLineNumber ();
        if (lineNumber < 0) {
            lineNumber = 0;
        }

        StackTraceElement next = stackTrace[level + 1];
        String nextFileName = next.getFileName ();
        String nextMethodName = next.getMethodName ();
        int nextLineNumber = next.getLineNumber ();
        if (nextLineNumber < 0) {
            nextLineNumber = 0;
        }

        String methodNameShort = methodName.substring (0, 1).toUpperCase () + methodName.substring (1);
        String msg = (objects == null) ? "Log with null object" : getObjectsString (objects);
//        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        String headString = "[(" + nextFileName + ":" + nextLineNumber + ")#" + nextMethodName +
                "(" + fileName + ":" + lineNumber + ")#" + Thread.currentThread ().getName () + "#" + methodNameShort + " ] ";
        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString (Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder ();
            stringBuilder.append ("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append ("param").append ("[").append (i).append ("]").append (" = ").append ("null").append ("\n");
                } else {
                    stringBuilder.append ("param").append ("[").append (i).append ("]").append (" = ").append (object.toString ()).append ("\n");
                }
            }
            return stringBuilder.toString ();
        } else {
            Object object = objects[0];
            return object == null ? "null" : object.toString ();
        }
    }

    public static void printLine (String tag, boolean isTop) {
        if (isTop) {
            Log.d (tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d (tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}
