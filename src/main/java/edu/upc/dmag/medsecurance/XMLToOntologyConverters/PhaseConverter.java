package edu.upc.dmag.medsecurance.XMLToOntologyConverters;

import edu.upc.dmag.medsecurance.OntologyElements.OntologyPhase;
import edu.upc.dmag.medsecurance.requirements.Phase;

public class PhaseConverter {
    public static OntologyPhase convert(Phase phase){
        return new OntologyPhase(phase.getName());
    }
}
