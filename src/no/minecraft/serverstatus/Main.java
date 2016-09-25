package no.minecraft.serverstatus;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		// Config
		this.saveDefaultConfig();
		boolean reportPlugins = this.getConfig().getBoolean("reportPlugins");
		final boolean reportPlayerUUIDs = this.getConfig().getBoolean("reportPlayerUUIDs");
		boolean reportRAM = this.getConfig().getBoolean("reportRAM");
		
		// Objects
		final Request request = new Request();
		final PerformanceMonitor monitor = new PerformanceMonitor();
		final HttpClient client = new HttpClient();
		final Server server = this.getServer();
		final PlayerTracker tracker = new PlayerTracker();
		
		// Register listeners
		this.getServer().getPluginManager().registerEvents(tracker, this);
		
		// Gather static data
		request.motd = this.getServer().getMotd();
		if(reportPlugins) {
			for(Plugin p : this.getServer().getPluginManager().getPlugins()) {
				request.plugins.add(new no.minecraft.serverstatus.Plugin(p.getName()));
			}
		} else {
			request.reportingPlugins = "false";
		}
		request.version = this.getServer().getVersion();
		request.bukkitVersion = this.getServer().getBukkitVersion();
		request.slots = String.valueOf(this.getServer().getMaxPlayers());
		if(reportRAM) {
			request.memory = String.valueOf(monitor.memory);
		} else {
			request.reportingRAM = "false";
		}
		if(!reportPlayerUUIDs) {
			request.reportingPlayerUUIDs = "false";
		}
		
		// Gather dynamic data
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				monitor.tick();
			}
		}, 1L, 1L);
		
		// Send data
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				// Calculate daily reset. If midnight has passed, reset daily statistics
				tracker.pollResetPlayerCounter();
				
				request.playerCount = String.valueOf(server.getOnlinePlayers().size());
				request.playerCountToday = String.valueOf(tracker.getPlayerCount());
				if(reportPlayerUUIDs) {
					request.playerUUIDs.clear();
					for(Player p : server.getOnlinePlayers()) {
						request.playerUUIDs.add(new no.minecraft.serverstatus.Player(p.getUniqueId().toString()));
					}
				}
				
				request.avgTPSlastHour = String.valueOf(monitor.avgTPSlastHour);
				request.avgTPSlastDay = String.valueOf(monitor.avgTPSlastDay);
				request.highestTPS = String.valueOf(monitor.highestTPS);
				request.lowestTPS = String.valueOf(monitor.lowestTPS);
				
				client.send(request);
			}
		}, 1L, 36000L); // 30 minutes
	}
}