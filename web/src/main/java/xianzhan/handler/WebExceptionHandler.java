package xianzhan.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import xianzhan.pojo.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author xianzhan
 * @since 2018-05-05
 */
@ControllerAdvice
public class WebExceptionHandler {

    private static final String WEB_ERROR_VIEW = "/thymeleaf/error/error";

    @ExceptionHandler({Exception.class})
    public Object errorHandler(HttpServletRequest req, HttpServletResponse res, Exception e) {

        e.printStackTrace();

        if (isAjax(req)) {
            // json 异常处理
            return ResultVO.err(e.getMessage());
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.setViewName(WEB_ERROR_VIEW);
            return mav;
        }

    }

    private static boolean isAjax(HttpServletRequest req) {
        String header = req.getHeader("X-Requested-With");
        // ajax 请求头
        return "XMLHttpRequest".equals(header);
    }
}
