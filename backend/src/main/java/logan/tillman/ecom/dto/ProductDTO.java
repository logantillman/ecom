package logan.tillman.ecom.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    Integer productId;
    String title;
    String description;
    OffsetDateTime releaseDate;
    List<CategoryDTO> categories;
}
