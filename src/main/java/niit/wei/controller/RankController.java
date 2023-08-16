package niit.wei.controller;

import niit.wei.entity.Rank;
import niit.wei.service.RankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author WeiJinLong
 * @Date 2023-08-15 17:05
 * @Version 1.0
 */
@Controller
public class RankController {
    @Autowired
    private RankServiceImpl rankService;

    @RequestMapping("addMedal")
    @ResponseBody
    public String addRank(Rank rank) {
        String countryName = rank.getCountryName();
        System.out.println(rank);
      rankService.addCount(countryName, rank);
//        model.addAttribute("ranks",rankList);
        return "ranks";
    }

    @RequestMapping("listRank")
    public String listRank(Model model) {
        List<Rank> ranks = rankService.getRanks();
        model.addAttribute("ranks",ranks);
        return "rankC";
    }

    @RequestMapping("goldIn")
    @ResponseBody
    public String goldIn(@RequestParam("countryName")String countryName,@RequestParam("score") Double score) {
        System.out.println(countryName);
        System.out.println(score);
        rankService.goldIn(countryName,score);
        return  "this.listRank(model)";
    }

}
