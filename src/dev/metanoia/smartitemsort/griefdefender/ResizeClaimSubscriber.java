package dev.metanoia.smartitemsort.griefdefender;

import com.griefdefender.api.event.ChangeClaimEvent;
import com.griefdefender.lib.kyori.event.EventSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.function.Supplier;



// Monitor changes to sizes of claims as this may indicate that a item sorter or a target block
// has moved into or out of a claim and this may invalidate previously established bindings between
// an item sorter and targets or possible allow new ones to be established. So, notify SmartItemSOrt
// to flush its cache and recalculate these relationships.
class ResizeClaimSubscriber implements EventSubscriber<ChangeClaimEvent.Resize> {
    public ResizeClaimSubscriber(final SmartItemSortGriefDefender plugin) {
        this.plugin = plugin;
    }

    public void on(final ChangeClaimEvent.Resize event) throws Throwable {
        final World world = Bukkit.getWorld(event.getClaim().getWorldUniqueId());

        debug(() -> String.format("Grief Defender claim changed size in %s", world));
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
