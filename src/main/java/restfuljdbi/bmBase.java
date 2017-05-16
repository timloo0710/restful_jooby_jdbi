package restfuljdbi;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Data
@RequiredArgsConstructor
public class bmBase {

	private String license_desc;

	private String license_desc_old;

	private String identify_lice_date;

	private String receive_lice_date;

	private String use_category_code1;

	private double base_area_arc;

	private double base_area_shrink;

	private double base_area_other;

  //10
	private double base_area_total;
	private double tot_house_no;
	private double build_cover_rate;
	private double space_rate;
	private String license_kind;
	private double airraid_u_area;
	private double park_sum;
	private double park_sum3;
	private double price;
	private double statutory_open_space;
  //20
	private double total_constru_area;
	private double building_height;
	private String building_kind_desc;
	private double park_sum1;
	private double park_sum2;
	private double up_floor_no;
	private double dn_floor_no;
	//private double base_area_total;
	private String usage_code_desc;
	private String public_code;
   //30
	private String others_name;
	private String approve_lice_date;
	private String commence_date;
	private String valid_month;
	private String complete_date;
   	


  private List<bmP01> bmp01s;

  private List<bmP02> bmp02s;

  private List<bmP03> bmp03s;

  private List<bmP04> bmp04s;

  private List<bmStair> bmstairs;

  private List<Lan> lans;

  public void setbmP01s(List<bmP01> bmp01s) {
		this.bmp01s = bmp01s;
	}
  public void setbmP02s(List<bmP02> bmp02s) {
		this.bmp02s = bmp02s;
	}
  public void setbmP03s(List<bmP03> bmp03s) {
		this.bmp03s = bmp03s;
	}
  public void setbmP04s(List<bmP04> bmp04s) {
		this.bmp04s = bmp04s;
	}
  public void setbmStairs(List<bmStair> bmstairs) {
		this.bmstairs = bmstairs;
	}
  public void setLans(List<Lan> lans) {
		this.lans = lans;
	}

}
