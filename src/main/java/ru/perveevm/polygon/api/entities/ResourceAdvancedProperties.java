package ru.perveevm.polygon.api.entities;

import ru.perveevm.polygon.api.entities.enums.ResourceAsset;
import ru.perveevm.polygon.api.entities.enums.ResourceStage;

/**
 * Represents special resources properties for IOI-style graders.
 *
 * @author Perveev Mike (perveev_m@mail.ru)
 */
public class ResourceAdvancedProperties {
    private String forTypes;
    private Boolean main;
    private ResourceStage[] stages;
    private ResourceAsset[] assets;

    /**
     * @return Solution extensions for that this resource can be applied.
     */
    public String getForTypes() {
        return forTypes;
    }

    /**
     * @return Reserved to <code>false</code> according to the documentation.
     */
    public Boolean getMain() {
        return main;
    }

    /**
     * @return An array of stages when the resource is used.
     */
    public ResourceStage[] getStages() {
        return stages;
    }

    /**
     * @return An array of assets in which the resource is used.
     */
    public ResourceAsset[] getAssets() {
        return assets;
    }
}
