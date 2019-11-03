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
		// 파일 불러옴
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
		System.out.println("염료의 국가 플러그인 활성화");
		System.out.println(" ");
		System.out.println("확인된 국가 수 : "+cl.size()+"\n");
		System.out.println("본 플러그인의 모든 저작권은 염료에게 있습니다.");
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
		if(label.equalsIgnoreCase("국가") || label.equalsIgnoreCase("ctr")){
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("목록") || args[0].equalsIgnoreCase("list")){
					sender.sendMessage("§6[ §fCou§entry §6]§f "+CountryAPI.countryList());
					return true;
				}
				if(args[0].equalsIgnoreCase("정보") || args[0].equalsIgnoreCase("info")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							p.sendMessage(c.getInfo());
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어 있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("탈퇴") || args[0].equalsIgnoreCase("out")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(!c.isKing(p)){
								p.sendMessage("§6[ §fCou§entry §6]§f "+c.getName()+"국가에서 탈퇴했습니다.");
								c.DropMember(p);
								c.message(ChatColor.RED+p.getName()+"님이 국가에서 탈퇴했습니다.");
								StatAPI.refresh(p);
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국왕이라 탈퇴할 수 없습니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어 있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("삭제") || args[0].equalsIgnoreCase("remove")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								c.message(ChatColor.RED+"국왕 "+c.getKing()+"에 의해 "+c.getName()+"국가가 사라집니다.");
								c.remove();
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+"당신은 국왕이 아닙니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("채팅") || args[0].equalsIgnoreCase("chat")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							c.ChatMod(p);
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("신호기") || args[0].equalsIgnoreCase("beacon")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							for(Building b : c.getBuildings()){
								b.getBeacon().setType(Material.BEACON);
							}
							p.sendMessage("§6[ §fCou§entry §6]§f 신호기가 재생성 되었습니다.");
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("수락") || args[0].equalsIgnoreCase("agree")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(invite.containsKey(p)){
							Country c = invite.get(p);
							c.addMember(p);
							invite.remove(p);
							StatAPI.refresh(p);
							c.message(ChatColor.AQUA+p.getName()+"님이 "+c.getName()+"국가에 가입했습니다.");
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신에게 온 초대가 없습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("거절") || args[0].equalsIgnoreCase("disagree")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(invite.containsKey(p)){
							Country c = invite.get(p);
							c.message(ChatColor.AQUA+p.getName()+"님이 가입을 거절했습니다.");
							invite.remove(p);
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신에게 온 초대가 없습니다.");
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("건물설정") || args[0].equalsIgnoreCase("build2")){
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
								p.sendMessage("§6[ §fCou§entry §6]§f "+"저장됨");
								saveBuild(name, blocks);
								
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"범위를 먼저 지정하세요. (석영들고 좌클릭, 우클릭)");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"권한이 없습니다.");
							return true;
						}
					}
				}
				
				
				if(args[0].equalsIgnoreCase("생성") || args[0].equalsIgnoreCase("create")){
					sender.sendMessage("§6[ §fCou§entry §6]§f  사용법: /국가 생성 [국가이름]");	
				}
				if(args[0].equalsIgnoreCase("초대") || args[0].equalsIgnoreCase("invite")){
					sender.sendMessage("§6[ §fCou§entry §6]§f  사용법: /국가 초대 [이름]");	
				}
				if(args[0].equalsIgnoreCase("왕") || args[0].equalsIgnoreCase("king")){
					sender.sendMessage("§6[ §fCou§entry §6]§f  사용법: /국가 왕 [이름]");	
				}
				if(args[0].equalsIgnoreCase("부왕") || args[0].equalsIgnoreCase("sking")){
					sender.sendMessage("§6[ §fCou§entry §6]§f  사용법: /국가 부왕 [이름]");	
				}
				
				if(args[0].equalsIgnoreCase("추방") || args[0].equalsIgnoreCase("kick")){
					sender.sendMessage("§6[ §fCou§entry §6]§f  사용법: /국가 추방 [이름]");	
				}
				if(args[0].equalsIgnoreCase("전쟁선포") || args[0].equalsIgnoreCase("war")){
					sender.sendMessage("§6[ §fCou§entry §6]§f  사용법: /국가 전쟁선포 [국가이름]");	
				}
				if(args[0].equalsIgnoreCase("아이템") || args[0].equalsIgnoreCase("item")){
					Player p = (Player)sender;
					if(p.isOp()){
						p.getInventory().addItem(StatAPI.getICON(Material.BEACON,"§f[ §c새로운 신호기 §f]"));
						p.getInventory().addItem(StatAPI.getICON(Material.FLOWER_POT_ITEM,"§4[ §0폭탄 §4]"));
						p.getInventory().addItem(StatAPI.getICON(Material.PAPER,"§f[ §a국가 추가권 §f]"));
					}
				}
				
				
				
			}
			else if(args.length == 2){
				if(args[0].equalsIgnoreCase("생성") || args[0].equalsIgnoreCase("create")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(p.hasPermission("개척자") || p.isOp()){
							if(CountryAPI.hasCountry(p)){
								p.sendMessage("§6[ §fCou§entry §6]§f "+"당신은 이미 속한 국가가 있습니다.");
								p.sendMessage("§6[ §fCou§entry §6]§f "+"먼저 탈퇴하세요");
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
									Bukkit.broadcastMessage("§6[ §fCou§entry §6]§f "+ChatColor.BLUE+name+"국가가 "+p.getName()+"에 의해 건국되었습니다.");
									StatAPI.refresh(p);
								}else{
									p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이미 존재하는 이름입니다.");
								}
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"국가명은 최대 4자까지만 가능합니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+"권한이 없습니다.");
							return true;
						}
					}
				}
				if(args[0].equalsIgnoreCase("초대") || args[0].equalsIgnoreCase("invite")){
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
													p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.AQUA+t.getName()+"을 "+c.getName()+"국가에 초대했습니다.");
													t.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.AQUA+p.getName()+"으로부터 [ "+c.getName()+" ]국가에 초대되었습니다.");
													t.sendMessage(c.getInfo());
													t.sendMessage("§6[ §fCou§entry §6]§f "+" /국가 수락 - "+c.getName()+"국가에 가입합니다.");
													t.sendMessage("§6[ §fCou§entry §6]§f "+" /국가 거절 - 제안을 거절합니다.");
													invite.put(t, c);
												}else{
													p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"그 플레이어는 "+invite.get(t).getName()+"국가의 제의를 처리중입니다.");
												}
											}else{
												p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"그 플레이어는 이미 "+CountryAPI.getCountry(t).getName()+"국가에 소속되어있습니다.");
											}
										}else
											p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"접속중인 플레이어가 아닙니다.");
									}else
										p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"접속중인 플레이어가 아닙니다.");
								}else{
									p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"국민은 최대 4명까지만 가능합니다.");
								}
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 권한이 없습니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("삭제") || args[0].equalsIgnoreCase("remove")){
					if(sender.isOp()){
						if(sender instanceof Player){
							Player p = (Player)sender;
							String name = args[1];
							Country t = CountryAPI.getCountryByName(name);
							if(t != null){
								Country c = CountryAPI.getCountry(p);
								Bukkit.broadcastMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"OP "+p.getName()+"에 의해 "+c.getName()+"국가가 사라집니다.");
								c.remove();
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"존재하지 않는 국가입니다.");
							}
						}
					}
				}
				if(args[0].equalsIgnoreCase("왕") || args[0].equalsIgnoreCase("king")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								if(c.isMember(args[1])){
									if(p.getName().equalsIgnoreCase(args[1])){
										p.sendMessage("§6[ §fCou§entry §6]§f "+"당신은 이미 왕입니다.");
									}else{
										
										c.setKing(args[1]);
										c.message(args[1]+"님이 새로운 왕이 되었습니다.");
																				
									}
								}else{
									p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"해당 플레이어는 당신의 국가에 소속되어있지 않습니다.");
								}
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"권한이 없습니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("부왕") || args[0].equalsIgnoreCase("sking")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.isKing(p)){
								if(c.isMember(args[1])){
									if(p.getName().equalsIgnoreCase(args[1])){
										p.sendMessage("§6[ §fCou§entry §6]§f "+"당신은 이미 왕입니다.");
									}else{
										
										c.setSubking(args[1]);
										c.message(args[1]+"님이 새로운 부왕이 되었습니다.");
																				
									}
								}else{
									p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"해당 플레이어는 당신의 국가에 소속되어있지 않습니다.");
								}
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"권한이 없습니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("추방") || args[0].equalsIgnoreCase("kick")){
					if(sender instanceof Player){
						Player p = (Player)sender;
						if(CountryAPI.hasCountry(p)){
							Country c = CountryAPI.getCountry(p);
							if(c.hasPower(p)){
								if(c.isMember(args[1])){
									if(p.getName().equalsIgnoreCase(args[1])){
										p.sendMessage("§6[ §fCou§entry §6]§f "+"자기 자신을 추방할 수 없습니다.");
									}else{
										if(c.isKing(args[1])){
											p.sendMessage("§6[ §fCou§entry §6]§f "+"왕은 추방할 수 없습니다.");
										}else{
											c.message(args[1]+"님이 "+p.getName()+"님에 의해 국가에서 추방되었습니다.");
											c.DropMember(args[1]);
										}									
									}
								}else{
									p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"해당 플레이어는 당신의 국가에 소속되어있지 않습니다.");
								}
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"권한이 없습니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
				if(args[0].equalsIgnoreCase("전쟁선포") || args[0].equalsIgnoreCase("war")){
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
											p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"본인이 속한 나라입니다.");
											return true;
										}
										if(t.getEnemy() == null){
											if(Bukkit.getPlayer(t.getKing()) != null){
												c.readyWar(t);
											}else{
												p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"해당 국가는 왕이 접속중이 아닙니다.");
											}
										}else{
											p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"해당 국가는 이미 "+t.getEnemy().getName()+"국가와 전쟁중입니다.");
										}
										
									}else{
										p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"그런 국가는 없습니다.");
									}
								}else{
									p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신의 국가는 이미 "+c.getEnemy().getName()+"국가와 전쟁중입니다.");
								}
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"권한이 없습니다.");
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 국가에 소속되어있지 않습니다.");
						}
					}
				}
			}
			else{
				sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.GOLD+"───국가 관련 명령어───────");
				if(sender.isOp()){
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.LIGHT_PURPLE+" ┌OP 전용 명령어");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.LIGHT_PURPLE+"/국가 삭제 [국가이름]§f - 국가를 삭제합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.LIGHT_PURPLE+"/국가 건물설정 §f- 선택한 영역을 저장합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.LIGHT_PURPLE+" * 선택한 영역 : 석영을 들고 좌클릭, 우클릭을 통해 선택된 영역");
				}
				if(CountryAPI.isKing((Player)sender)){
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+" ┌왕 명령어");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"/국가 전쟁선포 [국가이름]§f - 해당 국가에 전쟁을 선포합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"/국가 왕 [닉네임]§f - 해당 플레이어를 왕으로 임명합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"/국가 부왕 [닉네임]§f - 해당 플레이어를 부왕으로 임명합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"/국가 삭제 - 국가를 삭제합니다.");
				}
				if(CountryAPI.hasPower((Player)sender)){
					if(!CountryAPI.isKing((Player)sender))sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+" ┌부왕 명령어");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.GREEN+"/국가 초대 [닉네임] §f- 해당 플레이어에게 초대를 보냅니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.GREEN+"/국가 추방 [닉네임] §f- 해당 플레이어를 국가에서 추방합니다.");
				}

				if(CountryAPI.hasCountry((Player)sender)){
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.YELLOW+"/국가 정보 §f- 자신이 속한 국가의 정보를 확인합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.YELLOW+"/국가 탈퇴 §f- 국가에서 탈퇴합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.YELLOW+"/국가 채팅 §f- 국가전용 채팅모드를 변경합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.YELLOW+"/국가 신호기 §f- 오류로 사라진 신호기를 복원합니다.");
				}
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.WHITE+"/국가 생성 [국가이름] §f- 국가를 생성합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.WHITE+"/국가 목록 §f- 모든 국가의 목록을 확인합니다.");

				if(!CountryAPI.hasCountry((Player)sender)){
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.WHITE+"/국가 수락 §f- 국가의 초대에 응합니다.");
					sender.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.WHITE+"/국가 거절 §f- 국가의 초대를 거절합니다.");
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
