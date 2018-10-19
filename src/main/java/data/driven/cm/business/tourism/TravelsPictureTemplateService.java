package data.driven.cm.business.tourism;

import java.util.List;

/**
 * 景区游记图片模板Service
 * @author 何晋凯
 * @date 2018/06/07
 */
public interface TravelsPictureTemplateService {

    /**
     * 获取景区游记图片模板
     * @param scenicSpotId
     * @return
     */
    public List<String> findTravelsPictureTemplateList(String scenicSpotId);
}
