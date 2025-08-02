package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.registry.RenewedEntity;
import com.github.jeffyjamzhd.renewed.registry.RenewedEntityRenderer;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import com.github.jeffyjamzhd.renewed.registry.RenewedRecipes;
import com.google.common.eventbus.Subscribe;
import net.minecraft.ChatMessageComponent;
import net.minecraft.EntityPlayer;
import net.xiaoyu233.fml.reload.event.*;

public class EventListen {

    @Subscribe
    public void onItemRegister(ItemRegistryEvent event) {
        RenewedItems.register(event);
    }

    @Subscribe
    public void onRecipeRegister(RecipeRegistryEvent event) {
        RenewedRecipes.registerRecipes(event);
    }

    @Subscribe
    public void onEntityRegister(EntityRegisterEvent event) {
        RenewedEntity.register(event);
    }

    @Subscribe
    public void onEntityRendererRegister(EntityRendererRegistryEvent event) {
        RenewedEntityRenderer.register(event);
    }

    //玩家登录事件
    @Subscribe
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {

    }

    //指令事件
    @Subscribe
    public void handleChatCommand(HandleChatCommandEvent event) {
        String par2Str = event.getCommand();
        EntityPlayer player = event.getPlayer();
        if (par2Str.startsWith("Hello")) {    //当玩家输入 /Hello
            player.sendChatToPlayer(ChatMessageComponent.createFromText("你好，FML！"));
            event.setExecuteSuccess(true);
        }
    }
}
