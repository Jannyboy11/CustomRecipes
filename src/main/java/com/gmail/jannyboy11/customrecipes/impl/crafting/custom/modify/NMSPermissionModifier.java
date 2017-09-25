package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import java.util.Objects;

import org.bukkit.entity.Player;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;

import net.minecraft.server.v1_12_R1.IRecipe;

//TODO implements NBTSerializable?
public class NMSPermissionModifier extends NMSAbstractCraftingModifier<IRecipe, IRecipe> {
    
    private final String permission;
    
    public NMSPermissionModifier(String permission) {
        this.permission = Objects.requireNonNull(permission);
    }

    @Override
    public IRecipe modify(IRecipe base) {
        return new NMSProxyCraftingRecipe((inventoryCrafting, world) -> {
            if (inventoryCrafting.getOwner() instanceof Player) {
                Player player = (Player) inventoryCrafting.getOwner();
                if (!player.hasPermission(permission)) return false;
            }
            
            return base.a(inventoryCrafting, world);
        },
                base::craftItem,
                base::b,
                base::b,
                base::d,
                base::c,
                base::toBukkitRecipe,
                base::setKey);
    }

    @Override
    protected CraftingModifier createBukkitModifier() {
        return new CRPermissionModifier(this);
    }
    
    public String getPermission() {
        return permission;
    }

}
