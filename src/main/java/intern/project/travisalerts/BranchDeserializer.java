//package intern.project.travisalerts;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import jdk.nashorn.internal.parser.JSONParser;
//
//import java.io.IOException;
//
//public class BranchDeserializer extends StdDeserializer<Branch> {
//    public BranchDeserializer () {
//        this(null);
//    }
//
//    public BranchDeserializer(Class<?> vc) {
//        super(vc);
//    }
//
//    @Override
//    public Branch deserialize(JsonParser jp, DeserializationContext context)
//        throws IOException, JsonProcessingException {
//        JsonNode node = jp.getCodec().readTree(jp);
//        String name = node.get("name").asText();
//        boolean existsOnGithub = node.get("exists_on_github").asBoolean();
//        boolean defaultBranch = node.get("default_branch").asBoolean();
//
//    }
//
//}
