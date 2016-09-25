package no.minecraft.serverstatus;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerTracker implements Listener {
	private Set<String> uuids = new HashSet<String>();
	private long lastReset = System.currentTimeMillis();
	private Calendar calendar = new GregorianCalendar();
	
	public int getPlayerCount() {
		return uuids.size();
	}
	
	public void pollResetPlayerCounter() {
		calendar.setTimeInMillis(System.currentTimeMillis()); // Set to now
		calendar.set(Calendar.HOUR_OF_DAY, 0); // Wind back to midnight
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long time = calendar.getTimeInMillis();
		
		if(lastReset < time) {
			lastReset = System.currentTimeMillis();
			uuids.clear();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		uuids.add(e.getPlayer().getUniqueId().toString());
	}
}