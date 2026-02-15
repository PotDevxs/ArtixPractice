package dev.artixdev.practice.models;

// import com.sk89q.worldedit.extent.clipboard.Clipboard; // WorldEdit dependency not available
// import com.sk89q.worldedit.world.World; // WorldEdit dependency not available
import java.io.File;
import org.bukkit.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.api.practice.nametag.util.VersionUtil;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

public class SchematicData {
    
    private transient File schematicFile;
    @SerializedName("schematicName")
    private String schematicName;
    private transient Object clipboard;
    private static final Logger LOGGER = LogManager.getLogger(SchematicData.class);
    
    private static final String SCHEMATIC_EXTENSION;

    static {
        String extension;
        if (VersionUtil.MINOR_VERSION <= 12) {
            extension = ".schematic";
        } else {
            extension = ".schem";
        }
        SCHEMATIC_EXTENSION = extension;
    }

    public SchematicData() {
    }

    public SchematicData(File file, Object clipboard) {
        this.schematicFile = file;
        this.schematicName = file.getName().replace(SCHEMATIC_EXTENSION, "");
        this.clipboard = clipboard;
    }

    public void clearClipboard() {
        this.clipboard = null;
    }

    public File getSchematicFile() {
        if (this.schematicFile != null) {
            return this.schematicFile;
        } else {
            File file = new File(
                dev.artixdev.practice.Main.getInstance().getDataFolder(),
                this.schematicName + SCHEMATIC_EXTENSION
            );
            
            if (file.exists()) {
                return file;
            } else {
                LOGGER.info("Schematic file not found: " + this.schematicName + SCHEMATIC_EXTENSION);
                return null;
            }
        }
    }

    public void deleteSchematic() {
        File file = this.getSchematicFile();
        
        if (file != null && file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                LOGGER.info("Failed to delete schematic file: " + file.getName());
            }
        }
    }

    public String getSchematicName() {
        return this.schematicName;
    }

    public Object getClipboard(Object world, Object location) {
        // This method would load the clipboard from the schematic file
        // Implementation depends on WorldEdit API usage
        return this.clipboard;
    }

    public void setSchematicName(String schematicName) {
        this.schematicName = schematicName;
    }

    public void setSchematicFile(File schematicFile) {
        this.schematicFile = schematicFile;
    }

    public void setClipboard(Object clipboard) {
        this.clipboard = clipboard;
    }

    public boolean hasClipboard() {
        return this.clipboard != null;
    }

    public boolean isLoaded() {
        return this.clipboard != null;
    }
}
