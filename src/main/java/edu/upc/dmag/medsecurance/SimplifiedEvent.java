package edu.upc.dmag.medsecurance;

import edu.upc.dmag.medsecurance.requirements.*;

import java.util.Objects;

public class SimplifiedEvent {
    private final String name;
    private final String type;
    
    public SimplifiedEvent(Event event){
        if (event.getGeneralEvent() != null){
            type = "GeneralEvent";
            name = event.getGeneralEvent().getName();
        } else if (event.getChangeEvent() != null){
            type = "ChangeEvent";
            name = event.getChangeEvent().getName();

        } else if (event.getContentIdentificationEvent() != null){
            type = "ContentIdentificationEvent";
            name = event.getContentIdentificationEvent().getName();

        } else if (event.getFailureEvent() != null){
            type = "FailureEvent";
            name = event.getFailureEvent().getName();

        } else if (event.getSuccessEvent() != null){
            type = "SuccessEvent";
            name = event.getSuccessEvent().getName();

        } else if (event.getActionPerformedEvent() != null){
            type = "ActionPerformedEvent";
            name = event.getActionPerformedEvent().getName();

        } else if (event.getDisclosureEvent() != null){
            type = "DisclosureEvent";
            name = event.getDisclosureEvent().getName();

        } else if (event.getSafetyRelatedIncident() != null){
            type = "SafetyRelatedIncident";
            name = event.getSafetyRelatedIncident().getName();

        } else if (event.getSecurityRelatedIncident() != null){
            type = "SecurityRelatedIncident";
            name = event.getSecurityRelatedIncident().getName();

        } else if (event.getCompromissionEvent() != null){
            type = "CompromissionEvent";
            name = event.getCompromissionEvent().getName();

        } else if (event.getCorruptionEvent() != null){
            type = "CorruptionEvent";
            name = event.getCorruptionEvent().getName();

        } else if (event.getAttackEvent() != null){
            type = "AttackEvent";
            name = event.getAttackEvent().getName();

        } else if (event.getDecommissioningEvent() != null){
            type = "DecommissioningEvent";
            name = event.getDecommissioningEvent().getName();

        } else if (event.getOutOfBoundariesEvent() != null){
            type = "OutOfBoundariesEvent";
            name = event.getOutOfBoundariesEvent().getName();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplifiedEvent that)) return false;
        return getName().equals(that.getName()) && getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType());
    }

    @Override
    public String toString() {
        return type+": " + name;
    }
}
