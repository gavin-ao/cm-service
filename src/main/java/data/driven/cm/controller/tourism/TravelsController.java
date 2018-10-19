package data.driven.cm.controller.tourism;

import data.driven.cm.business.tourism.PictureService;
import data.driven.cm.business.tourism.TravelsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 景区游记controller
 * @author 何晋凯
 * @date 2018/06/07
 */
@Controller
@RequestMapping(path = "/tourism/travels")
public class TravelsController {

    private static final Logger logger = LoggerFactory.getLogger(TravelsController.class);

    @Autowired
    private PictureService pictureService;
    @Autowired
    private TravelsService travelsService;

}
