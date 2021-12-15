package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.ResourceAsset;
import ru.perveevm.polygon.api.entities.enums.ResourceStage;

/**
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ResourceAdvancedProperties {
    private String forTypes;
    private Boolean main;
    private ResourceStage[] stages;
    private ResourceAsset[] assets;

    public String getForTypes() {
        return forTypes;
    }

    public Boolean getMain() {
        return main;
    }

    public ResourceStage[] getStages() {
        return stages;
    }

    public ResourceAsset[] getAssets() {
        return assets;
    }
}
