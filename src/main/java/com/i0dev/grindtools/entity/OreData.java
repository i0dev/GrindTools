package com.i0dev.grindtools.entity;

import com.i0dev.grindtools.entity.object.Ore;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class OreData extends Entity<OreData> {

    protected static OreData i;

    public static OreData get() {
        return i;
    }

    public void addOreData(Location location, String oreId) {
        oreLocDataList.add(new OreLocData(oreId, PS.valueOf(location)));
    }

    @Getter
    @Setter
    public static class OreLocData {
        private final String oreId;
        private final PS location;
        private long timeMined;
        private long regenTime;

        public OreLocData(String oreId, PS location) {
            this.oreId = oreId;
            this.location = location;
            setNewRegenTime();
        }

        public void setNewRegenTime() {
            long minTime = OreConfig.get().getOreById(oreId).getMinRespawnTimeMillis();
            long maxTime = OreConfig.get().getOreById(oreId).getMaxRespawnTimeMillis();

            this.regenTime = (long) (Math.random() * (maxTime - minTime) + minTime);
        }

        public boolean isMined() {
            return timeMined != 0;
        }


    }


    @Getter
    private List<OreLocData> oreLocDataList = MUtil.list();


    public boolean isLocationOre(Location location) {
        return oreLocDataList.stream().anyMatch(oreLocData -> oreLocData.location.equals(PS.valueOf(location)));
    }

    public Ore getOreByLocation(Location location) {
        return OreConfig.get().getOreById(oreLocDataList.stream().filter(oreLocData -> oreLocData.location.equals(PS.valueOf(location))).findFirst().orElse(null).oreId);
    }

    public void setMinedTime(Location location, long timeMined) {
        oreLocDataList.stream().filter(oreLocData -> oreLocData.location.equals(PS.valueOf(location))).findFirst().orElse(null).setTimeMined(timeMined);
    }

    public void removeOreData(Location location) {
        oreLocDataList.removeIf(oreLocData -> oreLocData.location.equals(PS.valueOf(location)));
    }


    @Override
    public OreData load(OreData that) {
        super.load(that);
        return this;
    }

}


