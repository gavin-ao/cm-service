package data.driven.cm.business.system.impl;

import data.driven.cm.business.system.PictureService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.system.PictureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> findPictureByIds(List<String> ids) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ids.size(); i++){
            sb.append(",?");
        }
        sb.delete(0, 1);
        String sql = "select file_path from sys_picture where picture_id in (" + sb + ")";
        List<PictureEntity> list = jdbcBaseDao.queryListWithListParam(PictureEntity.class, sql, ids);
        if(list != null && list.size() > 0){
            return list.stream().collect(Collectors.mapping(o -> o.getFilePath(), Collectors.toList()));
        }
        return null;
    }
}
