package data.driven.cm.controller.tourism;

import data.driven.cm.business.tourism.SceneryService;
import data.driven.cm.business.tourism.ScenicSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 何晋凯
 * @date 2018/06/06
 */
@Controller
@RequestMapping(path = "/tourism/scenery")
public class SceneryController {

    private static final Logger logger = LoggerFactory.getLogger(SceneryController.class);

    @Autowired
    private SceneryService sceneryService;
    @Autowired
    private ScenicSpotService scenicSpotService;


}
