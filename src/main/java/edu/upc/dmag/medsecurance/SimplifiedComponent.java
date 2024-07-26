package edu.upc.dmag.medsecurance;

import edu.upc.dmag.medsecurance.requirements.ComponentContent;

import java.util.Objects;

public class SimplifiedComponent {
    private final String name;
    private final String parentName;
    private final String parentStorageName;


    public SimplifiedComponent(ComponentContent componentContent) {
        name = componentContent.getName();
        if (componentContent.getParentComponent() != null){
            parentName = componentContent.getParentComponent().getName();
        } else {
            parentName = null;
        }
        if (componentContent.getParentStorage() != null){
            parentStorageName = componentContent.getParentStorage().getName();
        } else {
            parentStorageName = null;
        }
    }

    public String getName() {
        return name;
    }

    public  String getParentName() {
        return parentName;
    }

    public  String getParentStorageName() {
        return parentStorageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplifiedComponent that)) return false;
        return getName().equals(that.getName()) && Objects.equals(getParentName(), that.getParentName()) && Objects.equals(getParentStorageName(), that.getParentStorageName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParentName(), getParentStorageName());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Component: "+name);
        if (parentName != null){
            builder.append("\n\tparent: ").append(parentName);
        }
        if (parentStorageName != null){
            builder.append("\n\tparent storage: ").append(parentStorageName);
        }
        return builder.toString();
    }
}
