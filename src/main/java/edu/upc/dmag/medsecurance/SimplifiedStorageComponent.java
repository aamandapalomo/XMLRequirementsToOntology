package edu.upc.dmag.medsecurance;

import edu.upc.dmag.medsecurance.requirements.StorageComponentContent;

import java.util.Objects;

public class SimplifiedStorageComponent {
    private final String name;
    private final String parentName;
    private final String parentStorage;

    public SimplifiedStorageComponent(StorageComponentContent storageComponentContent){
        name = storageComponentContent.getName();
        if (storageComponentContent.getParentComponent() != null){
            parentName = storageComponentContent.getParentComponent().getName();
        } else {
            parentName = null;
        }
        if (storageComponentContent.getParentStorage() != null){
            parentStorage = storageComponentContent.getParentStorage().getName();
        } else {
            parentStorage = null;
        }
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentStorage() {
        return parentStorage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplifiedStorageComponent that)) return false;
        return getName().equals(that.getName()) && Objects.equals(getParentName(), that.getParentName()) && Objects.equals(getParentStorage(), that.getParentStorage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParentName(), getParentStorage());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Storage: "+name);
        if (parentName != null){
            builder.append("\n\tparent: ").append(parentName);
        }
        if (parentStorage != null){
            builder.append("\n\tparent storage: ").append(parentStorage);
        }
        return builder.toString();
    }
}
