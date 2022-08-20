package dev.metanoia.smartitemsort.griefdefender;

import com.griefdefender.api.Core;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import dev.metanoia.smartitemsort.BindDropTargetEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.function.Supplier;

import static org.bukkit.event.EventPriority.HIGHEST;



final class BindTargetListener implements Listener {

    public BindTargetListener(final SmartItemSortGriefDefender plugin) {
        this.plugin = plugin;
        this.griefDefenderCoreApi = GriefDefender.getCore();
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


    ///
    /// Private methods
    ///

    // getAttachedBlock returns the block that the indicated item frame is attached to
    private Block getAttachedBlock(final ItemFrame itemFrame) {
        return itemFrame.getLocation().getBlock().getRelative(itemFrame.getAttachedFace());
    }


    // isPermittedTarget() returns true of the itemSorter should be allowed to send items to the
    // indicated target.
    private boolean isPermittedTarget(final Block itemSorter, final ItemFrame target) {
        final Config config = this.plugin.getPluginConfig();

        // get information about the claim that contains the item sorter
        final Claim itemSorterClaim = this.griefDefenderCoreApi.getClaimAt(itemSorter.getLocation());
        trace(() -> String.format("Iem Sorter claim: %s", itemSorterClaim));

        // if the teleport is initiated from unclaimed land, any destination within range is allowed as long
        // as item sorters in unclaimed land are allowed.
        if (itemSorterClaim == null || itemSorterClaim.isWilderness()) {
            if (config.getAllowFromUnclaimedLand()) {
                trace(() -> String.format("All nearby targets allowed by policy. %s is in unclaimed land.", itemSorter));
                return true;
            }

            debug(() -> String.format("Item teleport from %s is not allowed by policy. It is in unclaimed land.", itemSorter));
            return false;
        }

        // see if the target is in the same claim as the source block.
        final Block targetBlock = getAttachedBlock(target);
        final Claim targetClaim = this.griefDefenderCoreApi.getClaimAt(targetBlock.getLocation());
        trace(() -> String.format("Target claim: %s", targetClaim));

        // are the sorter and the target in the same claim?
        if (!itemSorterClaim.equals(targetClaim)) {
            debug(() -> String.format("Target cannot be bound to source. Source claim is %s. Target claim is %s.", itemSorterClaim, targetClaim));
            return false;
        }

        trace(() -> "Source and target are in the same claim.");
        return true;
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
    private final Core griefDefenderCoreApi;
}
