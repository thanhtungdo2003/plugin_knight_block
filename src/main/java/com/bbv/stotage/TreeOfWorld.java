package com.bbv.stotage;

import org.bukkit.Location;

import java.util.List;

public interface TreeOfWorld{
    Location getTreeOfWorld();
    List<Location> getLocOfBeams();
    void setTreOfWorld(Location location);
    void setLocOfBeams(List<Location> locOfBeams);
}
