package ancsum.com.inclass06;

import com.google.gson.annotations.SerializedName;

public class MessageResponseSingle {

    @SerializedName("message")
    protected Message message;

    @SerializedName("status")
    protected String status;
}
