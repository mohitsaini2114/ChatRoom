
package ancsum.com.inclass06;

import android.content.Context;
import android.widget.ListView;

public class Constants {


    public static String URL_LOGIN = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login";
    public static String URL_THREADS = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread";
    public static String URL_THREAD_DELETE = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/delete";

    public static String URL_SIGNUP = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup";
    public static String URL_ADD_THREAD = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/add";

    public static String URL_GET_MESSAGES_ALL = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/messages";
    public static String URL_ADD_MESSAGE_TO_THREAD = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/message/add";

    public static String URL_INDIVIDUAL_MESSAGE_DELETE = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/message/delete";

    public static String USER_OBJECT = "User Object";
    public static String THREAD_OBJECT = "Thread Object";


    public static Context MESSAGE_THREAD_CONTEXT;

    public static FetchMessageThreads.ListViewAdapter THREADS_ADAPTER;
    public static MessagesListViewAdapter MESSAGES_ADAPTER;


    public static ListView MESSAGE_THREAD_LIST_VIEW;
}
