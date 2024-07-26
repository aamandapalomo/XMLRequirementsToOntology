package edu.upc.dmag.medsecurance;

import edu.upc.dmag.medsecurance.ExtractingRequirementInfo.NotesRenames;
import edu.upc.dmag.medsecurance.ExtractingRequirementInfo.RequirementOrigin;
import edu.upc.dmag.medsecurance.ExtractingRequirementInfo.RequirementOriginInStandard;
import edu.upc.dmag.medsecurance.requirements.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static Requirements unmarshal() throws IOException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(Requirements.class);
        return (Requirements) context.createUnmarshaller()
                .unmarshal(Main.class.getClassLoader().getResourceAsStream("Requirements.xml"));
    }

    private static List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    private static InputStream getResourceAsStream(String resource) {
        return Main.class.getClassLoader().getResourceAsStream(resource);
    }

    public static Map<String, RequirementOrigin> extract_standard_to_clause_to_requirements() throws IOException {
        var requirementOrigins = new LinkedHashMap<String, RequirementOrigin>();

        List<String> filenames = getResourceFiles("notes/Requirements");

        filenames.stream().filter(it -> it.startsWith("Requirement that")|it.startsWith("Requirements that")).forEach(fileName -> {
            URL url = Main.class.getClassLoader().getResource("notes/Requirements/"+fileName);
            requirementOrigins.put(fileName.replace(".md","").toLowerCase().replaceAll("\\s+", " "), extract_info_from_standard(url));
        });

        return requirementOrigins;
    }

    private static RequirementOrigin extract_info_from_standard(URL url) {
        try (
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ){

            Pattern bulletPointPattern = Pattern.compile("- \\[\\[([^\\|]*)(\\|.*)?\\]\\] as \\[\\[(.*) requirement\\]\\]", Pattern.CASE_INSENSITIVE);
            Pattern headerPattern = Pattern.compile("# According to \\[\\[(.*)]]:", Pattern.CASE_INSENSITIVE);
            Pattern subHeaderPattern = Pattern.compile("## According to \\[\\[([^\\|]*)(?:\\|.*)?]]:", Pattern.CASE_INSENSITIVE);


            boolean found_list = false;
            String currentStandard = null;
            Map<String, String> clauseToStandard = new HashMap<>();
            var fileContent = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            Map<String, RequirementOriginInStandard> originInStandard = new HashMap<>();


            for(String line : fileContent.lines().toList()){
                if (found_list) {
                    var matcher = bulletPointPattern.matcher(line);
                    if (!matcher.find()){
                        System.out.println(line);
                        System.out.println(url.getFile());
                    }
                    var clauseName = matcher.group(1);
                    var modalVerb = matcher.group(3);
                    var standardName = clauseToStandard.get(clauseName);

                    if (!originInStandard.containsKey(standardName)){
                        originInStandard.put(standardName, new RequirementOriginInStandard());
                    }
                    originInStandard.get(standardName).getClauseToModalVerb().put(clauseName, modalVerb);
                } else {
                    if (line.startsWith("Listed in:")){
                        found_list = true;
                    } else {
                        if (line.startsWith("# According to ")){
                            var matcher = headerPattern.matcher(line);
                            if (!matcher.find()){
                                System.out.println(line);
                                System.out.println(url.getFile());
                            }
                            currentStandard = matcher.group(1);
                        } else if (line.startsWith("## According to ")){
                            var matcher = subHeaderPattern.matcher(line);
                            if (!matcher.find()){
                                System.out.println(line);
                            }
                            clauseToStandard.put(
                                    matcher.group(1),
                                    currentStandard
                            );
                        }
                    }
                }
            }
            return new RequirementOrigin(originInStandard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        try {
            Requirements requirements = unmarshal();

            var requirementToOrigins = extract_standard_to_clause_to_requirements();
            NotesRenames notesRenames = new NotesRenames();
            requirements.getDocumentationRequirementOrPublishRequirementOrEnsuringRequirement().forEach(
                it -> {
                    var requirementOrigins = requirementToOrigins.get(it.getName().replaceAll("\\s+"," ").trim().toLowerCase());
                    if (requirementOrigins == null){
                        requirementOrigins = requirementToOrigins.get(it.getName().replaceAll("\\s+"," ").trim().toLowerCase().replace("requirement","requirements"));
                        if (requirementOrigins == null) {
                            if (notesRenames.renames.containsKey(it.getName().replaceAll("\\s+"," ").trim())){
                                requirementOrigins = requirementToOrigins.get(notesRenames.renames.get(it.getName().replaceAll("\\s+"," ").trim()).toLowerCase());
                            }
                            if (requirementOrigins == null && it.getNoteName() != null){
                                if (requirementToOrigins.containsKey(it.getNoteName().replaceAll("\\s+"," ").trim().toLowerCase())){
                                    requirementOrigins = requirementToOrigins.get(it.getNoteName().replaceAll("\\s+"," ").trim().toLowerCase());
                                }

                            }
                            if (requirementOrigins == null){
                                System.out.println(it.getName().replaceAll("\\s+"," "));
                                return;
                            }
                        }
                    }

                    it.getRequirementOrigin().clear();
                    for (var entry: requirementOrigins.getOriginInStandards().entrySet()){
                        var requirementOriginsToAdd = new RequirementOrigins();
                        requirementOriginsToAdd.setSpecification(entry.getKey());

                        for (var clauseToModal: entry.getValue().getClauseToModalVerb().entrySet()){
                            var clauseOrigin = new ClauseOrigin();
                            clauseOrigin.setClause(clauseToModal.getKey());
                            clauseOrigin.setModalVerb(clauseToModal.getValue());
                            requirementOriginsToAdd.getClauseOrigin().add(clauseOrigin);
                        }
                        it.getRequirementOrigin().add(requirementOriginsToAdd);
                    }
                }
            );
            JAXBContext context = JAXBContext.newInstance(Requirements.class);
            try(FileWriter fileWriter = new FileWriter("./extended_requirements.xml")){
                context.createMarshaller().marshal(requirements, fileWriter);
            }
        } catch (IOException | JAXBException /*| IllegalAccessException | InvocationTargetException*/ e) {
            throw new RuntimeException(e);
        }
    }
}