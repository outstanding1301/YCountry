package com.yeomryo.country.Country;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.yeomryo.country.main;

public class CountryAPI {
	public static Country getCountry(Player p){
		Country c = null;
		for(Country f:main.cl){
			if(f.getMembers().contains(p.getName()))return f;
		}
		return c;
	}
	public static Country getCountryByName(String name){
		Country c = null;
		for(Country f:main.cl){
			if(f.getName().equalsIgnoreCase(name))return f;
		}
		return c;
	}
	public static boolean hasCountry(Player p){
		for(Country f:main.cl){
			if(f.getMembers().contains(p.getName()))return true;
		}
		return false;
	}
	public static boolean isKing(Player p){
		for(Country f:main.cl){
			if(f.getKing().equalsIgnoreCase(p.getName()))return true;
		}
		return false;
	}
	public static boolean hasPower(Player p){
		for(Country f:main.cl){
			if(f.hasPower(p))return true;
		}
		return false;
	}
	public static boolean canBuild(Location l){
		for(Country c : main.cl){
			for(Building b : c.buildings){
				if(b.getBorder(l).distance(l) <= 5){
					return false;
				}
			}
		}
		return true;
	}
	public static String getCountryName(Player p){
		String s = "橈擠";
		if(hasCountry(p))
			s=getCountry(p).getName();
		return s;
	}
	public static String countryList(){
		String s="＝6[ ＝fCou＝entry ＝6]＝f  "+ChatColor.GREEN+"措陛 跡煙\n式式式式式式式式式式式式式式式式式式\n";
		for(Country c : main.cl){
			s+="＝6[ ＝fCou＝entry ＝6]＝f  "+ChatColor.YELLOW+c.getName()+"措陛(措諾:"+c.getKing()+") 措團 熱 : "+c.getMembers().size()+"\n";
		}
		s+=ChatColor.GREEN+"＝6[ ＝fCou＝entry ＝6]＝f  式式式式式式式式式式式式式式式式式式";
		return s;
	}
}
