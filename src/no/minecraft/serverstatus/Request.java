package no.minecraft.serverstatus;

import java.util.ArrayList;

public class Request {
	public String motd;
	public ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	public String version;
	public String bukkitVersion;
	
	public String reportingPlugins = "true";
	public String reportingPlayerUUIDs = "true";
	public String reportingRAM = "true";
	
	public String slots;
	public String playerCount;
	public String playerCountToday;
	public ArrayList<Player> playerUUIDs = new ArrayList<Player>();
	
	public String avgTPSlastHour;
	public String avgTPSlastDay;
	public String highestTPS;
	public String lowestTPS;
	
	public String memory = "-1";
	
	@Override
	public String toString() {
		String pluginList = "";
		for(Plugin plugin : plugins) {
			pluginList += plugin.name + "\n";
		}
		
		String uuids = "";
		for(Player player : playerUUIDs) {
			uuids += player.uuid + "\n";
		}
		
		String result = "";
		
		result += "MOTD: " + motd + "\n";
		result += "Reporting plugins: " + reportingPlugins + "\n";
		result += "Plugins:\n" + pluginList;
		result += "Version: " + version + "\n";
		result += "Bukkit version: " + bukkitVersion + "\n";
		result += "Slots: " + slots + "\n";
		result += "Players: " + playerCount + "\n";
		result += "Players today: " + playerCountToday + "\n";
		result += "Reporting player UUIDs: " + reportingPlayerUUIDs + "\n";
		result += "Player UUIDs:\n" + uuids;
		result += "Avg TPS last hour: " + avgTPSlastHour + "\n";
		result += "Avg TPS last day: " + avgTPSlastDay + "\n";
		result += "TPS High: " + highestTPS + "\n";
		result += "TPS Low: " + lowestTPS + "\n";
		result += "Reporting memory: " + reportingRAM + "\n";
		result += "Memory: " + memory;
		
		return result;
	}
}