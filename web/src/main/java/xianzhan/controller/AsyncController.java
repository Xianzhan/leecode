package xianzhan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xianzhan.pojo.vo.ResultVO;
import xianzhan.service.IAsyncService;

import java.util.concurrent.Future;

/**
 * @author xianzhan
 * @since 2018-05-15
 */
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    private IAsyncService asyncService;

    /**
     * 返回需要 13 秒的任务
     * 实际上 7 秒就返回了
     */
    @GetMapping("sleep13")
    public ResultVO sleep() {
        long timeMillis = System.currentTimeMillis();

        Future<Integer> five = asyncService.sleep5seconds();
        Future<Integer> seven = asyncService.sleep7seconds();

        while (true) {
            if (five.isDone() && seven.isDone()) {
                break;
            }
        }

        timeMillis = System.currentTimeMillis() - timeMillis;
        return ResultVO.ok(String.format("经过了 %d 秒", timeMillis / 1000));
    }
}
