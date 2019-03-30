package ancsum.com.inclass06;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessagesResponse {

    @SerializedName("messages")
    protected ArrayList<Message> messageList;

    @SerializedName("status")
    protected String status;
}
