package com.bbv.stotage;

import com.bbv.blib.ConfigFile;
import com.bbv.knightblock.KnightBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class KnightBlockConfig implements ConfigurationFile, TreeOfWorld {
    private final ConfigFile configFile;
    private Location treeOfWorld;
    private List<Location> locOfBeam;

    public KnightBlockConfig() {
        this.configFile = new ConfigFile(KnightBlock.pl, "Config.yml");
        HashMap<String, Object> dataSets = new HashMap<>();
        dataSets.put("TreeOfWorld", null);
        dataSets.put("PrefixMessage", "&7[&fKnight&2Block&7]");
        configFile.setDefaultConfigurationFile(dataSets);
        this.treeOfWorld = configFile.getConfigFile().getLocation("TreeOfWorld.location");
        this.locOfBeam = (List<Location>) configFile.getConfigFile().getList("TreeOfWorld.locOfBeam");
    }

    @Override
    public Configuration getConfig() {
        return configFile.getConfigFile();
    }

    @Override
    public String getPath() {
        return configFile.getPath();
    }

    @Override
    public ConfigFile getConfigBlib() {
        return configFile;
    }

    @Override
    public Location getTreeOfWorld() {
        return treeOfWorld;
    }

    @Override
    public List<Location> getLocOfBeams() {
        return locOfBeam;
    }

    @Override
    public void setTreOfWorld(Location location) {
        configFile.setConfigurationFile("TreeOfWorld.location", location);
        this.treeOfWorld = location;
    }

    @Override
    public void setLocOfBeams(List<Location> locOfBeams) {
        configFile.setConfigurationFile("TreeOfWorld.locOfBeam", locOfBeams);
        this.locOfBeam = locOfBeams;
    }

    @Override
    public TreeOfWorld getTreOfWorld() {
        return this;
    }

    @Override
    public String getPrefixMessage() {
        return configFile.getConfigFile().getString("PrefixMessage");
    }
}
