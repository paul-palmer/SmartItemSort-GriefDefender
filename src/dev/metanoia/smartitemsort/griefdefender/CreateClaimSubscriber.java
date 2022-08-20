package dev.metanoia.smartitemsort.griefdefender;

import com.griefdefender.api.event.CreateClaimEvent;
import com.griefdefender.lib.kyori.event.EventSubscriber;
import dev.metanoia.smartitemsort.SmartItemSort;
import dev.metanoia.smartitemsort.plugin.SmartItemSortPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.function.Supplier;


// Monitor when a claim is created as this may indicate that a item sorter or a target block
// has become part of the new claim and this may invalidate previously established bindings
// between an item sorter and targets or possible allow new ones to be established.
// So, notify SmartItemSort to flush its cache and recalculate these relationships.

class CreateClaimSubscriber implements EventSubscriber<CreateClaimEvent.Post> {
    public CreateClaimSubscriber(final SmartItemSortGriefDefender plugin) {
        this.plugin = plugin;
    }

    public void on(final CreateClaimEvent.Post event) throws Throwable {
        final World world = Bukkit.getWorld(event.getClaim().getWorldUniqueId());

        debug(() -> String.format("New Grief Defender claim created in %s", world.getName()));
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
