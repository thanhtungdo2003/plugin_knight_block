package com.bbv.stotage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MessagerDataFile extends DataFiles {
    List<String> getMessages(@NotNull String key);
    void setDefaultMessage(@NotNull String key,@Nullable String value);
}
