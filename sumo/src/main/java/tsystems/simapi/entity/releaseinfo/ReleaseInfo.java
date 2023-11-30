package tsystems.simapi.entity.releaseinfo;

import lombok.*;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseInfo {
    private String releaseId;
    private String metaTrack;
    private boolean isHardwareChangesAllowed;
    private List<String> brands;
    private List<String> models;
    private List<String> countries;
    private List<FunctionInfo> functions;
    private String releaseDate;
    private String releaseStatus;
}
