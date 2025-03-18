package com.bbv.stotage;

import com.bbv.blib.ConfigFile;
import org.bukkit.configuration.Configuration;

public interface DataFiles {
    Configuration getConfig();
    String getPath();
    ConfigFile getConfigBlib();
}