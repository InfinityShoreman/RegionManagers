package me.sirsavary.townmanager.objects;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TaxType {
	FLAT("Flat"),
	INCOME("Income"),
	RESOURCE("Resource"),
	PROPERTY("Property"),
	NONE("None");

	private String name;

    private static final Map<String, TaxType> mapping = new HashMap<String, TaxType>();

    static {
        for (TaxType type : EnumSet.allOf(TaxType.class)) {
            mapping.put(type.name, type);
        }
    }

    private TaxType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TaxType fromName(String name) {
        return mapping.get(name);
    }
    
    public static Collection<TaxType> getSkills() {
    	return mapping.values();
    }
}
