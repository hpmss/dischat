package hpms.discordchat.channel;

import java.util.UUID;

public abstract class ChannelUpgrade extends ChannelPrefix {
	
	protected int currentUpgradeCost;
	protected int nextUpgradeCost;
	protected int maxUpgradeSlot;
	protected float costScaleRate;
	
	public ChannelUpgrade(String name, UUID leader, boolean getFlag) {
		super(name, leader, getFlag);
	}

	public void upgradeSlot(int amount) {
		
	}

	public void setUpgradeCost(int cost) {
		this.currentUpgradeCost = cost;
	}

	public void setNextUpgradeCost(int cost) {
		this.nextUpgradeCost = cost;
	}
	
	public void setUpgradeCostRate(float rate) {
		this.costScaleRate = rate;
	}
	
	public void setMaxUpgradableSlot(int slot) {
		this.maxUpgradeSlot = slot;
	}

	public int getMaxUpgradableSlot() {
		return this.maxUpgradeSlot;
	}

	public int getCurrentUpgradeCost() {
		return this.currentUpgradeCost;
	}
	
	public int getNextUpgradeCost() {
		return this.nextUpgradeCost;
	}

	

	
	
	
	
}
