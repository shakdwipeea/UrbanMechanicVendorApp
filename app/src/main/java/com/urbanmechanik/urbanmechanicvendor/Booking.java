package com.urbanmechanik.urbanmechanicvendor;

/**
 * Created by Akash on 19-10-2015.
 */
public class Booking {
    private int id;
    private String type;
    private String vendor;
    private String user;
    private String status;
    private String problem;
    private String slot;
    private String feedback;
    private String ratings;


    public Booking(int id, String type, String vendor, String user, String status, String problem, String slot, String feedback, String ratings) {
        this.id = id;
        this.type = type;
        this.vendor = vendor;
        this.user = user;
        this.status = status;
        this.problem = problem;
        this.slot = slot;
        this.feedback = feedback;
        this.ratings = ratings;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getVendor() {
        return vendor;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public String getProblem() {
        return problem;
    }

    public String getSlot() {
        return slot;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getRatings() {
        return ratings;
    }
}
