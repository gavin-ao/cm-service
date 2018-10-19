package data.driven.cm.business.material;

import data.driven.cm.vo.material.MatProductVO;

import java.util.List;

/**
 * 素材-产品信息表
 * @author hejinkai
 * @date 2018/7/13
 */
public interface MatProductService {

    /**
     * 根据产品分类code查询产品列表
     * @param levelCode
     * @return
     */
    public List<MatProductVO> findMatProductListByCatgLevelCode(String levelCode);

}
