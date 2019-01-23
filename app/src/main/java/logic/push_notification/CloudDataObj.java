package logic.push_notification;

import com.google.gson.annotations.SerializedName;
import com.soloviof.easyads.AdObj;

public class CloudDataObj {

    @SerializedName("cmd")
    private String toDo;

    @SerializedName("gp_link")
    private String promoLink;

    @SerializedName("txt_val")
    private String strVal;

    @SerializedName("msg")
    private MsgNotification userNotification;


    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public String getPromoLink() {
        return promoLink;
    }

    public void setPromoLink(String promoLink) {
        this.promoLink = promoLink;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public MsgNotification getUserNotification() {
        return userNotification;
    }

    public void setUserNotification(MsgNotification userNotification) {
        this.userNotification = userNotification;
    }

    public static class MsgNotification {

        @SerializedName("pid")
        private int notificId;

        @SerializedName("from")
        private String senderName;

        @SerializedName("txt")
        private String text;


        public int getNotificId() {
            return notificId;
        }

        public void setNotificId(int notificId) {
            this.notificId = notificId;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}