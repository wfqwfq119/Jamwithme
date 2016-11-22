package cse110.jamwithme;

/**
 * Created by Qinghu on 11/21/2016.
 */

public class friend_obj {
    private String User_Uid;
    private String User_name;

    public friend_obj(String Uid,String name){
        this.User_Uid = Uid;
        this.User_name = name;
    }
    public friend_obj(){}

    public String getUser_Uid(){
       return User_Uid;
    }
    public String getUser_name(){
        return User_name;
    }
}
