package hpms.discordchat.chat;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventListener implements Listener{
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		Set<Player> recipient = e.getRecipients();
		e.setFormat("[%1$s] %2$s");
		e.setMessage("lol");
	}

}
