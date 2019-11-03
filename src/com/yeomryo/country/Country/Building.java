package com.yeomryo.country.Country;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.yeomryo.country.Build;

public class Building {
	int x1;
	int y1;
	int z1;
	int x2;
	int y2;
	int z2;
	Country country;
	Block beacon;
	public Building(){
		x1=0;
		y1=0;
		z1=0;
		x2=0;
		y2=0;
		z2=0;
		country = null;
		beacon = null;
	}
	
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public Block getBeacon() {
		return beacon;
	}
	public void setBeacon(Block beacon) {
		this.beacon = beacon;
	}
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	public int getZ1() {
		return z1;
	}
	public void setZ1(int z1) {
		this.z1 = z1;
	}
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	public int getZ2() {
		return z2;
	}
	public void setZ2(int z2) {
		this.z2 = z2;
	}
	
	public boolean inArea(Player p){
		if(((p.getLocation().getBlockX() >= x1 && p.getLocation().getBlockX() <= x2 )||
				(p.getLocation().getBlockX() <= x1 && p.getLocation().getBlockX() >= x2)) &&
				((p.getLocation().getBlockZ() >= z1 && p.getLocation().getBlockZ() <= z2 )||
						(p.getLocation().getBlockZ() <= z1 && p.getLocation().getBlockZ() >= z2)))
			return true;
		return false;
	}
	public boolean inArea(Location l){
		if(((l.getBlockX() >= x1 && l.getBlockX() <= x2 )||
				(l.getBlockX() <= x1 && l.getBlockX() >= x2)) &&
				((l.getBlockZ() >= z1 && l.getBlockZ() <= z2 )||
						(l.getBlockZ() <= z1 && l.getBlockZ() >= z2)))
			return true;
		return false;
	}
	public boolean protect(Player p){
		if(inArea(p)){
			if(getBeacon() != null){
				if(getBeacon().getType() == Material.BEACON){
					return true;
				}
			}
		}
		return false;
	}
	public boolean protect(Location l){
		if(inArea(l)){
			if(getBeacon() != null){
				if(getBeacon().getType() == Material.BEACON){
					return true;
				}
			}
		}
		return false;
	}
	public void getInfo(){
		
	}
	public void setBorder(Location l1, Location l2){
		this.x1 = l1.getBlockX();
		this.y1 = l1.getBlockY();
		this.z1 = l1.getBlockZ();
		this.x2 = l2.getBlockX();
		this.y2 = l2.getBlockY();
		this.z2 = l2.getBlockZ();
	}
	public void setBorder(int x1, int y1, int z1, int x2, int y2, int z2){
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}
	public Location getBorder(Location loc, int i){
		Location l = loc.clone();
		int x=l.getBlockX();
		int z=l.getBlockZ();
		if(Math.min(Math.abs(x-x1), Math.abs(x-x2)) < Math.min(Math.abs(z-z1), Math.abs(z-z2))){
			if(Math.abs(x-x1) < Math.abs(x-x2)){
				if(x1<x) l.setX(x1-i);
				else l.setX(x1+i);
			}else{
				if(x2<x) l.setX(x2-i);
				else l.setX(x2+i);
			}
		}else{
			if(Math.abs(z-z1) < Math.abs(z-z2)){
				if(z1<z) l.setZ(z1-i);
				else l.setZ(z1+i);
			}else{
				if(z2<z) l.setZ(z2-i);
				else l.setZ(z2+i);
			}
		}
		return l;
	}
	public void removeBuilding(){
		/*for(int x=Math.min(x1, x2);x<=Math.max(x1, x2);x++){
			for(int y=Math.min(y1, y2);y<=Math.max(y1, y2);y++){
				for(int z=Math.min(z1, z2);z<=Math.max(z1, z2);z++){
					(getCountry().getWorld()).getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}*/
		getCountry().buildings.remove(this);
	}
	public Location getBorder(Location loc){
		Location l = loc.clone();
		int x=l.getBlockX();
		int y=l.getBlockY();
		int z=l.getBlockZ();
				if(Math.abs(x-x1) < Math.abs(x-x2)){
					l.setX(x1);
				}else{
					l.setX(x2);
				}
				if(Math.abs(z-z1) < Math.abs(z-z2)){
					l.setZ(z1);
				}else{
					l.setZ(z2);
				}
				if(Math.abs(y-y1) < Math.abs(y-y2)){
					l.setY(y1);
				}else{
					l.setY(y2);
				}
		return l;
	}
	

}
