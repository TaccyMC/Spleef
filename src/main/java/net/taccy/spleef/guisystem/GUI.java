package net.taccy.spleef.guisystem;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GUI implements InventoryHolder {

    protected Inventory inv;
    protected PlayerMenuUtility playerMenuUtility;

    public GUI(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void setMenuItems();
    public abstract void handleMenu(InventoryClickEvent e);

    public void open() {
        inv = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();

        playerMenuUtility.getOwner().openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

}
