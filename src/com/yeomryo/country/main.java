package com.yeomryo.country;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.yeomryo.country.Country.Building;
import com.yeomryo.country.Country.Country;
import com.yeomryo.country.Country.CountryAPI;
import com.yeomryo.country.Country.CountryChat;
import com.yeomryo.country.Event.EventManager;
import com.yeomryo.stat.Stat.Stat;
import com.yeomryo.stat.Stat.StatAPI;

public class main extends JavaPlugin{
	public static LinkedList<Country> cl = new LinkedList<>();
	public static HashMap<Player, Country> invite = new HashMap<>();
	
	public void onEnable() {
		// ���� �ҷ���
		cl.clear();
		File f = new File("yeomryo\\country");
		if(!f.exists())
			f.mkdirs();
		File files[] = f.listFiles();
		for(int i = 0;i<files.length;++i){
			Country c = new Country();
				try {
					c.loadFile(files[i].getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			cl.add(c);
		}
		// -------------------------
		System.out.println("������ ���� �÷����� Ȱ��ȭ");
		System.out.println(" ");
		System.out.println("Ȯ�ε� ���� �� : "+cl.size()+"\n");
		System.out.println("�� �÷������� ��� ���۱��� ���ῡ�� �ֽ��ϴ�.");
		for(Country c : cl){
			System.out.println(c.toString()+"\n");
		}
		
		getServer().getPluginManager().registerEvents(new EventManager(), this);
		getServer().getPluginManager().registerEvents(new CountryChat(), this);
	}
	
	@Override
	public void onDisable() {
		for(Country s : cl){
			try {
				s.saveFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("����") || label.equalsIgnoreCase("ctr")){
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("���") || args[0].equalsIgnoreCase("list")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+CountryAPI.countryList());
					return true;
				}
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("info")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							p.sendMessage(c.getInfo());
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ� ���� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("Ż��") || args[0].equalsIgnoreCase("out")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(!c.isKing(p)){
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+c.getName()+"�������� Ż���߽��ϴ�.");
								c.DropMember(p);
								c.message(ChatColor.RED+p.getName()+"���� �������� Ż���߽��ϴ�.");
								StatAPI.refresh(p);
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� �����̶� Ż���� �� �����ϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ� ���� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("remove")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								c.message(ChatColor.RED+"���� "+c.getKing()+"�� ���� "+c.getName()+"������ ������ϴ�.");
								c.remove();
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+"����� ������ �ƴմϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("ä��") || args[0].equalsIgnoreCase("chat")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							c.ChatMod(p);
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("��ȣ��") || args[0].equalsIgnoreCase("beacon")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							for(Building b : c.getBuildings()){
								b.getBeacon().setType(Material.BEACON);
							}
							p.sendMessage("��6[ ��fCou��entry ��6]��f ��ȣ�Ⱑ ����� �Ǿ����ϴ�.");
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("agree")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(invite.containsKey(p)){
							Country c = invite.get(p);
							c.addMember(p);
							invite.remove(p);
							StatAPI.refresh(p);
							c.message(ChatColor.AQUA+p.getName()+"���� "+c.getName()+"������ �����߽��ϴ�.");
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"��ſ��� �� �ʴ밡 �����ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("disagree")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(invite.containsKey(p)){
							Country c = invite.get(p);
							c.message(ChatColor.AQUA+p.getName()+"���� ������ �����߽��ϴ�.");
							invite.remove(p);
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"��ſ��� �� �ʴ밡 �����ϴ�.");
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("�ǹ�����") || args[0].equalsIgnoreCase("build2")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.isOp()){
							String name = "kingdom";
							if(EventManager.lClick.containsKey(p) && EventManager.rClick.containsKey(p)){
								Location l1 = EventManager.lClick.get(p);
								Location l2 = EventManager.rClick.get(p);
								int x1 = l1.getBlockX();
								int y1 = l1.getBlockY();
								int z1 = l1.getBlockZ();
								int x2 = l2.getBlockX();
								int y2 = l2.getBlockY();
								int z2 = l2.getBlockZ();
								LinkedList<Build> blocks = new LinkedList<>();
								for(int x=Math.min(l1.getBlockX(), l2.getBlockX());x<=Math.max(l1.getBlockX(), l2.getBlockX());x++){
									for(int y=Math.min(l1.getBlockY(), l2.getBlockY());y<=Math.max(l1.getBlockY(), l2.getBlockY());y++){
										for(int z=Math.min(l1.getBlockZ(), l2.getBlockZ());z<=Math.max(l1.getBlockZ(), l2.getBlockZ());z++){
											Block bb = p.getWorld().getBlockAt(x,y,z);
											Build b = new Build();
											Location l = p.getLocation();
											b.setX(x-l.getBlockX());
											b.setY(y-l.getBlockY());
											b.setZ(z-l.getBlockZ());
											b.setData(bb.getData());
											b.setMaterial(bb.getType());
											blocks.add(b);
										}
									}
								}
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+"�����");
								saveBuild(name, blocks);
								
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ ���� �����ϼ���. (������� ��Ŭ��, ��Ŭ��)");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ �����ϴ�.");
							return true;
						}
					}
				}
				
				
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("create")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f  ����: /���� ���� [�����̸�]");	
				}
				if(args[0].equalsIgnoreCase("�ʴ�") || args[0].equalsIgnoreCase("invite")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f  ����: /���� �ʴ� [�̸�]");	
				}
				if(args[0].equalsIgnoreCase("��") || args[0].equalsIgnoreCase("king")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f  ����: /���� �� [�̸�]");	
				}
				if(args[0].equalsIgnoreCase("�ο�") || args[0].equalsIgnoreCase("sking")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f  ����: /���� �ο� [�̸�]");	
				}
				
				if(args[0].equalsIgnoreCase("�߹�") || args[0].equalsIgnoreCase("kick")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f  ����: /���� �߹� [�̸�]");	
				}
				if(args[0].equalsIgnoreCase("���Ｑ��") || args[0].equalsIgnoreCase("war")){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f  ����: /���� ���Ｑ�� [�����̸�]");	
				}
				if(args[0].equalsIgnoreCase("������") || args[0].equalsIgnoreCase("item")){
					Player p = (Player)sender;
					if(p.isOp()){
						p.getInventory().addItem(StatAPI.getICON(Material.BEACON,"��f[ ��c���ο� ��ȣ�� ��f]"));
						p.getInventory().addItem(StatAPI.getICON(Material.FLOWER_POT_ITEM,"��4[ ��0��ź ��4]"));
						p.getInventory().addItem(StatAPI.getICON(Material.PAPER,"��f[ ��a���� �߰��� ��f]"));
					}
				}
				
				
				
			}
			else if(args.length == 2){
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("create")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("��ô��") || p.isOp()){
							if(CountryAPI.hasCountry(p)){
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+"����� �̹� ���� ������ �ֽ��ϴ�.");
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+"���� Ż���ϼ���");
								return true;
							}
							String name = args[1];
							if(name.length() <= 4){
								if(CountryAPI.getCountryByName(name) == null){
									try {
										Country c = new Country(name, p);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.BLUE+name+"������ "+p.getName()+"�� ���� �Ǳ��Ǿ����ϴ�.");
									StatAPI.refresh(p);
								}else{
									p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�̹� �����ϴ� �̸��Դϴ�.");
								}
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�������� �ִ� 4�ڱ����� �����մϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+"������ �����ϴ�.");
							return true;
						}
					}
				}
				if(args[0].equalsIgnoreCase("�ʴ�") || args[0].equalsIgnoreCase("invite")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.hasPower(p)){
								if(c.getMembers().size()<4){
									Player t = Bukkit.getPlayer(args[1]);
									if(t!=null){
										if(t.isOnline()){
											if(!CountryAPI.hasCountry(t)){
												if(!invite.containsKey(t)){
													p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.AQUA+t.getName()+"�� "+c.getName()+"������ �ʴ��߽��ϴ�.");
													t.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.AQUA+p.getName()+"���κ��� [ "+c.getName()+" ]������ �ʴ�Ǿ����ϴ�.");
													t.sendMessage(c.getInfo());
													t.sendMessage("��6[ ��fCou��entry ��6]��f "+" /���� ���� - "+c.getName()+"������ �����մϴ�.");
													t.sendMessage("��6[ ��fCou��entry ��6]��f "+" /���� ���� - ������ �����մϴ�.");
													invite.put(t, c);
												}else{
													p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�� �÷��̾�� "+invite.get(t).getName()+"������ ���Ǹ� ó�����Դϴ�.");
												}
											}else{
												p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�� �÷��̾�� �̹� "+CountryAPI.getCountry(t).getName()+"������ �ҼӵǾ��ֽ��ϴ�.");
											}
										}else
											p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�������� �÷��̾ �ƴմϴ�.");
									}else
										p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�������� �÷��̾ �ƴմϴ�.");
								}else{
									p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ �ִ� 4������� �����մϴ�.");
								}
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �����ϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("����") || args[0].equalsIgnoreCase("remove")){
					if(sender.isOp()){
						if(sender instanceof Player){
							Player p = (Player)sender;
							String name = args[1];
							Country t = CountryAPI.getCountryByName(name);
							if(t != null){
								Country c = CountryAPI.getCountry(p);
								Bukkit.broadcastMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"OP "+p.getName()+"�� ���� "+c.getName()+"������ ������ϴ�.");
								c.remove();
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�������� �ʴ� �����Դϴ�.");
							}
						}
					}
				}
				if(args[0].equalsIgnoreCase("��") || args[0].equalsIgnoreCase("king")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								if(c.isMember(args[1])){
									if(p.getName().equalsIgnoreCase(args[1])){
										p.sendMessage("��6[ ��fCou��entry ��6]��f "+"����� �̹� ���Դϴ�.");
									}else{
										
										c.setKing(args[1]);
										c.message(args[1]+"���� ���ο� ���� �Ǿ����ϴ�.");
																				
									}
								}else{
									p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�ش� �÷��̾�� ����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
								}
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ �����ϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("�ο�") || args[0].equalsIgnoreCase("sking")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								if(c.isMember(args[1])){
									if(p.getName().equalsIgnoreCase(args[1])){
										p.sendMessage("��6[ ��fCou��entry ��6]��f "+"����� �̹� ���Դϴ�.");
									}else{
										
										c.setSubking(args[1]);
										c.message(args[1]+"���� ���ο� �ο��� �Ǿ����ϴ�.");
																				
									}
								}else{
									p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�ش� �÷��̾�� ����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
								}
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ �����ϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("�߹�") || args[0].equalsIgnoreCase("kick")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.hasPower(p)){
								if(c.isMember(args[1])){
									if(p.getName().equalsIgnoreCase(args[1])){
										p.sendMessage("��6[ ��fCou��entry ��6]��f "+"�ڱ� �ڽ��� �߹��� �� �����ϴ�.");
									}else{
										if(c.isKing(args[1])){
											p.sendMessage("��6[ ��fCou��entry ��6]��f "+"���� �߹��� �� �����ϴ�.");
										}else{
											c.message(args[1]+"���� "+p.getName()+"�Կ� ���� �������� �߹�Ǿ����ϴ�.");
											c.DropMember(args[1]);
										}									
									}
								}else{
									p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�ش� �÷��̾�� ����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
								}
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ �����ϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("���Ｑ��") || args[0].equalsIgnoreCase("war")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								if(c.getEnemy() == null){
									String name = args[1];
									Country t = CountryAPI.getCountryByName(name);
									if(t != null){
										if(t == c){
											p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ ���� �����Դϴ�.");
											return true;
										}
										if(t.getEnemy() == null){
											if(Bukkit.getPlayer(t.getKing()) != null){
												c.readyWar(t);
											}else{
												p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�ش� ������ ���� �������� �ƴմϴ�.");
											}
										}else{
											p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�ش� ������ �̹� "+t.getEnemy().getName()+"������ �������Դϴ�.");
										}
										
									}else{
										p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"�׷� ������ �����ϴ�.");
									}
								}else{
									p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �̹� "+c.getEnemy().getName()+"������ �������Դϴ�.");
								}
							}else{
								p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"������ �����ϴ�.");
							}
						}else{
							p.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"����� ������ �ҼӵǾ����� �ʽ��ϴ�.");
						}
					}
				}
			}
			else{
				sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.GOLD+"���������� ���� ��ɾ������������");
				if(sender.isOp()){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.LIGHT_PURPLE+" ��OP ���� ��ɾ�");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.LIGHT_PURPLE+"/���� ���� [�����̸�]��f - ������ �����մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.LIGHT_PURPLE+"/���� �ǹ����� ��f- ������ ������ �����մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.LIGHT_PURPLE+" * ������ ���� : ������ ��� ��Ŭ��, ��Ŭ���� ���� ���õ� ����");
				}
				if(CountryAPI.isKing((Player)sender)){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+" ���� ��ɾ�");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"/���� ���Ｑ�� [�����̸�]��f - �ش� ������ ������ �����մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"/���� �� [�г���]��f - �ش� �÷��̾ ������ �Ӹ��մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"/���� �ο� [�г���]��f - �ش� �÷��̾ �ο����� �Ӹ��մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+"/���� ���� - ������ �����մϴ�.");
				}
				if(CountryAPI.hasPower((Player)sender)){
					if(!CountryAPI.isKing((Player)sender))sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.RED+" ���ο� ��ɾ�");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.GREEN+"/���� �ʴ� [�г���] ��f- �ش� �÷��̾�� �ʴ븦 �����ϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.GREEN+"/���� �߹� [�г���] ��f- �ش� �÷��̾ �������� �߹��մϴ�.");
				}

				if(CountryAPI.hasCountry((Player)sender)){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.YELLOW+"/���� ���� ��f- �ڽ��� ���� ������ ������ Ȯ���մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.YELLOW+"/���� Ż�� ��f- �������� Ż���մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.YELLOW+"/���� ä�� ��f- �������� ä�ø�带 �����մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.YELLOW+"/���� ��ȣ�� ��f- ������ ����� ��ȣ�⸦ �����մϴ�.");
				}
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.WHITE+"/���� ���� [�����̸�] ��f- ������ �����մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.WHITE+"/���� ��� ��f- ��� ������ ����� Ȯ���մϴ�.");

				if(!CountryAPI.hasCountry((Player)sender)){
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.WHITE+"/���� ���� ��f- ������ �ʴ뿡 ���մϴ�.");
					sender.sendMessage("��6[ ��fCou��entry ��6]��f "+ChatColor.WHITE+"/���� ���� ��f- ������ �ʴ븦 �����մϴ�.");
				}
				
				return true;
			}
		}
		return true;
	}
	public void saveBuild(String name, LinkedList<Build> list){
		try{
			File f = new File("yeomryo\\build");
			f.mkdirs();
			f = new File("yeomryo\\build\\"+name+".yr");
			FileWriter fw = new FileWriter(f);
			String s="";
			for(Build b : list){
				s=s+b.getMaterial().toString()+"*";//0
				s=s+b.getData()+"*";//1
				if(list.getLast().equals(b))
					s=s+b.getX()+"/"+b.getY()+"/"+b.getZ();//2
				else
					s=s+b.getX()+"/"+b.getY()+"/"+b.getZ()+"*";//2
			}
			fw.write(s);
			fw.close();
			System.out.println(f.getPath());
		}catch(IOException e){
			
		}
	}
	
	public static LinkedList<Build> loadBuild(String name){
		LinkedList<Build> list = null;
		try{
			File f = new File("yeomryo\\build\\"+name+".yr");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s=br.readLine();
			br.close();
			fr.close();

			list = new LinkedList<>();
			
			String[] arg = s.split("\\*");
			
			
			for(int i=0;i<(arg.length)/3;i++){
				Build b = new Build();
				b.setMaterial(Material.getMaterial(arg[3*i]));
				b.setData(Byte.parseByte(arg[3*i+1]));
				String[] a2 = arg[3*i+2].split("\\/");
				b.setX(Integer.parseInt(a2[0]));
				b.setY(Integer.parseInt(a2[1]));
				b.setZ(Integer.parseInt(a2[2]));
				list.add(b);
			}
		}catch(IOException e){
			
		}
		
		return list;
	}
}
