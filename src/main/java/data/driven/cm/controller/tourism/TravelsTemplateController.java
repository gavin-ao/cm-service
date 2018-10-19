package data.driven.cm.controller.tourism;

import data.driven.cm.business.tourism.TravelsPictureTemplateService;
import data.driven.cm.business.tourism.TravelsTextTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 游记模板
 * @author 何晋凯
 * @date 2018/06/07
 */
@Controller
@RequestMapping(path = "/tourism/travels-template")
public class TravelsTemplateController {
    private static final Logger logger = LoggerFactory.getLogger(TravelsTemplateController.class);

    @Autowired
    private TravelsTextTemplateService travelsTextTemplateService;
    @Autowired
    private TravelsPictureTemplateService travelsPictureTemplateService;

}
