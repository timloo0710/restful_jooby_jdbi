package restfuljdbi;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class bmStair {

  private String building_no;

  private String story_code;

  private double story_height;

  private double story_area;
  
  private String usage_code_desc;

  

}
