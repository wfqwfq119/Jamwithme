package cse110.jamwithme;

import java.util.Date;

/**
 * Created by Qinghu on 11/20/2016.
 */

public class message_model {

        private String UserUid;
        private String messageText;
        private String messageUser;
        private long messageTime;

        public message_model(String messageText, String messageUser, String UserUid) {
            this.UserUid = UserUid;
            this.messageText = messageText;
            this.messageUser = messageUser;

            // Initialize to current time
            messageTime = new Date().getTime();
        }

        public message_model(){

        }

        public String getMessageText() {
            return messageText;
        }

        public String getUserUid(){
            return UserUid;
        }

        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }

        public String getMessageUser() {
            return messageUser;
        }

        public void setMessageUser(String messageUser) {
            this.messageUser = messageUser;
        }

        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }
    }
