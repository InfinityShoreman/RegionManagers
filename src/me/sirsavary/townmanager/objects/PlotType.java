package me.sirsavary.townmanager.objects;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PlotType {
	SHOP("Shop"),
	HOUSE("House"),
	GOVERNMENT("Government");

	private String name;

    private static final Map<String, PlotType> mapping = new HashMap<String, PlotType>();

    static {
        for (PlotType race : EnumSet.allOf(PlotType.class)) {
            mapping.put(race.name, race);
        }
    }

    private PlotType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PlotType fromName(String name) {
        return mapping.get(name);
    }
    
    public static Collection<PlotType> getSkills() {
    	return mapping.values();
    }
}
