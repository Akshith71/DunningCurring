package com.example.DCMS.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "dunning_rules")
public class DunningRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int startDay;
    private int endDay;

    // NOTIFY / THROTTLE / BLOCK
    private String action;

    // SMS / EMAIL / APP
    private String channel;

    private boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DunningRule(Long id, int startDay, int endDay, String action, String channel, boolean active) {
		super();
		this.id = id;
		this.startDay = startDay;
		this.endDay = endDay;
		this.action = action;
		this.channel = channel;
		this.active = active;
	}

	public DunningRule() {
		super();
	}

	public DunningRule(int startDay, int endDay, String action, String channel, boolean active) {
		super();
		this.startDay = startDay;
		this.endDay = endDay;
		this.action = action;
		this.channel = channel;
		this.active = active;
	}
    
    
}

