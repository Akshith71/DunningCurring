package com.example.DCMS.DTO;



public class CreateDunningRuleRequest {

    private int startDay;
    private int endDay;
    private String action;
    private String channel;
    private boolean active;

    // getters & setters
    public int getStartDay() { return startDay; }
    public void setStartDay(int startDay) { this.startDay = startDay; }

    public int getEndDay() { return endDay; }
    public void setEndDay(int endDay) { this.endDay = endDay; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

