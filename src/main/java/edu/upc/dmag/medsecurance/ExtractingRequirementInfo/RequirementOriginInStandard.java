package edu.upc.dmag.medsecurance.ExtractingRequirementInfo;

import java.util.HashMap;
import java.util.Map;

public class RequirementOriginInStandard {
    private final Map<String, String> clauseToModalVerb;

    public RequirementOriginInStandard() {
        this.clauseToModalVerb = new HashMap<>();
    }

    public Map<String, String> getClauseToModalVerb() {
        return clauseToModalVerb;
    }
}
