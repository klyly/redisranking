package niit.wei.service;


import niit.wei.entity.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author WeiJinLong
 * @Date 2023-08-15 17:43
 * @Version 1.0
 */
@Service
public class RankServiceImpl {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addCount(String key, Rank rank) {
        String keySet = "country:rankSet";
        String keyMap = "country:rankMap";
        Integer goldCount = rank.getGoldCount();
        Integer silverCount = rank.getSilverCount();
        Integer bronzeCount = rank.getBronzeCount();
        rank.setAllCount(goldCount + silverCount + bronzeCount);
        Double goldSore = (double) (goldCount * 1000);
        Double silverSore = (double) (silverCount);
        Double bronzeSore = bronzeCount * 0.001;
        Double allScore = goldSore + silverSore + bronzeSore;
        rank.setAllScore(allScore);
        redisTemplate.opsForZSet().add(keySet, key, allScore);
        redisTemplate.opsForHash().put(keyMap, key, rank);
    }

    public List<Rank> getRanks() {
        String keySet = "country:rankSet";
        String keyMap = "country:rankMap";
        Set<ZSetOperations.TypedTuple<Object>> set = redisTemplate.opsForZSet().reverseRangeWithScores(keySet, 0, -1);
        List<Rank> ranks = new ArrayList<>();
        Iterator<ZSetOperations.TypedTuple<Object>> it = set.iterator();
        while (it.hasNext()) {
            ZSetOperations.TypedTuple<Object> obj = it.next();
            Double score = obj.getScore();
            Object value = obj.getValue();
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, value);
            o.setAllScore(score);
            ranks.add(o);
        }
        int id = 1;
        for (int i = 0; i < ranks.size(); i++) {
            ranks.get(0).setId(1);
//            System.out.println(ranks.get(i).getAllScore());
            if (i > 0) {
                if (Objects.equals(ranks.get(i).getAllScore(), ranks.get(i - 1).getAllScore())) {
                    ranks.get(i).setId(id);
                } else {
                    id++;
                    ranks.get(i).setId(id);
                }
            }
        }
        return ranks;
    }

    public void goldIn(String countryName,Double score) {
        String keySet = "country:rankSet";
        String keyMap = "country:rankMap";
        if (score == 1000){
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, countryName);
            Integer goldCount = o.getGoldCount();
            o.setGoldCount(goldCount + 1);
            addCount(countryName,o);
        }else if (score == 1){
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, countryName);
            Integer silverCount = o.getSilverCount();
            o.setSilverCount(silverCount + 1);
            addCount(countryName,o);
        }else if (score == 0.001){
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, countryName);
            Integer bronzeCount = o.getBronzeCount();
            o.setBronzeCount(bronzeCount + 1);
            addCount(countryName,o);
        }else if (score == -1){
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, countryName);
            Integer silverCount = o.getSilverCount();
            o.setSilverCount(silverCount - 1);
            addCount(countryName,o);
        }else if (score == -1000){
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, countryName);
            Integer goldCount = o.getGoldCount();
            o.setGoldCount(goldCount - 1);
            addCount(countryName,o);
        }else if (score == -0.001){
            Rank o = (Rank) redisTemplate.opsForHash().get(keyMap, countryName);
            Integer bronzeCount = o.getBronzeCount();
            o.setBronzeCount(bronzeCount - 1);
            addCount(countryName,o);
        }
    }
}
