package dev.metanoia.smartitemsort.griefdefender;

import com.griefdefender.api.event.RemoveClaimEvent;
import com.griefdefender.lib.kyori.event.EventSubscriber;
import dev.metanoia.smartitemsort.plugin.SmartItemSortPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.function.Supplier;



// Monitor when a claim is removed as this may indicate that a item sorter or a target block
// has moved into the wilderness or become part of a parent claim and this may invalidate previously
// established bindings between an item sorter and targets or possible allow new ones to be established.
// So, notify SmartItemSort to flush its cache and recalculate these relationships.
class RemoveClaimSubscriber implements EventSubscriber<RemoveClaimEvent> {
    public RemoveClaimSubscriber(final SmartItemSortGriefDefender plugin) {
        this.plugin = plugin;
    }

    public void on(final RemoveClaimEvent event) throws Throwable {
        final World world = Bukkit.getWorld(event.getClaim().getWorldUniqueId());

        debug(() -> String.format("Grief Defender claim removed in %s", world.getName()));
        SmartItemSortPlugin.invalidateCache(world);
    }


    ///
    /// Private forwarders
    ///

    private void debug(Supplier<String> message) { this.plugin.debug(message); }
    private void trace(Supplier<String> message) { this.plugin.trace(message); }



    ///
    /// Private members
    ///

    private final SmartItemSortGriefDefender plugin;
}
