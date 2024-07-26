package edu.upc.dmag.medsecurance.ExtractingRequirementInfo;

import edu.upc.dmag.medsecurance.requirements.Specification;

import java.util.HashMap;
import java.util.Map;

public class RequirementOrigin {
    private final Map<Specification, RequirementOriginInStandard> originInStandards;

    public RequirementOrigin(Map<String, RequirementOriginInStandard> originInStandards) {
        this.originInStandards = new HashMap<Specification, RequirementOriginInStandard>();
        originInStandards.forEach((specificationName, origins) -> {
            Specification specification = new Specification();
            specification.setName(specificationName);
            this.originInStandards.put(specification, origins);
        });
    }

    public Map<Specification, RequirementOriginInStandard> getOriginInStandards() {
        return originInStandards;
    }
}
