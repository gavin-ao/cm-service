package data.driven.cm.controller.tourism;

import data.driven.cm.business.tourism.ScenicSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 景区controller
 * @author 何晋凯
 * @date 2018/06/06
 */
@Controller
@RequestMapping(path = "/tourism/scenic-spot")
public class ScenicSpotController {
    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotController.class);

    @Autowired
    private ScenicSpotService scenicSpotService;


}
