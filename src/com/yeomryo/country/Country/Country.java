package com.yeomryo.country.Country;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yeomryo.country.Build;
import com.yeomryo.country.main;
import com.yeomryo.stat.Stat.StatAPI;


public class Country {
	String name;
	String king;
	String subking;
	World world;
	LinkedList<String> members = new LinkedList<String>();
	LinkedList<Player> chatmod = new LinkedList<>();
	LinkedList<Building> buildings = new LinkedList<>();
	int maxbuilding;
	
	Country enemy;

	boolean war = false;

	public Country(){
		
	}
	public Country(String name, Player p) throws IOException{
		this.name = name;
		this.king = p.getName();
		this.subking = "����";
		this.world = p.getWorld();
		this.members.add(p.getName());
		this.maxbuilding = 1;
		main.cl.add(this);
		this.saveFile();
	}
	
	public String getName() {
		return name;
	}
	
	public int getMaxbuilding() {
		return maxbuilding;
	}
	public void setMaxbuilding(int maxbuilding) {
		this.maxbuilding = maxbuilding;
	}
	public void addMember(Player p){
		this.members.add(p.getName());
		try {
			saveFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getRank(Player p) {
		if(p.getName().equalsIgnoreCase(this.king))if(hasBeacon())return "����"; else return "��ô��";
		else if(p.getName().equalsIgnoreCase(this.subking)) if(hasBeacon())return "�ο�"; else return "����";
		return "����";
	}
	public String getRank(String p) {
		if(p.equalsIgnoreCase(this.king))if(hasBeacon())return "����"; else return "��ô��";
		else if(p.equalsIgnoreCase(this.subking)) if(hasBeacon())return "�ο�"; else return "����";
		return "����";
	}
	public boolean isChatMod(Player p){
		return this.chatmod.contains(p);
	}
	public void chat(String msg){
		for(String s : this.members){
			Player t = Bukkit.getPlayer(s);
			if(t != null)t.sendMessage(msg);
		}
	}
	public void message(String msg){
		for(String s : this.members){
			Player t = Bukkit.getPlayer(s);
			if(t != null)t.sendMessage(ChatColor.YELLOW+" * [ "+ChatColor.WHITE+getName()+"����"+ChatColor.YELLOW+" ] "+""+ChatColor.ITALIC+msg);
		}
	}
	public void ChatMod(Player p){
		if(!isChatMod(p)){
			this.chatmod.add(p);
			p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.GREEN+"����ä�� ��� ON");
		}
		else{
			this.chatmod.remove(p);
			p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����ä�� ��� OFF");
		}
	}

	public boolean isMember(String s){
		return members.contains(s);
	}
	public boolean isMember(Player p){
		return isMember(p.getName());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getKing() {
		return king;
	}
	public void setKing(String king) {
		this.king = king;
		try {
			saveFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getSubking() {
		return subking;
	}
	public void setSubking(String subking) {
		if(this.king != subking)
			this.subking = subking;
		try {
			saveFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public LinkedList<String> getMembers() {
		return members;
	}
	public void setMembers(LinkedList<String> members) {
		this.members = members;
	}

	public boolean inArea(Player p){
		for(Building b : this.buildings){
			if(b.inArea(p))
			return true;
		}
		return false;
	}
	public boolean inArea(Location l){
		for(Building b : this.buildings){
			if(b.inArea(l))
				return true;
		}
		return false;
	}
	public boolean protect(Player p){
		for(Building b : this.buildings){
			if(b.protect(p))
			return true;
		}
		return false;
	}

	public boolean protect(Location p){
		for(Building b : this.buildings){
			if(b.protect(p))
			return true;
		}
		return false;
	}
	
	public Location getBorder(Location loc, int i){
		LinkedList<Integer> r = new LinkedList<>();
		for(Building b : buildings)
			r.add((int) b.getBorder(loc,i).distance(loc));
		Collections.sort(r);
		for(Building b : buildings){
			if(r.get(0) == (int)b.getBorder(loc,i).distance(loc))
				return b.getBorder(loc,i);
		}
		return null;
	}
	
	public LinkedList<Building> getBuildings() {
		return buildings;
	}
	public void setBuildings(LinkedList<Building> buildings) {
		this.buildings = buildings;
	}
	public void removeBuilding(Building b){
		this.buildings.remove(b);
	}
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
	public void DropMember(Player p){
		
		if(getKing().equalsIgnoreCase(p.getName())){
			remove();
		}// ���� ����
		if(getSubking().equalsIgnoreCase(p.getName())){
			this.subking = "";
			this.members.remove(p.getName());
			if(isChatMod(p))ChatMod(p);
		}// �ο� Ż��
		if(members.contains(p.getName())){
			this.members.remove(p.getName());
			if(isChatMod(p))ChatMod(p);
		}// Ż��
		try {
			saveFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StatAPI.refresh(p);
		
	}
	public void DropMember(String s){
		Player p = Bukkit.getPlayer(s);
		if(p != null){
			DropMember(p);
		}else{
			if(getKing().equalsIgnoreCase(s)){
				remove();
			}// ���� ����
			if(getSubking().equalsIgnoreCase(s)){
				this.subking = "";
				this.members.remove(s);
			}// �ο� Ż��
			if(members.contains(s)){
				this.members.remove(s);
			}// Ż��
			try {
				saveFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public Country getEnemy() {
		return enemy;
	}
	public void setEnemy(Country enemy) {
		this.enemy = enemy;
	}
	public void readyWar(Country c){
		setEnemy(c);
		c.setEnemy(this);
		Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+" ");
		Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+" * "+getName()+"������ "+c.getName()+"�������� ������ �����߽��ϴ�.");
		Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+" ");
		message("�����غ� - 3�� "+ChatColor.RED+"���뱹�� : "+c.getName());
		message("3�е��� ������ �غ��մϴ�.");
		message("������ ���۵Ǹ� ���뱹���� ���信 �� �� �ֽ��ϴ�.");
		c.message("�����غ� - 3�� "+ChatColor.RED+"���뱹�� : "+getName());
		c.message("3�е��� ������ �غ��մϴ�.");
		c.message("������ ���۵Ǹ� ���뱹���� ���信 �� �� �ֽ��ϴ�.");
		
		Timer tim = new Timer();
		tim.schedule(new TimerTask() {
			
			@Override
			public void run() {
				startWar(c);
			}
		}, 1000*60*3);
		
	}
	public void startWar(Country c){
		this.war = true;
		c.war = true;
		Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+" ");
		Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+" * "+getName()+"������ "+c.getName()+"������ ������ ���۵Ǿ����ϴ�.");
		Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+" ");
		message(c.getName()+"������ ������ �����մϴ�. 10�� �� ������ ����˴ϴ�.");
		c.message(getName()+"������ ������ �����մϴ�. 10�� �� ������ ����˴ϴ�.");

		Timer tim = new Timer();
		tim.schedule(new TimerTask() {
			int count;
			@Override
			public void run() {
				if(++count>=10*60){
					Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+" ");
					Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+" * "+getName()+"������ "+c.getName()+"������ ������ �������ϴ�.");
					Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+" ");
					war = false;
					c.war = false;
					enemy = null;
					c.enemy = null;
					message("������ �������ϴ�.");
					c.message("������ �������ϴ�.");
					this.cancel();
				}
				if(war == false || c.war == false){
					this.cancel();
				}
			}
		}, 1000, 1000);
	}

	public boolean isWar() {
		return war;
	}
	public void setWar(boolean war) {
		this.war = war;
	}
	
	public String getInfo(){
		String s = "��6[       ��f"+name+"       ��6]��f\n �� ��  �� : "+ChatColor.RED+king+ChatColor.WHITE+"\n �� ��  �� : "+ChatColor.LIGHT_PURPLE+subking+ChatColor.WHITE+"\n �� ��  �� : "+ChatColor.GREEN+members.size()+ChatColor.WHITE+"��\n �� ��ȣ�� : "+ChatColor.GREEN+buildings.size()+"��f��";
		s+=memberName();
		return s;
	}
	public String memberName(){
		String s = "";
		for(String z : members){
			Player p = Bukkit.getPlayer(z);
			if(p != null){
				s=ChatColor.RED+"\n[��f "+getRank(z)+ChatColor.RED+" ] "+ChatColor.WHITE+z+" - "+ChatColor.YELLOW+"������"+s;
			}else{
				s=s+ChatColor.GRAY+"\n[ "+getRank(z)+" ] "+z+" - ��������";
			}
		}
		return s;
	}
	
	public void remove(){
		for(String s : members){
			Player p = Bukkit.getPlayer(s);
			if(p != null){
				members.remove(s);
				StatAPI.refresh(p);
			}
		}
		this.chatmod.clear();
		this.king="";
		this.subking="";
		this.enemy = null;
		//this.sinho = null;
		main.cl.remove(this);
		File f = new File("yeomryo\\country\\"+name+".yr");
		f.delete();
	}

	public boolean isKing(Player p){
		return this.king.equalsIgnoreCase(p.getName());
	}
	public void addBuilding(Building b){
		this.buildings.add(b);
	}
	
	public boolean hasBeacon(){
		if(this.buildings.size()>0)return true;
		return false;
	}

	public boolean isKing(String p){
		return this.king.equalsIgnoreCase(p);
	}
	
	public boolean hasPower(Player p){
		return this.king.equalsIgnoreCase(p.getName()) || this.subking.equalsIgnoreCase(p.getName());
	}
	
	public void saveFile() throws IOException{
		File f = new File("yeomryo\\country");
		f.mkdirs();
		f = new File("yeomryo\\country\\"+name+".yr");
		FileWriter fw = new FileWriter(f);
		String s = name+"*"+king+"*"+subking+"*"+world.getName()+"*"+maxbuilding+"*";
		for(int i=0;i<buildings.size();i++){
			Building b = buildings.get(i);
			Block bc = b.getBeacon();
			if(i==buildings.size()-1) s+=b.getX1()+"/"+b.getY1()+"/"+b.getZ1()+"/"+b.getX2()+"/"+b.getY2()+"/"+b.getZ2()+"/"+bc.getX()+"/"+bc.getY()+"/"+bc.getZ();
			else s+=b.getX1()+"/"+b.getY1()+"/"+b.getZ1()+"/"+b.getX2()+"/"+b.getY2()+"/"+b.getZ2()+"/"+bc.getX()+"/"+bc.getY()+"/"+bc.getZ()+"|";
		}
		if(buildings.size()==0)s+="0";
		s+="*";
		for(int i=0;i<members.size();i++){
			s=s+members.get(i);
			if(i+1 != members.size())s+="/";
		}
		fw.write(s);
		fw.close();
		System.out.println(f.getPath());
	}
	
	
	public void loadFile(String path) throws IOException{
		File f = new File(path);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s=br.readLine();
		br.close();
		fr.close();
		this.name = s.split("\\*")[0];
		this.king = s.split("\\*")[1];
		this.subking = s.split("\\*")[2];
		this.world = Bukkit.getWorld(s.split("\\*")[3]);
		this.maxbuilding = Integer.parseInt(s.split("\\*")[4]);
		
		String sin = s.split("\\*")[5]; // ��ȣ��
		if(!sin.equalsIgnoreCase("0")){
			for(String a:sin.split("\\|")){
				Building b = new Building();
				int x1=Integer.parseInt(a.split("\\/")[0]);
				int y1=Integer.parseInt(a.split("\\/")[1]);
				int z1=Integer.parseInt(a.split("\\/")[2]);
				int x2=Integer.parseInt(a.split("\\/")[3]);
				int y2=Integer.parseInt(a.split("\\/")[4]);
				int z2=Integer.parseInt(a.split("\\/")[5]);
				int xb=Integer.parseInt(a.split("\\/")[6]);
				int yb=Integer.parseInt(a.split("\\/")[7]);
				int zb=Integer.parseInt(a.split("\\/")[8]);
				b.setX1(x1);
				b.setY1(y1);
				b.setZ1(z1);
				b.setX2(x2);
				b.setY2(y2);
				b.setZ2(z2);
				b.setCountry(this);
				b.setBeacon(world.getBlockAt(xb,yb,zb));
				this.buildings.add(b);
			}
		}
		
		String memb = s.split("\\*")[6];
		for(String m : memb.split("\\/")){
			this.members.add(m);
		}
	}
}
