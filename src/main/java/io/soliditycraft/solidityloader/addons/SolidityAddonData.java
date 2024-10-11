package io.soliditycraft.solidityloader.addons;

import lombok.Data;

@Data
public class SolidityAddonData {
    private String name;
    private String version;
    private String description;
    private String main;
    private String id;
    private boolean loadPlugin;
}
