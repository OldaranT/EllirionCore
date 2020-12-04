package com.ellirion.core.resource;

import com.ellirion.core.EllirionCore;
import com.ellirion.core.database.DatabaseManager;
import com.ellirion.core.race.model.Race;
import com.ellirion.core.resource.model.Resource;
import com.ellirion.core.util.StringHelper;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class ResourceHelper {

    private static HashMap<String, Resource> RESOURCES = new HashMap();
    private static DatabaseManager DATABASE_MANAGER = EllirionCore.getINSTANCE().getDbManager();

    /**
     * @param resourceName Name of the resource.
     * @param material Material of the resource.
     * @param sizeX Size of the resource at the X-cords.
     * @param sizeY Size of the resource at the Y-cords.
     * @param sizeZ Size of the resource at the Z-cords.
     * @return Return true when successfully added the resource.
     */
    public static boolean addResource(String resourceName, Material material, int sizeX, int sizeY, int sizeZ) {
        if (resourceExists(resourceName)) {
            return false;
        }

        resourceName = normalCasing(resourceName);
        Resource resource = new Resource(resourceName, material, sizeX, sizeY, sizeZ);
        return RESOURCES.putIfAbsent(resourceName, resource) == null;
    }


    /**
     * @param resourceName Name of the resource.
     * @return Return true when resource exists.
     */
    public static boolean resourceExists(String resourceName){
        resourceName = normalCasing(resourceName);
        return RESOURCES.containsValue(resourceName);
    }

    private static String normalCasing(String string) {
        return StringHelper.normalNameCasing(string);
    }

    public static void removeAllResources() {
        RESOURCES.clear();
    }

    public static Collection<Resource> getResources() {
        return RESOURCES.values();
    }

}
