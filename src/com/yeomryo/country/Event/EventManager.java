package com.yeomryo.country.Event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.yeomryo.country.Build;
import com.yeomryo.country.main;
import com.yeomryo.country.Country.Building;
import com.yeomryo.country.Country.Country;
import com.yeomryo.country.Country.CountryAPI;
import com.yeomryo.stat.Stat.Stat;
import com.yeomryo.stat.Stat.StatAPI;

import net.minecraft.server.v1_5_R3.EntityCreature;
import net.minecraft.server.v1_5_R3.PathEntity;
import net.minecraft.server.v1_5_R3.PathPoint;

public class EventManager implements Listener{

	public static HashMap<Player, Location> lClick= new HashMap<>();
	public static HashMap<Player, Location> rClick= new HashMap<>();
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		if(CountryAPI.hasCountry(e.getPlayer())){
			Country c = CountryAPI.getCountry(e.getPlayer());
			if(c.isChatMod(e.getPlayer())){
				c.ChatMod(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getPlayer().isOp()){
			Player p = e.getPlayer();
			ItemStack is = p.getItemInHand();
			if(is != null){
				if(is.getType() == Material.QUARTZ){ // 석영
					Location l = e.getClickedBlock().getLocation();
					if(e.getAction() == Action.LEFT_CLICK_BLOCK){
						lClick.put(e.getPlayer(), l);
						p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.GREEN+"첫번째 좌표 - X:"+l.getBlockX()+" Y:"+l.getBlockY()+" Z:"+l.getBlockZ());
					}if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
						rClick.put(e.getPlayer(), l);
						p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.GREEN+"두번째 좌표 - X:"+l.getBlockX()+" Y:"+l.getBlockY()+" Z:"+l.getBlockZ());
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClickE(InventoryClickEvent e){
		if(e.getInventory().getName().contains(ChatColor.GOLD+"워프")){
			ItemStack is = e.getCurrentItem();
			Player p = (Player)e.getInventory().getHolder();
			if(e.isLeftClick()){
				if(is != null){
					if(is.getType() == Material.BEACON){
						if(is.hasItemMeta()){
							if(is.getItemMeta().getDisplayName().contains("번"+ChatColor.WHITE+" 신호기")){
								String s = is.getItemMeta().getLore().get(0);
								String name = is.getItemMeta().getDisplayName().split(ChatColor.GREEN+"")[1].split("국가")[0];
								Country c = CountryAPI.getCountryByName(name);
								int x,y,z;
								x=Integer.parseInt(s.split("\\: ")[1].split(" ")[0]);
								y=Integer.parseInt(s.split("\\: ")[2].split(" ")[0]);
								z=Integer.parseInt(s.split("\\: ")[3].split(" ")[0]);
								p.getPlayer().teleport(new Location(c.getWorld(), x, y, z));

								Timer t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										p.openInventory(e.getInventory());
									}
								},10);
								e.setCancelled(true);
							}
						}
					}else if(is.getType() == Material.SIGN){
						if(is.hasItemMeta()){
							if(is.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA+"닫기"))
								p.closeInventory();
						}
					}
				}
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInvenopen(InventoryOpenEvent e){
		
			if(e.getInventory().getType() == InventoryType.BEACON){
	
				Player p = (Player)e.getPlayer();
				Block bl = e.getPlayer().getTargetBlock(null, 0);
				if(bl != null){
					if(CountryAPI.hasCountry(p)){
						Country c = CountryAPI.getCountry(p);
						for(Building b : c.getBuildings()){
							if(b.getBeacon().equals(bl)){
								
						Timer t = new Timer();
						t.schedule(new TimerTask() {
							
							@Override
							public void run() {
								Inventory iv = Bukkit.createInventory(p, 9, ChatColor.GOLD+"워프");
								for(int i=0;i<c.getBuildings().size();i++){
									Building bc = c.getBuildings().get(i);
									iv.addItem(StatAPI.getICON(Material.BEACON, ChatColor.GREEN+c.getName()+"국가 "+ChatColor.YELLOW+""+(i+1)+"번"+ChatColor.WHITE+" 신호기", 
									ChatColor.GOLD+"X: "+bc.getBeacon().getX()+" Y: "+(bc.getBeacon().getY()+1)+" Z: "+bc.getBeacon().getZ(),
									ChatColor.AQUA+"클릭 시 해당 위치로 이동합니다."));
								}
								iv.setItem(8, StatAPI.getICON(Material.SIGN,ChatColor.AQUA+"닫기"));
								p.openInventory(iv);
							}
						}
						, 10);
						e.setCancelled(true);
					break;
							}
						}
				}
			}
		}
	}
	@EventHandler
	public void onBB(BlockBreakEvent e){
		if(e.getBlock().getType() == Material.BEACON){
			if(e.getPlayer().isOp()){
				for(Country c : main.cl){
					Building b = null;
					for(Building bb : c.getBuildings()){
						if(bb.getBeacon().equals(e.getBlock())){
							b = bb;
							break;
						}
					}
					if(b!=null){
						c.removeBuilding(b);
						c.message("OP에 의해 신호기가 파괴되었습니다.");
					}
				}
			}
			if(CountryAPI.hasCountry(e.getPlayer())){
				Country c = CountryAPI.getCountry(e.getPlayer());
				if(c.inArea(e.getBlock().getLocation())){
					for(Building b : c.getBuildings()){
						if(b.getBeacon().equals(e.getBlock())){
							if(!e.getPlayer().isOp()){
								e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"자신의 신호기는 부술 수 없습니다.");
								e.setCancelled(true);
								return;
							}
						}
					}
				}
				if(c.getEnemy() != null && c.isWar()){
					Country t = c.getEnemy();
					if(t.inArea(e.getBlock().getLocation())){
						// 비콘 부쉈을때
						for(Building b : t.getBuildings()){
							if(b.getBeacon().equals(e.getBlock())){
								b.getBeacon().setType(Material.AIR);
								t.removeBuilding(b);
								c.message("적의 신호기를 하나 부쉈습니다. (남은 신호기:"+ChatColor.GREEN+t.getBuildings().size()+ChatColor.WHITE+"개)");
								t.message("신호기를 하나 잃었습니다. (남은 신호기:"+ChatColor.GREEN+t.getBuildings().size()+ChatColor.WHITE+"개)");
							}
						}
					}else{
						if(!e.getPlayer().isOp()){
							e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"자신의 신호기는 부술 수 없습니다.");
							e.setCancelled(true);
						}
					}
				}
			}
		}
		
		if(e.getPlayer().isOp()){
			if(e.getPlayer().getItemInHand() != null){
				if(e.getPlayer().getItemInHand().getType() == Material.QUARTZ){
					e.setCancelled(true);
				}
			}
		}else{
			for(Country c : main.cl){
				if(c.inArea(e.getBlock().getLocation())){
					if(!c.isMember(e.getPlayer())){
						if(c.getEnemy() != null){
							if(!c.getEnemy().isMember(e.getPlayer()) || !c.isWar()){
								e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이곳은 "+c.getName()+"국가의 구역입니다.");
								e.setCancelled(true);
							}
						}else{
							e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이곳은 "+c.getName()+"국가의 구역입니다.");
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void rightclick(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand() != null){
				Player p = e.getPlayer();
				ItemStack is = p.getItemInHand();
				if(is.getType() == Material.PAPER){
					if(is.getItemMeta().hasDisplayName()){
						if(is.getItemMeta().getDisplayName().equalsIgnoreCase("§f[ §a국가 추가권 §f]")){
							Country c = CountryAPI.getCountry(p);
							if(!c.isKing(p)){
								e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"당신은 왕이 아닙니다.");
								e.setCancelled(true);
								return;
							}
							if(c.getMaxbuilding() >= 5){
								e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"국가는 최대 5개까지 추가할 수 있습니다.");
								e.setCancelled(true);
								return;
							}
							c.setMaxbuilding(c.getMaxbuilding()+1);
							c.message("§a추가 가능한 국가 수가 1 증가했습니다. §6("+c.getMaxbuilding()+"/"+5+")");
							
							if(is.getAmount()==1){p.setItemInHand(new ItemStack(0)); p.updateInventory(); }
							else {is.setAmount(is.getAmount()-1);}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getPlayer().isOp()){
		}else{
			for(Country c : main.cl){
				if(c.inArea(e.getPlayer())){
					if(!c.isMember(e.getPlayer())){
						if(c.getEnemy() != null){
							if(!c.getEnemy().isMember(e.getPlayer()) || !c.isWar()){
								e.getPlayer().teleport(c.getBorder(e.getPlayer().getLocation(),2));
								e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이곳은 "+c.getName()+"국가의 구역입니다.");
								return;
							}
						}else{
							e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이곳은 "+c.getName()+"국가의 구역입니다.");
							e.getPlayer().teleport(c.getBorder(e.getPlayer().getLocation(),2));
							return;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBp(BlockPlaceEvent e){
		if(e.getPlayer().getItemInHand() != null){
			if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null){
				if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§4[ §0폭탄 §4]")){
					e.getPlayer().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
					e.getBlock().setType(Material.AIR);
				}
			}
		}
		if(e.getBlock().getType() == Material.BEACON){
			if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§f[ §c새로운 신호기 §f]")){
				Player p = e.getPlayer();
					if(CountryAPI.isKing(e.getPlayer())){
						Country c = CountryAPI.getCountry(e.getPlayer());
						String name = "kingdom";
						LinkedList<Build> bl = main.loadBuild(name);
						if(bl!=null){
							if(c.getBuildings().size()<c.getMaxbuilding()){
								Location l = e.getBlock().getLocation();
								if(e.getPlayer().getLocation().getBlockY() == e.getBlock().getY()){
								Building bu = new Building();
								for(Build b : bl){
									Block bb = p.getWorld().getBlockAt(l.getBlockX()+b.getX(), l.getBlockY()+b.getY(), l.getBlockZ()+b.getZ());
									if(!CountryAPI.canBuild(bb.getLocation())){
										p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"주변에 다른 국가가 있습니다.");
										e.setCancelled(true);
										return;
									}
								}
								for(Build b : bl){
									Block bb = p.getWorld().getBlockAt(l.getBlockX()+b.getX(), l.getBlockY()+b.getY(), l.getBlockZ()+b.getZ());
									bb.setType(b.getMaterial());
									bb.setData(b.getData());
									if(b.getMaterial() == Material.BEACON)
									bu.setBeacon(bb);
								}
								Build a = bl.getFirst();
								Build bb = bl.getLast();
								bu.setBorder(l.getBlockX()+a.getX(), l.getBlockY()+a.getY(), l.getBlockZ()+a.getZ(), l.getBlockX()+bb.getX(), l.getBlockY()+bb.getY(), l.getBlockZ()+bb.getZ());
								bu.setCountry(c);
								c.addBuilding(bu);
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"건설되었습니다.");
								c.message("신호기가 하나 설치되었습니다. (신호기 : "+ChatColor.GREEN+c.getBuildings().size()+ChatColor.WHITE+"개)");
								e.getBlock().setType(Material.AIR);
								return;
							}else{
								p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"같은 높이에 땅에 설치하세요.");
								e.setCancelled(true);
								return;
							}
						}else{
							p.sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"신호기는 최대 "+c.getMaxbuilding()+"개까지만 설치할 수 있습니다.");
							e.setCancelled(true);
						}
					}else{
						return;
					}
				}
				e.setCancelled(true);
			}
		}
		if(e.getPlayer().isOp()){
		}else{
			for(Country c : main.cl){
				if(c.inArea(e.getBlock().getLocation())){
					if(!c.isMember(e.getPlayer())){
						if(c.getEnemy() != null){
							if(!c.getEnemy().isMember(e.getPlayer()) || !c.isWar()){
								e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이곳은 "+c.getName()+"국가의 구역입니다.");
								Player p = e.getPlayer();
								e.setCancelled(true);
							}
						}else{
							
							e.getPlayer().sendMessage("§6[ §fCou§entry §6]§f "+ChatColor.RED+"이곳은 "+c.getName()+"국가의 구역입니다.");
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDMGB(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player p = (Player)e.getDamager();
			Player t = (Player)e.getEntity();
			
			if(CountryAPI.hasCountry(p) || CountryAPI.hasCountry(t)){
				if(CountryAPI.getCountry(p) == CountryAPI.getCountry(t)){
					e.setCancelled(true);
				}
			}
		}
	}
}
