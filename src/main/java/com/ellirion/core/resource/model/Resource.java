package com.ellirion.core.resource.model;

import com.ellirion.util.model.BoundingBox;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.ellirion.core.util.GenericTryCatchUtils.tryCatch;

public class Resource {

    @Getter @Setter private String name;
    @Getter private Set<String> resources;
    @Getter private Material material;
    @Getter private int sizeX;
    @Getter private int sizeY;
    @Getter private int sizeZ;

    /**
     * This is the resource that can be made TODO: Fix that more materials can be used.
     * @param name The name of the resource, will be stored on the MongoDB server.
     * @param material The material of the resource (Which types of blocks).
     * @param sizeX The total size of X of the resource, in blocks.
     * @param sizeY The total size of Y of the resource, in blocks.
     * @param sizeZ The total size of Z of the resource, in blocks.
     */
    public Resource(final String name, final Material material, final int sizeX, final int sizeY, final int sizeZ){
        this.name = name;
        this.material = material;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        resources = new HashSet<>();
    }

    /**
     * @param resourceName The name of the resource.
     * @return Returns true if successful.
     */
    public boolean addResource(String resourceName){
        return tryCatch(() -> {
            resources.add(resourceName);
        });
    }

    /**
     * @param resourceName The name of the resource.
     * @return Returns true if found.
     */
    public boolean hasResource(String resourceName) {
        return resources.contains(resourceName);
    }

    /**
     * @param resourceName The name of the resource.
     * @return Returns true if successful.
     */
    public boolean removeResource(String resourceName){
        return tryCatch(() -> {
            resources.remove(resourceName);
        });
    }

}
