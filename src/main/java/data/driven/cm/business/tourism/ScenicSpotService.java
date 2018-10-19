package data.driven.cm.business.tourism;

import data.driven.cm.entity.tourism.ScenicSpotEntity;

/**
 * @author 何晋凯
 * @date 2018/06/06
 */
public interface ScenicSpotService {

    /**
     * 根据id获取景区对象
     * @param scenicSpotId
     * @return
     */
    public ScenicSpotEntity getScenicSpot(String scenicSpotId);

}
