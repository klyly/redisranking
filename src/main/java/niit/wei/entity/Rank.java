package niit.wei.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WeiJinLong
 * @Date 2023-08-15 16:52
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rank implements Serializable {
    private Integer id;
    private String countryName;
    private Integer goldCount;
    private Integer silverCount;
    private Integer bronzeCount;
    private Integer allCount;
    private Double allScore;
}
