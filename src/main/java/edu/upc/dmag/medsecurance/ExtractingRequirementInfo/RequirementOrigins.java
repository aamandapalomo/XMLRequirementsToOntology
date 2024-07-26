package edu.upc.dmag.medsecurance.ExtractingRequirementInfo;

import java.util.List;

public class RequirementOrigins {
    private final List<RequirementOrigin> origins;

    public RequirementOrigins(List<RequirementOrigin> origins) {
        this.origins = origins;
    }

    public List<RequirementOrigin> getOrigins() {
        return origins;
    }
}
