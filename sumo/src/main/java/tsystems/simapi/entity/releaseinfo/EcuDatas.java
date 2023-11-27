package tsystems.simapi.entity.releaseinfo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EcuDatas {
    private String ecu;
    private String hardwareVersion;
    private String componentId;
    private String componentName;
    private String componentVersion;
    private String status;
    private String lastChange;
    private EcuDatasInfo data;
}
