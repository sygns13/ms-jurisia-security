package pj.gob.pe.security.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Input")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdsRequest {

    private List<Long> idUsers;
}
