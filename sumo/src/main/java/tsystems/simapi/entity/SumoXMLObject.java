package tsystems.simapi.entity;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@JacksonXmlRootElement(localName = "routes")
public class SumoXMLObject {

    @JacksonXmlElementWrapper(localName = "vehicle", useWrapping = false)
    @JacksonXmlProperty(localName = "vehicle")
    private List<Vehicle> vehicles;

    @Data
    @Getter
    @Setter
    public static class Vehicle {
        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(isAttribute = true)
        private String type;

        @JacksonXmlProperty(isAttribute = true)
        private String depart;

        @JacksonXmlProperty(isAttribute = true)
        private String departPos;

        @JacksonXmlProperty(isAttribute = true)
        private String departSpeed;

        @JacksonXmlProperty(isAttribute = true)
        private String arrivalPos;

        @JacksonXmlProperty(isAttribute = true)
        private String arrivalSpeed;

        @JacksonXmlElementWrapper(localName = "param", useWrapping = false)
        @JacksonXmlProperty(localName = "param")
        private List<Param> params;

        @JacksonXmlProperty(localName = "route")
        private Route route;

        @Getter
        @Setter
        @Data
        public static class Route{
            @JacksonXmlProperty(isAttribute = true)
            private String edges;
        }

        @Getter
        @Setter
        @Data
        public static class Param {
            @JacksonXmlProperty(isAttribute = true)
            private String key;

            @JacksonXmlProperty(isAttribute = true)
            private String value;
        }
    }
}
