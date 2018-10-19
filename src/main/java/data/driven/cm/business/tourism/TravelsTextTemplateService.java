package data.driven.cm.business.tourism;

import java.util.List;

/**
 * 景区游记文本模板
 * @author 何晋凯
 * @date 2018/06/06
 */
public interface TravelsTextTemplateService {

    /**
     * 获取景区游记文字模板
     * @param scenicSpotId
     * @return
     */
    public List<String> findTravelsTextTemplateList(String scenicSpotId);

}
