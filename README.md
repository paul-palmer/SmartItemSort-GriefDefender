# SmartItemSort-GriefDefender

This plugin is not useful on its own.
However, if installed with SmartItemSort and GriefDefender plugins, will enable the [SmartItemSort](https://www.spigotmc.org/resources/smart-item-sort.100831/)
plugin to honor and enforce [GriefDefender](https://www.spigotmc.org/resources/1-12-2-1-19-2-griefdefender-claim-plugin-grief-prevention-protection.68900/) land claims.
Specifically, items will only be teleported from teleport pads or smart item sorters to teleport targets if they are within the same claim.
For the purposes of the plugin, a sub-claim is considered to be a claim separate from the claim that contains it.
That is, a teleport pad or smart item sorter in a claim will not teleport items into a sub-claim or vice-versa.

Teleport pads or smart item sorters in unclaimed land will either not work at all or will teleport to any target in range
(even in claimed land) depending on a config setting.
