package com.bbv.stotage;

import com.bbv.blib.ConfigFile;
import com.bbv.knightblock.KnightBlock;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class KnightBlockMessage implements MessagerDataFile {
    private final ConfigFile configFile;
    public KnightBlockMessage(){
        this.configFile = new ConfigFile(KnightBlock.pl, "Message.yml");
    }
    @Override
    public Configuration getConfig() {
        return null;
    }
    @Override
    public List<String> getMessages(@NotNull String key) {
        return configFile.getConfigFile().getStringList(key);
    }

    @Override
    public void setDefaultMessage(@NotNull String key, @Nullable String value) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(key, value);
        configFile.setDefaultConfigurationFile(values);
    }
    @Override
    public String getPath(){
        return configFile.getPath();
    }
    @Override
    public ConfigFile getConfigBlib() {
        return configFile;
    }
}
