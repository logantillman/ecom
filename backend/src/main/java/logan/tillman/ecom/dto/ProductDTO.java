package logan.tillman.ecom.dto;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class ProductDTO {
    Integer productId;
    String title;
    String description;
    OffsetDateTime releaseDate;
    List<CategoryDTO> categories;
}
