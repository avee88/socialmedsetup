package com.example.abajpai.demo.model;

/**
 * Created by ABajpai on 8/8/2016.
 */
public class SocialModel {
    private String userName ;
    private  String userDp ;
    private int socialImage ;
    private String socialMediaType ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDp() {
        return userDp;
    }

    public void setUserDp(String userDp) {
        this.userDp = userDp;
    }



    public String getSocialMediaType() {
        return socialMediaType;
    }

    public void setSocialMediaType(String socialMediaType) {
        this.socialMediaType = socialMediaType;
    }

    public int getSocialImage() {
        return socialImage;
    }

    public void setSocialImage(int socialImage) {
        this.socialImage = socialImage;
    }
}
