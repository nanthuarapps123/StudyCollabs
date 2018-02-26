package com.bma.studycollabs;

/**
 * Created by NANTHA on 10/11/2017.
 */

public class SubComments {


    private String name;
    private String comment;
    private String thumbnailUrl;
    private String commetid;
    private String postid;
    private String rating;
    private String ratingshow;
    private String ratingdisabled;
    private String posteddate;


    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setcomment(String comment) {
        this.comment = comment;
    }

    public String getcomment() {
        return comment;
    }

    public void setcommetid(String commetid) {
        this.commetid = commetid;
    }

    public String getCommetid(){
        return commetid;
    }

    public void setpostid(String postid) {
        this.postid = postid;
    }

    public String getPostid(){
        return postid;
    }

    public void setrating(String rating) {
        this.rating = rating;
    }

    public String getrating(){
        return rating;
    }

    public void setratingshow(String ratingshow) {
        this.ratingshow = ratingshow;
    }

    public String getratingshow(){
        return ratingshow;
    }

    public void setratingdisabled(String ratingdisabled) {
        this.ratingdisabled = ratingdisabled;
    }

    public String getratingdisabled(){
        return ratingdisabled;
    }

    public void setposteddate(String posteddate) {
        this.posteddate = posteddate;
    }

    public String getPosteddate(){
        return posteddate;
    }
}
