package tsystems.simapi.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.stereotype.Component;
import tsystems.simapi.entity.SumoXMLObject;

import java.io.*;

@Component
public class SumoFileGateway {

    private String ReadFile(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public SumoXMLObject readDefaultConfig() throws JsonProcessingException {
        String xmlContent = ReadFile("sumo_scenario" +
                                            File.separator +
                                            "default_route.xml");
        XmlMapper xmlMapper = new XmlMapper();
        SumoXMLObject value = xmlMapper.readValue(xmlContent, SumoXMLObject.class);
        return value;
    }

    public void writeConfigs(SumoXMLObject configs) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
        try {
            // Write the Java object to an XML file
            File outputFile = new File("sumo_scenario" +
                                                File.separator +
                                                "routes.xml");
            xmlMapper.writeValue(outputFile, configs);
            System.out.println("Java object has been written to output.xml as XML.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
