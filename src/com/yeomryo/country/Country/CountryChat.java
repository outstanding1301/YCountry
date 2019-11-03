package com.yeomryo.country.Country;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CountryChat implements Listener{
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if(CountryAPI.hasCountry(e.getPlayer())){
			Country c = CountryAPI.getCountry(e.getPlayer());
			if(!c.isChatMod(e.getPlayer()))
				e.setFormat(ChatColor.YELLOW+"["+ChatColor.WHITE+c.getName()+"국가"+ChatColor.AQUA+" "+c.getRank(e.getPlayer())+ChatColor.YELLOW+"] "+ChatColor.WHITE+e.getFormat());
			else{
				c.chat(ChatColor.YELLOW+" * ["+ChatColor.WHITE+c.getName()+"국가"+ChatColor.AQUA+" "+c.getRank(e.getPlayer())+" "+ChatColor.GREEN+e.getPlayer().getName()+ChatColor.YELLOW+"] "+ChatColor.WHITE+e.getMessage());
				e.setCancelled(true);
			}
		}
	}
}
