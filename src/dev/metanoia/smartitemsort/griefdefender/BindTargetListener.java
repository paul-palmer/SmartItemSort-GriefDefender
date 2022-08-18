package dev.metanoia.smartitemsort.griefdefender;

import dev.metanoia.smartitemsort.BindDropTargetEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.function.Supplier;

import static org.bukkit.event.EventPriority.HIGHEST;



public final class BindTargetListener implements Listener {

    private final SmartItemSortGriefDefender plugin;


    public BindTargetListener(final SmartItemSortGriefDefender plugin) {
        this.plugin = plugin;

        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        final Plugin dependentPlugin = pluginManager.getPlugin("GriefDefender");

    }


    // BindDropTargetEvent occurs whenever the SmartItemSort plugin attempts to bind a teleport target
    // to a specific teleport source location. If the event is canceled, the transfer/teleport of dropped
    // items between the two locations will not be allowed to occur. This event does not occur for every item
    // teleported, but only when the SmartItemSort plugin is looking for new teleport targets for an item.
    @EventHandler(priority = HIGHEST, ignoreCancelled = true)
    public void onBindDropTarget(final BindDropTargetEvent e) {
        final Block source = e.getSource();
        final ItemFrame target = e.getTarget();

        if (!isPermittedTarget(source, target)) {
            // canceling the event will prevent this target from being used to receive items from this
            // source location.
            debug(() -> String.format("Canceled targeting of %s from %s", target, source.getLocation()));
            e.setCancelled(true);
        }
    }


    private boolean isPermittedTarget(final Block srcBlock, final ItemFrame target) {
        final Config config = this.plugin.getPluginConfig();
        final boolean ignoreHeight = config.getIgnoreHeight();
        final boolean ignoreSubClaims = config.getIgnoreSubClaims();


        trace(() -> "Source and target are in the same claim.");
        return true;
    }


    private void debug(Supplier<String> message) { this.plugin.debug(message); }
    private void error(Supplier<String> message) { this.plugin.error(message); }
    private void info(Supplier<String> message) { this.plugin.info(message); }
    private void trace(Supplier<String> message) { this.plugin.trace(message); }

}
