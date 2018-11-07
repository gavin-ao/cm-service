package data.driven.cm.business.system.impl;

import data.driven.cm.business.system.PictureService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.system.PictureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图片service
 * @author hejinkai
 * @date 2018/11/7
 */
@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void insertManyPicture(List<PictureEntity> list) {
        String sql = "insert into sys_picture(picture_id,file_path,real_name,creator,create_at)";
        String valueSql = "(:picture_id,:file_path,:real_name,:creator,:create_at)";
        jdbcBaseDao.executeBachOneSql(sql, valueSql, list);
    }

    @Override
    public void insertPicture(PictureEntity pictureEntity) {
        jdbcBaseDao.insert(pictureEntity, "sys_picture");
    }
}
