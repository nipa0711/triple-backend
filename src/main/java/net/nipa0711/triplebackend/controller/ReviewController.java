package net.nipa0711.triplebackend.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.nipa0711.triplebackend.model.History;
import net.nipa0711.triplebackend.model.ReviewRequest;
import net.nipa0711.triplebackend.model.User;
import net.nipa0711.triplebackend.service.ReviewServiceImpl;

@RestController
public class ReviewController {

    @Autowired
    private ReviewServiceImpl reviewService;

    @RequestMapping(method = RequestMethod.POST, path = "/events")
    public Map<String, String> postRequestApi(@RequestBody ReviewRequest reviewRequest) {
        System.out.println("received : " + reviewRequest);
        String result = reviewService.processReview(reviewRequest);
        Map<String, String> res = new HashMap<>();
        res.put("Result", result);
        return res;
    }

    @RequestMapping(value = "/points", method = RequestMethod.GET)
    public Map<String, String> getUserPoints(@RequestBody @RequestParam(value = "userId") String userId) {
        Map<String, String> res = new LinkedHashMap<>();
        System.out.println("received userId : " + userId);
        User user = reviewService.getUser(userId);
        if (user == null) {
            res.put("userId", userId);
            res.put("Result", "Not registered user.");
        } else {
            res.put("userId", userId);
            res.put("Point", String.valueOf(user.getTotalPoint()));
        }
        return res;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public Map<String, String> getUserPointsHistory(@RequestBody @RequestParam(value = "userId") String userId) {
        Map<String, String> res = new LinkedHashMap<>();
        System.out.println("received userId : " + userId);
        User user = reviewService.getUser(userId);
        if (user == null) {
            res.put("userId", userId);
            res.put("Result", "Not registered user.");
            return res;
        }

        List<History> userPointHistory = reviewService.getHistory(userId);
        int i = 1;
        for (History history : userPointHistory) {
            res.put("" + i, history.toString());
            i++;
        }

        return res;
    }
}
